import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {
    
    FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog();
    }
    
    @After
    public void tearDown() {
        fontChooserDialog.dispose();
    }
    
    // Test case for the constructor
    @Test
    public void testFontChooserDialog() {
        assertNotNull(fontChooserDialog);
    }
    
    // Test case for the setVisible() method when visible is true
    @Test
    public void testSetVisibleTrue() {
        boolean visible = true;
        fontChooserDialog.setVisible(visible);
        assertEquals(visible, fontChooserDialog.isVisible());
        assertNotNull(fontChooserDialog.initialFont);
    }
    
    // Test case for the setVisible() method when visible is false
    @Test
    public void testSetVisibleFalse() {
        boolean visible = false;
        fontChooserDialog.setVisible(visible);
        assertEquals(visible, fontChooserDialog.isVisible());
        assertNull(fontChooserDialog.initialFont);
    }
    
    // Test case for the reset() method
    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }
    
    // Test case for the Closer class's windowClosing() method
    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(closer);
        closer.windowClosing(null);
        assertFalse(dialog.isVisible());
    }
    
    // Test case for the DisposeOnClose class's componentHidden() method
    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JDialog dialog = new JDialog();
        dialog.addComponentListener(disposeOnClose);
        disposeOnClose.componentHidden(null);
        assertFalse(dialog.isShowing());
        assertTrue(dialog.isDisposed());
    }
    
    // Test case for the OK button's ActionListener
    @Test
    public void testOkButtonActionListener() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(fontChooserDialog);
        assertEquals(1, okButton.getActionListeners().length);
        
        // Simulate button action
        ActionListener[] actionListeners = okButton.getActionListeners();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(okButton, ActionEvent.ACTION_PERFORMED, "OK"));
        }
        
        assertFalse(fontChooserDialog.isVisible());
    }
    
    // Test case for the Cancel button's ActionListener
    @Test
    public void testCancelButtonActionListener() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(fontChooserDialog);
        assertEquals(1, cancelButton.getActionListeners().length);
        
        // Simulate button action
        ActionListener[] actionListeners = cancelButton.getActionListeners();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(cancelButton, ActionEvent.ACTION_PERFORMED, "cancel"));
        }
        
        assertFalse(fontChooserDialog.isVisible());
    }
    
    // Test case for the Reset button's ActionListener
    @Test
    public void testResetButtonActionListener() {
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(fontChooserDialog);
        assertEquals(1, resetButton.getActionListeners().length);
        
        // Simulate button action
        ActionListener[] actionListeners = resetButton.getActionListeners();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(resetButton, ActionEvent.ACTION_PERFORMED, "reset"));
        }
        
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }
    
    // Test case for the JFontChooser in the constructor
    @Test
    public void testJFontChooserInConstructor() {
        assertEquals(fontChooserDialog.chooserPane, fontChooserDialog.getContentPane().getComponent(0));
    }
    
    // Test case for the button pane in the constructor
    @Test
    public void testButtonPaneInConstructor() {
        JPanel buttonPane = (JPanel)fontChooserDialog.getContentPane().getComponent(1);
        assertEquals(JPanel.class, buttonPane.getClass());
        assertEquals(FlowLayout.class, buttonPane.getLayout().getClass());
        assertEquals(3, buttonPane.getComponentCount());
        assertEquals(JButton.class, buttonPane.getComponent(0).getClass());
        assertEquals(JButton.class, buttonPane.getComponent(1).getClass());
        assertEquals(JButton.class, buttonPane.getComponent(2).getClass());
    }
    
    // Test case for setting the root pane's default button
    @Test
    public void testSetDefaultButtonInConstructor() {
        JButton okButton = (JButton)fontChooserDialog.getContentPane().getComponent(1).getComponent(0);
        JRootPane rootPane = fontChooserDialog.getRootPane();
        assertEquals(okButton, rootPane.getDefaultButton());
    }
    
    // Test case for OK button's ActionListener in the constructor
    @Test
    public void testOkButtonActionListenerInConstructor() {
        JButton okButton = (JButton)fontChooserDialog.getContentPane().getComponent(1).getComponent(0);
        ActionListener[] actionListeners = okButton.getActionListeners();
        assertEquals(2, actionListeners.length);
        
        // Simulate button action
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(okButton, ActionEvent.ACTION_PERFORMED, "OK"));
        }
        
        assertFalse(fontChooserDialog.isVisible());
    }
    
    // Test case for Cancel button's ActionListener in the constructor
    @Test
    public void testCancelButtonActionListenerInConstructor() {
        JButton cancelButton = (JButton)fontChooserDialog.getContentPane().getComponent(1).getComponent(1);
        ActionListener[] actionListeners = cancelButton.getActionListeners();
        assertEquals(2, actionListeners.length);
        
        // Simulate button action
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(cancelButton, ActionEvent.ACTION_PERFORMED, "cancel"));
        }
        
        assertFalse(fontChooserDialog.isVisible());
    }
    
    // Test case for Reset button's ActionListener in the constructor
    @Test
    public void testResetButtonActionListenerInConstructor() {
        JButton resetButton = (JButton)fontChooserDialog.getContentPane().getComponent(1).getComponent(2);
        ActionListener[] actionListeners = resetButton.getActionListeners();
        assertEquals(1, actionListeners.length);
        
        // Simulate button action
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(resetButton, ActionEvent.ACTION_PERFORMED, "reset"));
        }
        
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }
    
    // Test case for the pack() method in the constructor
    @Test
    public void testPackInConstructor() {
        fontChooserDialog.pack();
        assertTrue(fontChooserDialog.getSize().getWidth() > 0);
        assertTrue(fontChooserDialog.getSize().getHeight() > 0);
    }
    
    // Test case for the setLocationRelativeTo() method in the constructor
    @Test
    public void testSetLocationRelativeToInConstructor() {
        fontChooserDialog.setLocationRelativeTo(null);
        assertTrue(fontChooserDialog.getLocation().x >= 0);
        assertTrue(fontChooserDialog.getLocation().y >= 0);
    }
    
}