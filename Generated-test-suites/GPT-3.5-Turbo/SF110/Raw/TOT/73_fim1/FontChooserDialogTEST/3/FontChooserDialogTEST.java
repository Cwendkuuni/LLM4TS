import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog();
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent event = new WindowEvent(new Window(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(event);
        assertFalse(event.getWindow().isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent event = new ComponentEvent(new Window(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(event);
        assertTrue(((Window) event.getComponent()).isDisposed());
    }
}