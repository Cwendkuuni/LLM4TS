package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class FontChooserDialogTest {
    
    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", false, new JFontChooser(), null, null);
        assertEquals("", dialog.getTitle());
        assertNull(dialog.getInitialFont());
        assertNull(dialog.getChooserPane());
    }
    
    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", false, new JFontChooser(), null, null);
        dialog.setVisible(true);
        assertEquals(dialog.getChooserPane().getFont(), dialog.getInitialFont());
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", false, new JFontChooser(), null, null);
        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
    }
    
    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        // Test code for windowClosing event
    }
    
    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        // Test code for componentHidden event
    }
}