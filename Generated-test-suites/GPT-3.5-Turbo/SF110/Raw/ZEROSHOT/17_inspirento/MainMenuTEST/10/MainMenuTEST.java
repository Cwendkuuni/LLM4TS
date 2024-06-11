package com.allenstudio.ir.ui;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);

        // Test if the created menu is not null
        assertNotNull(mainMenu.createMenu("menu"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);

        // Test addShortcutAndIcon with valid key
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key");
        assertNotNull(item.getAccelerator());

        // Test addShortcutAndIcon with invalid key
        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "invalidKey");
        assertNull(item2.getAccelerator());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);

        // Test if changed method does not throw any exception
        mainMenu.changed();
    }
}