package com.allenstudio.ir.ui;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.*;
import javax.swing.*;

import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {
    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu();
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
        //Add more assertions for the menu creation
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "New");
        assertNotNull(item.getAccelerator());
        //Add more assertions for the shortcut and icon addition
    }

    @Test
    public void testChanged() {
        //Test the changed method of MainMenu
    }
}