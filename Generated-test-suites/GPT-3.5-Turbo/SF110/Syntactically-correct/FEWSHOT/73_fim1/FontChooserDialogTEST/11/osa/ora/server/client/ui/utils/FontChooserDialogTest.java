
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontDialog;
    private JFontChooser fontChooser;
    private ActionListener okListener;
    private ActionListener cancelListener;
    
    @Before
    public void setUp() {
        JButton button = new JButton();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fontChooser = new JFontChooser();
        fontDialog = new FontChooserDialog(frame, "Font Chooser Dialog", false, fontChooser, okListener, cancelListener);
    }
    
    @Test
    public void testSetVisible() {
        fontDialog.setVisible(true);
        assertTrue(fontDialog.isVisible());
        fontDialog.setVisible(false);
        assertFalse(fontDialog.isVisible());
    }
    
    @Test
    public void testReset() {
        fontDialog.reset();
        assertEquals(fontDialog.initialFont, fontDialog.chooserPane.getFont());
    }
    
    @Test
    public void testFontChooserDialog() {
        assertEquals("Font Chooser Dialog", fontDialog.getTitle());
        assertFalse(fontDialog.isModal());
        assertEquals(JOptionPane.getRootFrame(), fontDialog.getOwner());
    }
    
    @Test
    public void testActionPerformed() {
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        JButton resetButton = new JButton();
        okButton.setActionCommand("OK");
        cancelButton.setActionCommand("cancel");
        resetButton.setActionCommand("reset");
        
        ActionListener okAction = null;
        ActionListener cancelAction = null;
        ActionListener resetAction = null;
        
        fontDialog.okButton.addActionListener(okAction);
        fontDialog.cancelButton.addActionListener(cancelAction);
        fontDialog.resetButton.addActionListener(resetAction);
        
        okButton.doClick();
        assertTrue(fontDialog.isVisible());
        cancelButton.doClick();
        assertFalse(fontDialog.isVisible());
        resetButton.doClick();
        assertEquals(fontDialog.initialFont, fontDialog.chooserPane.getFont());
    }
    
    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JRootPane rootPane = new JRootPane();
        dialog.setRootPane(rootPane);
        JRootPane rootPane1 = new JRootPane();
        closer.windowClosing(new ActionEvent(rootPane1, 1, ""));
        
        assertFalse(dialog.isVisible());
    }
    
    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose dispose = new FontChooserDialog.DisposeOnClose();
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dispose.componentHidden(null);
        
        assertFalse(dialog.isVisible());
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        dispose.componentHidden(null);
        
        assertFalse(dialog.isVisible());
    }
}
