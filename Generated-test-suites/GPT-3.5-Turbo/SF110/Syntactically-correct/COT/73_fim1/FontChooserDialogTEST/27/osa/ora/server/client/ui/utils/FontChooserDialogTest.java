import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        Component c = new JLabel();
        String title = "Font Chooser";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ok button action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancel button action
            }
        };
        
        fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
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
        
        // Test when initialFont is null
        fontChooserDialog.setInitialFont(null);
        fontChooserDialog.reset();
        assertNull(fontChooserDialog.getChooserPane().getFont());
    }
}