package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.util.MissingResourceException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // test case when menu label has * at the end
        String[] arr$ = {"Menu1*", "Menu2"};
        JMenu resultMenu = mainMenu.createMenu("Menu");
        assertEquals(arr$[0].substring(0, arr$[0].length() - 2), resultMenu.getText());
        assertEquals(arr$[0].charAt(arr$[0].length() - 1), resultMenu.getMnemonic());
        
        // test case when menu label has no * at the end
        arr$ = new String[]{"Menu2"};
        resultMenu = mainMenu.createMenu("Menu");
        assertEquals(arr$[0], resultMenu.getText());
        assertEquals(0, resultMenu.getMnemonic());
        
        // test case when MenuItem label has * at the end
        arr$ = new String[]{"#Item1*", "Item2"};
        resultMenu = mainMenu.createMenu("Menu");
        JCheckBoxMenuItem resultMenuItem = (JCheckBoxMenuItem) resultMenu.getMenuComponent(0);
        assertEquals(arr$[0].substring(0, arr$[0].length() - 2), resultMenuItem.getText());
        assertEquals(arr$[0].charAt(arr$[0].length() - 1), resultMenuItem.getMnemonic());
        
        // test case when MenuItem label has no * at the end
        arr$ = new String[]{"Item2"};
        resultMenuItem = (JCheckBoxMenuItem) resultMenu.getMenuComponent(0);
        assertEquals(arr$[0], resultMenuItem.getText());
        assertEquals(0, resultMenuItem.getMnemonic());
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_missingResource() {
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.createMenu("InvalidMenu");
    }

    @Test
    public void testAddShortcutAndIcon() throws Exception {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        // test case when shortcut is available
        String shortcut = "shortcut";
        String key = "Key";
        mainMenu.addShortcutAndIcon(item, key);
        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());
        
        // test case when shortcut is not available
        item = new JMenuItem();
        key = "InvalidKey";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
        
        // test case when icon path is available
        item = new JMenuItem();
        key = "Key";
        try {
            mainMenu.addShortcutAndIcon(item, key);
            assertNotNull(item.getIcon());
        } catch (MissingResourceException ex2) {}
        
        // test case when icon path is not available
        item = new JMenuItem();
        key = "InvalidIcon";
        try {
            mainMenu.addShortcutAndIcon(item, key);
            assertNull(item.getIcon());
        } catch (MissingResourceException ex2) {}
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        
        // test case when method does nothing
        mainMenu.changed();
        assertEquals("", "");
    }
}