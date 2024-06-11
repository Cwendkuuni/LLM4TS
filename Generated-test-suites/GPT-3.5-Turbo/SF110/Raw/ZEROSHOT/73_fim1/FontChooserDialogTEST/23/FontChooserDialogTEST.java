import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog dialog;
    private JFontChooser chooserPane;
    
    @Before
    public void setUp() {
        chooserPane = new JFontChooser();
        dialog = new FontChooserDialog(null, "Test FontChooserDialog", false, chooserPane, null, null);
    }
    
    @Test
    public void testSetVisible() {
 	dialog.setVisible(true);
 	assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testSetVisible_false() {
 	dialog.setVisible(false);
 	assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        Font initialFont = dialog.getChooserPane().getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 16);
        dialog.getChooserPane().setFont(newFont);
        dialog.reset();
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }
    
    // Add more test cases for other methods 
    
    @Test
    public void testCloseOperation_Closer() {
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = dialog.new Closer();
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }
    
    @Test
    public void testComponentHidden_DisposeOnClose() {
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = dialog.new DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(componentEvent.getComponent().isVisible());
    }
    
    @Test
    public void testConstructor_initialFont() {
        Font initialFont = dialog.getChooserPane().getFont();
        assertEquals(initialFont, null);
    }
    
    @Test
    public void testConstructor_buttonListeners() {
        JButton okButton = (JButton) dialog.getContentPane().getComponent(1).getComponent(0);
        JButton cancelButton = (JButton) dialog.getContentPane().getComponent(1).getComponent(1);
        JButton resetButton = (JButton) dialog.getContentPane().getComponent(1).getComponent(2);
        ActionListener okListener = okButton.getActionListeners()[0];
        ActionListener cancelListener = cancelButton.getActionListeners()[0];
        ActionListener resetListener = resetButton.getActionListeners()[0];
        
        assertNotNull(okListener);
        assertNotNull(cancelListener);
        assertNotNull(resetListener);
    }

}