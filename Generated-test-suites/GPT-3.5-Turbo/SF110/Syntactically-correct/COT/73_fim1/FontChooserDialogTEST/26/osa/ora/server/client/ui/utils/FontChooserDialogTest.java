
package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class FontChooserDialogTest {

    private Component component;
    private String title;
    private boolean modal;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;
    private FontChooserDialog dialog;

    @Before
    public void setUp() {
        component = new JPanel();
        title = "Title";
        modal = true;
        chooserPane = new JFontChooser();
        okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform some action
            }
        };
        cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform some action
            }
        };
        dialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testFontChooserDialog() {
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        dialog.setVisible(true);
        assertEquals(chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testReset() {
        dialog.reset();
        assertEquals(dialog.initialFont, chooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        WindowEvent event = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);

        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(event);

        assertFalse(dialog.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        ComponentEvent event = new ComponentEvent(dialog, ComponentEvent.COMPONENT_HIDDEN);

        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(event);

        assertFalse(dialog.isDisplayable());
    }
}

