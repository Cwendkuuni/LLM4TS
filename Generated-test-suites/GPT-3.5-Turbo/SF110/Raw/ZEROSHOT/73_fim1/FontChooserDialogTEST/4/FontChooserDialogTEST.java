package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontDialog;
    
    @Before
    public void setUp() {
        fontDialog = new FontChooserDialog(null, "Font Chooser Dialog", false, null, null, null);
    }
    
    @Test
    public void testReset() {
        Font initialFont = fontDialog.chooserPane.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        fontDialog.chooserPane.setFont(newFont);
        
        fontDialog.reset();
        Font resetFont = fontDialog.chooserPane.getFont();
        
        assertEquals(initialFont, resetFont);
    }
    
    @Test
    public void testSetVisible() {
        assertFalse(fontDialog.isVisible());
        
        fontDialog.setVisible(true);
        assertTrue(fontDialog.isVisible());
        
        fontDialog.setVisible(false);
        assertFalse(fontDialog.isVisible());
    }
    
    @Test
    public void testCloser() {
        WindowEvent windowEvent = new WindowEvent(new Frame(), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);
        
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testDisposeOnClose() {
        ComponentEvent componentEvent = new ComponentEvent(new Frame(), ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);
        
        assertTrue(componentEvent.getComponent().isDisplayable());
        assertFalse(componentEvent.getComponent().isVisible());
    }
}