
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Window;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {
    
    @Test
    public void testFontChooserDialog() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        assertNotNull(dialog);
        assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        assertFalse(dialog.isVisible());
        
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        Font initialFont = dialog.getFont();
        Font newFont = new Font("Arial", Font.BOLD, 14);
        
        dialog.setFont(newFont);
        assertEquals(newFont, dialog.getFont());
        
        dialog.reset();
        assertEquals(initialFont, dialog.getFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        
        assertDoesNotThrow(() -> closer.windowClosing(event));
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent event = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        
        assertDoesNotThrow(() -> disposeOnClose.componentHidden(event));
    }
    
}
