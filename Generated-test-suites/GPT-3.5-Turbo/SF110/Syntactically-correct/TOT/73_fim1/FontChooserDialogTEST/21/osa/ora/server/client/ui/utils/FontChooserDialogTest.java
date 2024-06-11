import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog();
        // set initial font
        dialog.reset();
        // check if font is reset to initial font
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }

    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertFalse(componentEvent.getComponent().isVisible());
        assertTrue(componentEvent.getComponent().isDisposed());
    }

}