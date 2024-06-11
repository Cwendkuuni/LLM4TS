
package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

import javax.swing.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Mock
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("menuName");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "menuItemKey");
        assertNotNull(item.getAccelerator());
        Icon icon = item.getIcon();
        assertNotNull(icon);
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertions, as the method does not return anything
    }
}
