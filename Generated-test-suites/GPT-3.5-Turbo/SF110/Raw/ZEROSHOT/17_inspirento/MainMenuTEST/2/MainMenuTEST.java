package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;
    
    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu jMenu = mainMenu.createMenu(menu);
        
        assertNotNull(jMenu);
        assertEquals(menu + "Label", jMenu.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNotNull(item.getAccelerator());
        try {
            String path = UIResources.getString(key + "Icon");
            assertNotNull(item.getIcon());
        } catch (MissingResourceException ex2) {
            // handle exception
        }
    }
    
    @Test
    public void testChanged() {
        // Test empty implementation
        mainMenu.changed();
    }
}