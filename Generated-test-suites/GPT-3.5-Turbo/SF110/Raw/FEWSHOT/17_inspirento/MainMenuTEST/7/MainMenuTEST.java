package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testCreateMenu() {
        // Test case for menu with label having asterisk suffix
        JMenu menu = mainMenu.createMenu("Menu1");
        assertEquals("Menu1", menu.getText());
        assertEquals('1', menu.getMnemonic());
        
        // Test case for menu without label having asterisk suffix
        menu = mainMenu.createMenu("Menu2");
        assertEquals("Menu2", menu.getText());
        
        // Test case for submenu
        menu = mainMenu.createMenu("Menu3");
        JMenu subMenu = (JMenu) menu.getMenuComponent(0);
        assertEquals("SubMenu3", subMenu.getText());
        
        // Test case for checkbox menu item with label having asterisk suffix
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) menu.getMenuComponent(1);
        assertEquals("CheckBoxMenuItem3", checkBoxMenuItem.getText());
        assertEquals('3', checkBoxMenuItem.getMnemonic());
        
        // Test case for menu item with label having asterisk suffix
        JMenuItem menuItem = (JMenuItem) menu.getMenuComponent(2);
        assertEquals("MenuItem3", menuItem.getText());
        assertEquals('3', menuItem.getMnemonic());
        
        // Test case for separator
        assertEquals("Separator3", menu.getMenuComponent(3).getName());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test case for menu item without shortcut and icon
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Item1");
        assertEquals(null, menuItem.getAccelerator());
        assertEquals(null, menuItem.getIcon());
        
        // Test case for menu item with shortcut and without icon
        menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Item2");
        assertEquals("ctrl shift T", menuItem.getAccelerator().toString());
        assertEquals(null, menuItem.getIcon());
        
        // Test case for menu item without shortcut and with icon
        menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Item3");
        assertEquals(null, menuItem.getAccelerator());
        assertEquals(null, menuItem.getIcon());
        
        // Test case for menu item with shortcut and icon
        menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Item4");
        assertEquals("ctrl T", menuItem.getAccelerator().toString());
        assertEquals(new ImageIcon(this.getClass().getResource("/path/to/icon.png")), menuItem.getIcon());
    }
    
    @Test
    public void testChanged() {
        // No assertion as this is a void method
        mainMenu.changed();
    }
}