package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mockMediator;

    @Before
    public void setUp() {
        mockMediator = new InspirentoMediator();
        mainMenu = new MainMenu(mockMediator);
    }

    @Test
    public void testConstructor() {
        // Verify that the mediator is set correctly
        assertEquals(mockMediator, mainMenu.getMediator());

        // Verify that the menu items are created correctly
        JMenu[] menus = mainMenu.getMenus();

        // First menu
        assertEquals("Menu1", menus[0].getText());
        assertEquals(2, menus[0].getItemCount());
        assertEquals("Item1", menus[0].getItem(0).getText());
        assertEquals("Item2", menus[0].getItem(1).getText());

        // Second menu
        assertEquals("Menu2", menus[1].getText());
        assertEquals(2, menus[1].getItemCount());
        assertEquals("Item3", menus[1].getItem(0).getText());
        assertEquals("Item4", menus[1].getItem(1).getText());
    }

    @Test
    public void testCreateMenu() {
        // Test creating menu with a label ending with '*'
        JMenu menu = mainMenu.createMenu("Menu1");
        assertEquals("Menu1*", menu.getText());
        assertEquals('1', menu.getMnemonic());
        assertEquals(2, menu.getItemCount());
        assertEquals("Item1", menu.getItem(0).getText());
        assertEquals("Item2", menu.getItem(1).getText());

        // Test creating menu with a label not ending with '*'
        menu = mainMenu.createMenu("Menu2");
        assertEquals("Menu2", menu.getText());
        assertEquals(2, menu.getItemCount());
        assertEquals("Item3", menu.getItem(0).getText());
        assertEquals("Item4", menu.getItem(1).getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test adding shortcut and icon with valid key
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Item3");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        // Test adding shortcut and icon with missing shortcut key
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Item5");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        // Test adding shortcut and icon with missing icon key
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Item6");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingResourceException() {
        // Test adding shortcut and icon with missing key
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Item7");
    }
}