package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    private InspirentoMediator mediator;
    
    @Before
    public void setUp() {
        mediator = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(mainMenu);
        assertEquals(mainMenu.getMediator(), mediator);
    }
    
    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
        assertEquals(menu.getText(), UIResources.getString("menuLabel"));
    }
    
    @Test
    public void testAddSeparator() {
        JMenu menu = new JMenu();
        mainMenu.addSeparator(menu);
        assertEquals(menu.getMenuComponentCount(), 1);
        assertTrue(menu.getMenuComponent(0) instanceof JSeparator);
    }
    
    @Test
    public void testSetMenuMnemonic() {
        JMenu menu = new JMenu();
        mainMenu.setMenuMnemonic(menu, "menuLabel");
        assertEquals(menu.getMnemonic(), UIResources.getString("menuLabel").charAt(UIResources.getString("menuLabel").length() - 1));
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "item");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIconWithoutShortcut() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "itemWithoutShortcut");
        assertNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIconWithoutIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "itemWithoutIcon");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertion because the method does not return a value or change any properties
    }
}