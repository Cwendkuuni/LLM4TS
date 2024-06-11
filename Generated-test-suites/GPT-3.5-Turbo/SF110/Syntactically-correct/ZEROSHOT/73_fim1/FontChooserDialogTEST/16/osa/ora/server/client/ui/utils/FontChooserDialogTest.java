package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", false, null, null, null);
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", false, null, null, null);
        dialog.reset();
    }
}