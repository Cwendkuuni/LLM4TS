import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.UIManager;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.Window;
import java.awt.event.ComponentEvent;

public class FontChooserDialogTest {
    
    @Test
    public void testConstructor() {
        // Test the constructor of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        
        assertNotNull(dialog);
        assertEquals("Test Title", dialog.getTitle());
        assertFalse(dialog.isModal());
    }
    
    @Test
    public void testSetVisible() {
        // Test the setVisible method of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        
        dialog.setVisible(false);
        assertNull(dialog.initialFont);
    }
    
    @Test
    public void testReset() {
        // Test the reset method of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        
        dialog.reset();
        assertNotNull(dialog.chooserPane.getFont());
    }
    
    @Test
    public void testWindowClosing() {
        // Test the windowClosing method of Closer class
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_CLOSING);
        
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testComponentHidden() {
        // Test the componentHidden method of DisposeOnClose class
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(null, ComponentEvent.COMPONENT_HIDDEN);
        
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(componentEvent.getComponent() instanceof Window);
        assertTrue(((Window)componentEvent.getComponent()).isDisposed());
    }
}