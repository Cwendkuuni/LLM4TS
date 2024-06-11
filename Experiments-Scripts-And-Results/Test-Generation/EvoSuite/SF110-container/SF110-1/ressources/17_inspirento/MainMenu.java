package com.allenstudio.ir.ui;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class MainMenu extends JMenuBar implements InspirentoWidget
{
    private static final String LABEL_SUFFIX = "Label";
    private static final String ICON_SUFFIX = "Icon";
    private static final String SHORTCUT_SUFFIX = "Shortcut";
    private InspirentoMediator mediator;
    
    public MainMenu(final InspirentoMediator mediator) {
        this.mediator = null;
        this.mediator = mediator;
        final String[] arr$;
        final String[] menus = arr$ = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        for (final String menu : arr$) {
            this.add(this.createMenu(menu));
        }
    }
    
    private JMenu createMenu(final String menu) {
        JMenu jMenu = null;
        try {
            final String tempLabel = UIResources.getString(menu + "Label");
            if (tempLabel.length() > 2 && tempLabel.charAt(tempLabel.length() - 2) == '*') {
                jMenu = new JMenu(tempLabel.substring(0, tempLabel.length() - 2));
                jMenu.setMnemonic(tempLabel.charAt(tempLabel.length() - 1));
            }
            else {
                jMenu = new JMenu(tempLabel);
            }
            final String[] arr$;
            final String[] items = arr$ = InspirentoUtilities.tokenize(UIResources.getString(menu));
            for (final String item : arr$) {
                if (!"-".equals(item)) {
                    if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
                        jMenu.add(this.createMenu(item.substring(0, item.length() - 1)));
                    }
                    else if (item.charAt(0) == '#') {
                        final String itemLabel = UIResources.getString(item.substring(1) + "Label");
                        JCheckBoxMenuItem jMenuItem = null;
                        if (itemLabel.length() > 2 && itemLabel.charAt(itemLabel.length() - 2) == '*') {
                            jMenuItem = new JCheckBoxMenuItem(itemLabel.substring(0, itemLabel.length() - 2));
                            jMenuItem.setMnemonic(itemLabel.charAt(itemLabel.length() - 1));
                        }
                        else {
                            jMenuItem = new JCheckBoxMenuItem(itemLabel);
                        }
                        this.addShortcutAndIcon(jMenuItem, item);
                        jMenu.add(jMenuItem);
                    }
                    else {
                        final String itemLabel = UIResources.getString(item + "Label");
                        JMenuItem jMenuItem2 = null;
                        if (itemLabel.length() > 2 && itemLabel.charAt(itemLabel.length() - 2) == '*') {
                            jMenuItem2 = new JMenuItem(itemLabel.substring(0, itemLabel.length() - 2));
                            jMenuItem2.setMnemonic(itemLabel.charAt(itemLabel.length() - 1));
                        }
                        else {
                            jMenuItem2 = new JMenuItem(itemLabel);
                        }
                        this.addShortcutAndIcon(jMenuItem2, item);
                        jMenu.add(jMenuItem2);
                    }
                }
                else {
                    jMenu.addSeparator();
                }
            }
        }
        catch (MissingResourceException e) {
            System.out.println("Cannot load certain menu label(s).\n" + e);
            System.exit(2);
        }
        return jMenu;
    }
    
    private void addShortcutAndIcon(final JMenuItem item, final String key) {
        try {
            final String shortcut = UIResources.getString(key + "Shortcut");
            item.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        }
        catch (MissingResourceException ex) {}
        try {
            final String path = UIResources.getString(key + "Icon");
            final URL url = this.getClass().getResource(path);
            if (url != null) {
                item.setIcon(new ImageIcon(url));
            }
            else {
                System.out.println("Warning: icon file lost!");
            }
        }
        catch (MissingResourceException ex2) {}
    }
    
    @Override
    public void changed() {
    }
}
