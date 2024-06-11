package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mockMediator;

    @Before
    public void setUp() {
        mockMediator = new InspirentoMediator();
        mainMenu = new MainMenu(mockMediator);
    }

    @Test
    public void testCreateMenu() {
        // Test case for menu with label ending with '*'
        JMenu menu = mainMenu.createMenu("TestMenu*");
        assertNotNull(menu);
        assertEquals("TestMenu", menu.getText());
        assertEquals('r', menu.getMnemonic());

        // Test case for menu without label ending with '*'
        menu = mainMenu.createMenu("TestMenu");
        assertNotNull(menu);
        assertEquals("TestMenu", menu.getText());
        assertEquals(0, menu.getMnemonic());

        // Add more test cases for different scenarios
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case for existing shortcut
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "TestKeyShortcut");
        assertNotNull(item.getAccelerator());

        // Test case for missing shortcut
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "MissingKeyShortcut");
        assertNull(item.getAccelerator());

        // Test case for existing icon path
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "TestKeyIcon");
        assertNotNull(item.getIcon());

        // Test case for missing icon path
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "MissingKeyIcon");
        assertNull(item.getIcon());

        // Add more test cases for different scenarios
    }

    // Add more test cases for other methods in the MainMenu class

}