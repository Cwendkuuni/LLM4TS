package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    @Test
    public void testVisible() {
        // Test case to check if the dialog is visible when set to true
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.setVisible(true);
        assertEquals(true, dialog.isVisible());
    }

    @Test
    public void testReset() {
        // Test case to check if the reset button resets the font to initialFont
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
    
    // Add more test methods to cover all the methods in the FontChooserDialog class
        
}