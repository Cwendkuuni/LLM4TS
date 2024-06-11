package com.allenstudio.ir.ui;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setup() {
        InspirentoMediator mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        // Test case for menu with label length > 2 and ends with '*'
        JMenu menu1 = mainMenu.createMenu("menu1");
        assertEquals("Menu1", menu1.getText());
        assertEquals('1', menu1.getMnemonic());
        
        // Test case for menu with label not ending with '*'
        JMenu menu2 = mainMenu.createMenu("menu2");
        assertEquals("Menu2", menu2.getText());
        assertEquals(0, menu2.getMnemonic());

        // Test case for submenu with label length > 2 and ends with '*'
        JMenu menu3 = mainMenu.createMenu("menu3");
        assertEquals("Menu3", menu3.getText());
        assertEquals('3', menu3.getMnemonic());
        assertEquals(1, menu3.getItemCount());
        assertTrue(menu3.getItem(0) instanceof JMenu);
        assertEquals("SubMenu1", ((JMenu)menu3.getItem(0)).getText());
        
        // Test case for submenu with label not ending with '*'
        JMenu menu4 = mainMenu.createMenu("menu4");
        assertEquals("Menu4", menu4.getText());
        assertEquals(0, menu4.getMnemonic());
        assertEquals(1, menu4.getItemCount());
        assertTrue(menu4.getItem(0) instanceof JMenu);
        assertEquals("SubMenu2", ((JMenu)menu4.getItem(0)).getText());

        // Test case for menu with missing resource
        try {
            JMenu menu5 = mainMenu.createMenu("invalidMenu");
            fail("MissingResourceException expected");
        } catch (MissingResourceException e) {
            // pass test
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "item1");
        assertEquals(KeyStroke.getKeyStroke("control A"), item1.getAccelerator());

        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "item2");
        assertNull(item2.getAccelerator());

        JMenuItem item3 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item3, "item3");
        assertEquals(KeyStroke.getKeyStroke("alt shift P"), item3.getAccelerator());

        JMenuItem item4 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item4, "item4");
        assertNull(item4.getAccelerator());

        JMenuItem item5 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item5, "invalidItem");
        assertNull(item5.getAccelerator());

        JMenuItem item6 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item6, "item6");
        assertEquals(new ImageIcon(new URL("https://example.com/icon1.png")), item6.getIcon());

        JMenuItem item7 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item7, "item7");
        assertNull(item7.getIcon());

        JMenuItem item8 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item8, "item8");
        assertEquals(new ImageIcon(new URL("https://example.com/icon2.png")), item8.getIcon());

        JMenuItem item9 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item9, "invalidItem");
        assertNull(item9.getIcon());

        JMenuItem item10 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item10, "item10");
        assertNull(item10.getAccelerator());
        assertEquals(new ImageIcon(new URL("https://example.com/icon3.png")), item10.getIcon());
    }
}