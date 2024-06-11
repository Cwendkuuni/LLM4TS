import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class FontChooserDialogTest {
    
    @Test
    public void testFontChooserDialog() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        assertNotNull(dialog);
    }
    
    @Test
    public void testFontChooserDialogWithListeners() {
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        };
        JFontChooser chooserPane = new JFontChooser();
        
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, chooserPane, okListener, cancelListener);
        assertNotNull(dialog);
    }
    
    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        
        dialog.setVisible(true);
        assertEquals(null, dialog.initialFont);
        
        dialog.setVisible(false);
        assertEquals(null, dialog.initialFont);
    }
    
    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        dialog.initialFont = new Font("Arial", Font.PLAIN, 12);
        
        dialog.reset();
        assertEquals(new Font("Arial", Font.PLAIN, 12), dialog.chooserPane.getFont());
    }
    
    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        
        WindowEvent windowEvent = new WindowEvent(new Window(new Frame()), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        
        Frame frame = new Frame();
        ComponentEvent componentEvent = new ComponentEvent(frame, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertFalse(frame.isVisible());
    }
}