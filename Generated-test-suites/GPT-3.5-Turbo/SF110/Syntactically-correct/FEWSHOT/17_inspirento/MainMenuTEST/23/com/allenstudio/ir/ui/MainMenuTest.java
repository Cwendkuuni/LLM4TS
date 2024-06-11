
package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {
    private MainMenu mainMenu;

    @Before
    public void setUp() {
        // Create a dummy InspirentoMediator object for testing
        InspirentoMediator mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        // Test case when menu label ends with "*"
        JMenu menu1 = mainMenu.createMenu("File");
        assertEquals("File", menu1.getText());
        assertEquals('F', menu1.getMnemonic());

        // Test case when menu label doesn't end with "*"
        JMenu menu2 = mainMenu.createMenu("Edit");
        assertEquals("Edit", menu2.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case when shortcut and icon exist
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "File");
        assertNotNull(item1.getAccelerator());
        assertNotNull(item1.getIcon());

        // Test case when only shortcut exists
        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "Edit");
        assertNotNull(item2.getAccelerator());
        assertNull(item2.getIcon());

        // Test case when only icon exists
        JMenuItem item3 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item3, "Help");
        assertNull(item3.getAccelerator());
        assertNotNull(item3.getIcon());

        // Test case when neither shortcut nor icon exist
        JMenuItem item4 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item4, "View");
        assertNull(item4.getAccelerator());
        assertNull(item4.getIcon());
    }
}
