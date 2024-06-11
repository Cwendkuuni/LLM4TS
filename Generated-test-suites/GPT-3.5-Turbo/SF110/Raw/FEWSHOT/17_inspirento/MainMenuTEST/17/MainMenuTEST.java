package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        // Test case for menu with normal label
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertEquals("File", menu.getText());
        
        // Test case for menu with label containing an asterisk
        mainMenu = new MainMenu(null);
        menu = mainMenu.createMenu("Edit");
        assertEquals("Edit", menu.getText());
        
        // Test case for menu with submenu
        mainMenu = new MainMenu(null);
        menu = mainMenu.createMenu("Help");
        assertEquals("Help", menu.getText());
               
        // Test case for menu with missing resource exception
        mainMenu = new MainMenu(null);
        try {
            menu = mainMenu.createMenu("MissingMenu");
            fail("Expected MissingResourceException to be thrown");
        } catch (MissingResourceException e) {
            assertEquals("Cannot load certain menu label(s).\n" + e, e.getMessage());
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case for shortcut and icon
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "NewItem");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());

        // Test case for missing shortcut
        mainMenu = new MainMenu(null);
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "MissingShortcut");
        assertNull(item.getAccelerator());

        // Test case for missing icon
        mainMenu = new MainMenu(null);
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "MissingIcon");
        assertNull(item.getIcon());
    }
}