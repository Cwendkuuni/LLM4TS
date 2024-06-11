import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
    
    @Test
    public void testSetFont() {
        FontChooserDialog dialog = new FontChooserDialog();
        Font newFont = new Font("Arial", Font.BOLD, 14);
        
        dialog.setFont(newFont);
        
        assertEquals(newFont, dialog.getFont());
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog();
        Font initialFont = dialog.getFont();
        
        // Set a different font
        Font newFont = new Font("Calibri", Font.PLAIN, 12);
        dialog.setFont(newFont);
        assertNotEquals(initialFont, dialog.getFont());
        
        // Reset the font
        dialog.reset();
        
        assertEquals(initialFont, dialog.getFont());
    }
    
    @Test
    public void testVisible() {
        FontChooserDialog dialog = new FontChooserDialog();
        
        // Set initial visibility to true
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
        
        // Set visibility to false
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new Window(), WindowEvent.WINDOW_CLOSING);
        
        closer.windowClosing(windowEvent);
        
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window window = new Window();
        ComponentEvent componentEvent = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
        
        disposeOnClose.componentHidden(componentEvent);
        
        assertTrue(window.isDisposed());
    }
}