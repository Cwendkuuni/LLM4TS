package com.allenstudio.ir.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu();
    }
    
    @Test
    public void testCreateMenu() {
        assertEquals(jMenu, mainMenu.createMenu("menu"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        assert(mainMenu.addShortcutAndIcon(item, key));
    }

    @Test
    public void testChanged() {
    }
}