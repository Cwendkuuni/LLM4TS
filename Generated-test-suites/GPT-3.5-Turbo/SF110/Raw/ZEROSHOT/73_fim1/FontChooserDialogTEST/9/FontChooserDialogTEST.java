package osa.ora.server.client.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "", false, null, null, null);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @After
    public void tearDown() {
        fontChooserDialog = null;
    }
}