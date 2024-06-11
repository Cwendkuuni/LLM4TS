import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
    
    @Test
    public void testFontChooserDialog() {
        // Test the constructor of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, choosePane, okListener, cancelListener);
        assertNotNull(dialog);
    }
    
    @Test
    public void testSetVisible() {
        // Test the setVisible method
        FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, choosePane, okListener, cancelListener);
        dialog.setVisible(true);
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        // Test the reset method
        FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, choosePane, okListener, cancelListener);
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        // Test the windowClosing method of Closer class
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window w = new Window();
        WindowEvent windowEvent = new WindowEvent(w, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(w.isVisible());
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Test the componentHidden method of DisposeOnClose class
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window w = new Window();
        ComponentEvent componentEvent = new ComponentEvent(w, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertFalse(w.isVisible());
    }
}