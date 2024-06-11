package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", false, new JFontChooser(), null, null);
    }

    @Test
    public void testReset() {
        assertNotNull(fontChooserDialog);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.chooserPane.getFont(), fontChooserDialog.initialFont);
    }

}