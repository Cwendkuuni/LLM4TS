package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setup() {
        fontChooserDialog = new FontChooserDialog(null, "", false, null, null, null);
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.chooserPane.getFont(), fontChooserDialog.initialFont);
    }
}