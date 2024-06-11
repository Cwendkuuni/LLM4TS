package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.util.*;
import java.net.*;

import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "item");
        assertNull(item.getAccelerator());

        mainMenu.addShortcutAndIcon(item, "itemShortcut");
        assertNotNull(item.getAccelerator());

        mainMenu.addShortcutAndIcon(item, "itemIcon");
        assertNull(item.getIcon());
    }

}