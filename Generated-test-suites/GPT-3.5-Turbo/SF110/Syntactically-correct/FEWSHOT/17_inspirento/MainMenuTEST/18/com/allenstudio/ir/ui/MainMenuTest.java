package com.allenstudio.ir.ui;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void testCreateMenu() {
        // Test case for creating a menu with a label ending in "*" and mnemonic
        JMenu menu1 = mainMenu.createMenu("menu1");
        assertEquals("Menu1", menu1.getText());
        assertEquals('M', menu1.getMnemonic());

        // Test case for creating a menu with a normal label
        JMenu menu2 = mainMenu.createMenu("menu2");
        assertEquals("Menu2", menu2.getText());
        assertEquals('\u0000', menu2.getMnemonic());

        // Test case for creating a submenu
        JMenu menu3 = mainMenu.createMenu("menu3");
        assertEquals("Menu3", menu3.getText());
        assertNull(menu3.getMnemonic());
        assertEquals(1, menu3.getMenuComponentCount());

        // Test case for creating a checkbox menu item
        JCheckBoxMenuItem checkBoxMenuItem1 = (JCheckBoxMenuItem) menu3.getMenuComponent(0);
        assertEquals("Item1", checkBoxMenuItem1.getText());
        assertEquals('I', checkBoxMenuItem1.getMnemonic());
        assertNull(checkBoxMenuItem1.getAccelerator());
        assertNull(checkBoxMenuItem1.getIcon());

        // Test case for creating a menu item
        JMenuItem menuItem1 = (JMenuItem) menu3.getMenuComponent(1);
        assertEquals("Item2", menuItem1.getText());
        assertEquals('I', menuItem1.getMnemonic());
        assertNotNull(menuItem1.getAccelerator());
        assertNull(menuItem1.getIcon());

        // Test case for creating a separator
        JSeparator separator = (JSeparator) menu3.getMenuComponent(2);
        assertNotNull(separator);
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "item1");
        assertNotNull(menuItem.getAccelerator());
        assertNull(menuItem.getIcon());

        JMenuItem menuItem2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem2, "item2");
        assertNotNull(menuItem2.getAccelerator());
        assertNotNull(menuItem2.getIcon());
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertion, just a method call
    }
}