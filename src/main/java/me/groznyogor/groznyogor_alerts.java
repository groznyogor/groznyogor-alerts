package me.groznyogor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class groznyogor_alerts extends JavaPlugin {

    private String defaultTitle;

    @Override
    public void onEnable() {
        // Rejestrowanie komend
        getCommand("alert").setExecutor(new AlertCommand());
        getCommand("reloadalertplugin").setExecutor(new ReloadCommand());

        // Ładowanie konfiguracji
        loadConfig();
    }

    private void loadConfig() {
        getConfig().addDefault("defaultTitle", "&cOGŁOSZENIE");
        getConfig().options().copyDefaults(true);
        saveConfig();

        defaultTitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("defaultTitle"));
    }

    private class AlertCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Ta komenda może być używana tylko przez graczy!");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("groznyogor.alert")) {
                player.sendMessage(ChatColor.RED + "Nie masz odpowiednich uprawnień do użycia tej komendy!");
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Użycie: /alert <wiadomość>");
                return true;
            }

            String message = String.join(" ", args);

            String title = defaultTitle;

            player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', message), 10, 70, 20);

            return true;
        }
    }

    private class ReloadCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!sender.hasPermission("groznyogor.alert.reload")) {
                sender.sendMessage(ChatColor.RED + "Nie masz uprawnień do przeładowywania pluginu!");
                return true;
            }

            reloadConfig();
            loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Konfiguracja pluginu została przeładowana!");

            return true;
        }
    }
}
