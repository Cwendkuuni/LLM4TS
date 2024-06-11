
package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        assertEquals(null, mainMenu.createMenu(null));
        // Add more test cases
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Add test cases
    }

    @Test
    public void testChanged() {
        // Add test cases
    }
}
