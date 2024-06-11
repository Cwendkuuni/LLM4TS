import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        // Test the constructor of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testFontChooserDialogSetVisible() {
        // Test the setVisible method of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog);
    }

    @Test
    public void testFontChooserDialogReset() {
        // Test the reset method of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
        dialog.reset();
        assertNotNull(dialog);
    }

    @Test
    public void testWindowCloserWindowClosing() {
        // Test the windowClosing method of WindowCloser
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(null);
        assertNotNull(closer);
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Test the componentHidden method of DisposeOnClose
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(null);
        assertNotNull(disposeOnClose);
    }

}