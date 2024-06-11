package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooser;
    private JFontChooser chooserPane;

    @Before
    public void setUp() {
        chooserPane = new JFontChooser();
        fontChooser = new FontChooserDialog(null, "Test Font Chooser Dialog", true, chooserPane,
                null, null);
    }

    @After
    public void tearDown() {
        fontChooser = null;
        chooserPane = null;
    }

    @Test
    public void testSetVisible() {
        fontChooser.setVisible(true);
        assertFalse(fontChooser.isResizable());
        assertTrue(fontChooser.isEnabled());
    }

    @Test
    public void testReset() {
        Font initialFont = chooserPane.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(newFont);
        fontChooser.reset();
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testCloser_WindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(fontChooser, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(fontChooser.isVisible());
    }

    @Test
    public void testDisposeOnClose_ComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(fontChooser, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertFalse(fontChooser.isDisplayable());
    }
}