package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.MissingResourceException;
import java.net.URL;

import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }

    @Test
    public void testAddShortcutAndIconWithValidShortcutKey() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "File");
        assertNotNull(item.getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIconWithInvalidShortcutKey() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Edit");
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIconWithValidIconPath() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Help");
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIconWithInvalidIconPath() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "View");
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertion, just testing that the method doesn't throw an exception
    }
}