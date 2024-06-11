package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FontChooserDialogTest {
    
    @Test
    public void testSetVisible() {
        // Test if visible is true, initialFont should be set to the font of the chooserPane
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true);
        dialog.setVisible(true);
        assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
    }
    
    @Test
    public void testReset() {
        // Test if reset sets the font of the chooserPane to the initialFont
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true);
        dialog.getChooserPane().setFont(new Font("Arial", Font.PLAIN, 12));
        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        // Test if windowClosing hides the window
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertEquals(false, windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Test if componentHidden disposes the window
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertEquals(true, componentEvent.getComponent().isDisplayable());
    }
}