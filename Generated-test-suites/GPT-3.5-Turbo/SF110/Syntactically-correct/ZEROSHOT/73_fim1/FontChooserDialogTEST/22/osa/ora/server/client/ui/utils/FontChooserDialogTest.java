package osa.ora.server.client.ui.utils;

import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog();
        dialog.setVisible(true);
        
        // Test visibility
        assertTrue(dialog.isVisible());
        
        // Test initialFont is set
        assertNotNull(dialog.initialFont);
        
        dialog.setVisible(false);
        
        // Test visibility
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog();
        
        // Set initialFont
        dialog.initialFont = new Font("Arial", Font.PLAIN, 12);
        
        // Reset font
        dialog.reset();
        
        // Test font is reset to initialFont
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent event = new WindowEvent(new Window(), WindowEvent.WINDOW_CLOSING);
        
        // Test window closing
        closer.windowClosing(event);
        
        // Test window is not visible
        assertFalse(event.getWindow().isVisible());
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent event = new ComponentEvent(new Window(), ComponentEvent.COMPONENT_HIDDEN);
        
        // Test component hidden
        disposeOnClose.componentHidden(event);
        
        // Test window is disposed
        assertTrue(event.getComponent().isDisposed());
    }
}