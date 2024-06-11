package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontChooserDialogTest {
    private FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(new JFrame(), "Font Chooser", true,
                                                new JFontChooser(), null, null);
    }
    
    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
        
        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }
    
    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.getInitialFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JFrame frame = new JFrame();
        
        frame.addWindowListener(closer);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        
        assertFalse(frame.isVisible());
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JFrame frame = new JFrame();
        
        frame.addComponentListener(disposeOnClose);
        frame.dispatchEvent(new ComponentEvent(frame, ComponentEvent.COMPONENT_HIDDEN));
        
        assertTrue(frame.isDisposed());
    }
}