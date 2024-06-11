package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        // Test case 1: menu = "menu"
        JMenu menu1 = mainMenu.createMenu("menu");
        assertNotNull(menu1);
        assertEquals("Menu", menu1.getText());
        
        // Test case 2: menu = "file"
        JMenu menu2 = mainMenu.createMenu("file");
        assertNotNull(menu2);
        assertEquals("File", menu2.getText());
        
        // Test case 3: menu = "edit"
        JMenu menu3 = mainMenu.createMenu("edit");
        assertNotNull(menu3);
        assertEquals("Edit", menu3.getText());
        
        // Test case 4: menu = "view"
        JMenu menu4 = mainMenu.createMenu("view");
        assertNotNull(menu4);
        assertEquals("View", menu4.getText());
        
        // Test case 5: menu = "tools"
        JMenu menu5 = mainMenu.createMenu("tools");
        assertNotNull(menu5);
        assertEquals("Tools", menu5.getText());
        
        // Test case 6: menu = "help"
        JMenu menu6 = mainMenu.createMenu("help");
        assertNotNull(menu6);
        assertEquals("Help", menu6.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        
        // Test case 1: key = "new", shortcut = "ctrl+n", icon = "newIcon.png"
        mainMenu.addShortcutAndIcon(item, "new");
        assertNotNull(item.getAccelerator());
        assertEquals("ctrl N", item.getAccelerator().toString());
        
        // Test case 2: key = "open", shortcut = "ctrl+o", icon = "openIcon.png"
        mainMenu.addShortcutAndIcon(item, "open");
        assertNotNull(item.getAccelerator());
        assertEquals("ctrl O", item.getAccelerator().toString());
        
        // Test case 3: key = "save", shortcut = "ctrl+s", icon = "saveIcon.png"
        mainMenu.addShortcutAndIcon(item, "save");
        assertNotNull(item.getAccelerator());
        assertEquals("ctrl S", item.getAccelerator().toString());
        
        // Test case 4: key = "undo", shortcut = "ctrl+z", icon = "undoIcon.png"
        mainMenu.addShortcutAndIcon(item, "undo");
        assertNotNull(item.getAccelerator());
        assertEquals("ctrl Z", item.getAccelerator().toString());
        
        // Test case 5: key = "redo", shortcut = "ctrl+y", icon = "redoIcon.png"
        mainMenu.addShortcutAndIcon(item, "redo");
        assertNotNull(item.getAccelerator());
        assertEquals("ctrl Y", item.getAccelerator().toString());
    }
}