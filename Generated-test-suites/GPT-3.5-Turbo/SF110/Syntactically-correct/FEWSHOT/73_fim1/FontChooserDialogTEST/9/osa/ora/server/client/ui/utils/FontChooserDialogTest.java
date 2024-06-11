package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {
    private FontChooserDialog fontChooserDialog;
    private JFontChooser jFontChooser;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "", false, null, null, null);
        jFontChooser = new JFontChooser();
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertNotNull(fontChooserDialog.chooserPane);  
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new Window(new Frame()), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertNotNull(windowEvent.getWindow());
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new Window(new Frame()), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertNotNull(componentEvent.getComponent());
    }
}