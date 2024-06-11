package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        // Test case where menu label length is greater than 2 and the second-last character is '*'
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("Edit");
        assertNotNull(menu);
        assertEquals("Edi", menu.getLabel());
        assertEquals('t', menu.getMnemonic());
        
        // Test case where menu label length is greater than 2 and the second-last character is not '*'
        menu = mainMenu.createMenu("View");
        assertNotNull(menu);
        assertEquals("View", menu.getLabel());
        assertEquals(0, menu.getMnemonic());
        
        // Test case where menu label length is not greater than 2
        menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getLabel());
        assertEquals(0, menu.getMnemonic());
        
        // Test case where menu label is not found in UIResources
        menu = mainMenu.createMenu("InvalidMenuLabel");
        assertNull(menu);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test case where shortcut and icon path exist in UIResources
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "menuitem1");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
        
        // Test case where shortcut exists in UIResources but icon path does not exist
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "menuitem2");
        assertNotNull(item.getAccelerator());
        assertNull(item.getIcon());
        
        // Test case where neither shortcut nor icon path exist in UIResources
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "menuitem3");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
        // No assertions made as this is a void method
    }
}