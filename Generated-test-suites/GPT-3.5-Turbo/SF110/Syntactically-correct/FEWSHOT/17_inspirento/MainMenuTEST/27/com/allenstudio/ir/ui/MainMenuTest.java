
package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
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
        assertNotNull(mainMenu.createMenu("menu"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertions needed, method does not return any value
    }
}
