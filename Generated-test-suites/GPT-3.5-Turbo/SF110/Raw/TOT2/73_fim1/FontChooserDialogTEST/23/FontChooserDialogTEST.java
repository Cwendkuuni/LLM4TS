package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "Test", true, new JFontChooser(), null, null);
    }

    @Test
    public void testVisible() {
        assertFalse(fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.getInitialFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        assertNotNull(closer);
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        assertNotNull(disposeOnClose);
    }
}