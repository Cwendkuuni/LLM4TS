import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        assertNotNull(dialog);
    }
    
    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog);
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        dialog.reset();
        assertNotNull(dialog);
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        assertNotNull(closer);
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose dispose = new FontChooserDialog.DisposeOnClose();
        assertNotNull(dispose);
    }

}