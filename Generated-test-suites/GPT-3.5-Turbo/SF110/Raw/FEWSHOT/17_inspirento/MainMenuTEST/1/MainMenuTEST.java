package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.InspirentoUtilities;
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
    public void testConstructor() throws MissingResourceException {
        verify(mediator, times(1)).changed();
    }

    @Test
    public void testCreateMenu() {
        // Test menu creation
        JMenu jMenu = mainMenu.createMenu("menu");
        assertEquals("menuLabel", jMenu.getText());

        // Test submenu creation
        jMenu = mainMenu.createMenu("submenu");
        assertEquals("submenuLabel", jMenu.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        
        // Test adding shortcut
        UIResources.setResource("keyShortcut", "shortcutKey");
        mainMenu.addShortcutAndIcon(item, "key");
        assertEquals(KeyStroke.getKeyStroke("shortcutKey"), item.getAccelerator());

        // Test adding icon
        UIResources.setResource("keyIcon", "/path/to/icon.png");
        mainMenu.addShortcutAndIcon(item, "key");
        URL url = mainMenu.getClass().getResource("/path/to/icon.png");
        assertEquals(new ImageIcon(url), item.getIcon());
    }

    @Test
    public void testChanged() {
        // No assertion, void method
        mainMenu.changed();
    }
}