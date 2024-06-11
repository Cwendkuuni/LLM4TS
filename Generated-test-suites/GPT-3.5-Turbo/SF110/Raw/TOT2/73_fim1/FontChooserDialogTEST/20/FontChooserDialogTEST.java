import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        // Test FontChooserDialog constructor functionality
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);
        assertNotNull(dialog);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
    }

    @Test
    public void testFontChooserDialogReset() {
        // Test reset method functionality
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);
        Font initialFont = dialog.chooserPane.getFont();
        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testFontChooserDialogVisibility() {
        // Test setVisible method functionality
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testCloser() {
        // Test Closer class functionality
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window window = new Window(null);
        WindowEvent windowEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(window.isVisible());
    }

    @Test
    public void testDisposeOnClose() {
        // Test DisposeOnClose class functionality
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window window = new Window(null);
        ComponentEvent componentEvent = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(window.isShowing());
    }
}