package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }
    
    @Test
    public void testCreateMenu() {
        // Test case 1
        String menu = "menu";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);
        
        // Test case 2
        String invalidMenu = "invalidMenu";
        JMenu invalidResult = mainMenu.createMenu(invalidMenu);
        assertNull(invalidResult);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test case 1
        JMenuItem item = new JMenuItem();
        String key = "key";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
        
        // Test case 2
        String validShortcut = "validShortcut";
        String validKey = "validKey";
        JMenuItem validItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(validItem, validKey);
        assertEquals(validShortcut, validItem.getAccelerator());
        assertNotNull(validItem.getIcon());
    }
    
    @Test
    public void testChanged() {
        // No assertion needed, just test if the method executes successfully
        mainMenu.changed();
    }
}