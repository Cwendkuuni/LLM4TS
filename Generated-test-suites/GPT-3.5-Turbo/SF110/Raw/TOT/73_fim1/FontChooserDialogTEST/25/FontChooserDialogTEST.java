import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        assertNotNull(dialog);
    }
    
    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }
    
    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new Frame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new Frame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(componentEvent.getComponent().isShowing());
        assertFalse(componentEvent.getComponent().isValid());
    }
}