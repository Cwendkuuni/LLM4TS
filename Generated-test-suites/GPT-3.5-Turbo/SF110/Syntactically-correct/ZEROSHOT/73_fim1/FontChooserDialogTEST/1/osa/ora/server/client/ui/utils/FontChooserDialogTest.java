
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    private Component component;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;

    @Before
    public void setUp() {
        component = new Component() {};
        chooserPane = new JFontChooser();
        okListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // OK button action listener
            }
        };
        cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Cancel button action listener
            }
        };

        fontChooserDialog = new FontChooserDialog(component, "Test Dialog", false, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
        assertNotNull(fontChooserDialog.getRootPane());
        assertNotNull(fontChooserDialog.getContentPane());
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
        assertNotNull(fontChooserDialog.initialFont);

        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }

    @After
    public void tearDown() {
        fontChooserDialog.dispose();
    }
}

