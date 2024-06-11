package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "", false, new JFontChooser(), null, null);
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.chooserPane.getFont();
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testVisible() {
        boolean visible = true;
        fontChooserDialog.setVisible(visible);
        assertEquals(visible, fontChooserDialog.isVisible());
    }

    @Test
    public void testCloserWindowClosing() {
        Window window = new JFrame();
        window.addWindowListener(new FontChooserDialog.Closer());
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        assertFalse(window.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        Window window = new JFrame();
        window.addComponentListener(new FontChooserDialog.DisposeOnClose());
        window.dispatchEvent(new WindowEvent(window, WindowEvent.COMPONENT_HIDDEN));
        assertTrue(window.isDisposed());
    }
}