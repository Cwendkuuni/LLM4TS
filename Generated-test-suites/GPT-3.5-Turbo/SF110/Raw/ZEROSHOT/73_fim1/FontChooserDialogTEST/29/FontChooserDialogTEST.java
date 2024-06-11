
package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        fontChooserDialog = new FontChooserDialog(
                new JPanel(),
                "Test Dialog",
                true,
                chooserPane,
                okListener,
                cancelListener
        );
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.chooserPane.getFont();
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testSetVisible() {
        boolean visible = true;
        fontChooserDialog.setVisible(visible);
        assertEquals(visible, fontChooserDialog.isVisible());
    }

    @Test
    public void testCloserWindowClosing() {
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);
        assertEquals(false, windowEvent.getWindow().isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);
        assertEquals(true, componentEvent.getComponent().isShowing());
    }
}
