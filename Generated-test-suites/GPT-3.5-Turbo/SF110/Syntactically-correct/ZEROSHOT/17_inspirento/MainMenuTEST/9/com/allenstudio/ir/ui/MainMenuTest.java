package com.allenstudio.ir.ui;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {

    private InspirentoMediator mediator;
    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mediator = Mockito.mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "item");
        boolean hasShortcut = (item.getAccelerator() != null);
        assertTrue(hasShortcut);
    }

    @Test
    public void testChanged() {
        // Assuming no implementation is required for this method
    }

}