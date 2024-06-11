package osa.ora.server.client.ui.utils;
import static org.junit.Assert.*;
import org.junit.Test;

public class FontChooserDialogTest {

    FontChooserDialog dialog;

    @Test
    public void testVisible() {
        dialog = new FontChooserDialog(null, "Test Dialog", false, new JFontChooser(), null, null);
        dialog.setVisible(true);
        assertNotNull(dialog);
    }

    @Test
    public void testReset() {
        dialog = new FontChooserDialog(null, "Test Dialog", false, new JFontChooser(), null, null);
        dialog.reset();
        assertNotNull(dialog);
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        assertNotNull(closer);
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        assertNotNull(disposeOnClose);
    }
}