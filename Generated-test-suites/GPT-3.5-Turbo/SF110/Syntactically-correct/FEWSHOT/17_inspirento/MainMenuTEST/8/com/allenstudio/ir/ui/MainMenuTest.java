package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        InspirentoUtilities inspirentoUtilitiesMock = mock(InspirentoUtilities.class);
        UIResources uiResourcesMock = mock(UIResources.class);
        MainMenu mainMenu = new MainMenu(mediator);
        assertEquals(jMenu, mainMenu.createMenu(menu));
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem jMenuItemMock = mock(JMenuItem.class);
        MainMenu mainMenu = new MainMenu(mediator);
        mainMenu.addShortcutAndIcon(jMenuItemMock, key);
        assertEquals(expectedAccelerator, jMenuItemMock.getAccelerator());
        assertEquals(expectedIcon, jMenuItemMock.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(mediator);
        mainMenu.changed();
        // Add assertions here
    }
}