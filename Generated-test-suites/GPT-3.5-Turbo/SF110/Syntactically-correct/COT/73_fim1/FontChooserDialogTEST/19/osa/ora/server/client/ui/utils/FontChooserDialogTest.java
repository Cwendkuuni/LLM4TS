import static org.junit.Assert.*;
import org.junit.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontChooserDialog;
    private Component component;
    private JFontChooser fontChooser;
    private ActionListener okListener;
    private ActionListener cancelListener;
    
    @Before
    public void setUp() {
        component = new JPanel();
        fontChooser = new JFontChooser();
        okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) { }
        };
        cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) { }
        };
        
        fontChooserDialog = new FontChooserDialog(component, "Title", true, fontChooser, okListener, cancelListener);
    }
    
    @Test
    public void testFontChooserDialog() {
        assertNotNull(fontChooserDialog);
        assertEquals("Title", fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
        assertEquals(fontChooser, fontChooserDialog.getChooserPane());
        assertEquals(okListener, fontChooserDialog.getOkListener());
        assertEquals(cancelListener, fontChooserDialog.getCancelListener());
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
        Font initialFont = fontChooserDialog.getChooserPane().getFont();
        fontChooserDialog.getChooserPane().setFont(new Font("Arial", Font.PLAIN, 12));
        
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
    }
    
    // Additional test cases
    
    // Edge case: Test with null component
    @Test(expected = NullPointerException.class)
    public void testFontChooserDialog_NullComponent() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, fontChooser, okListener, cancelListener);
    }
    
    // Edge case: Set a font and verify if it gets reset after setVisible(true)
    @Test
    public void testSetVisible_FontResetWhenVisible() {
        Font initialFont = fontChooserDialog.getChooserPane().getFont();
        
        fontChooserDialog.getChooserPane().setFont(new Font("Arial", Font.PLAIN, 12));
        fontChooserDialog.setVisible(true);
        
        assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
    }
    
    // Exception handling: Test reset() with null chooserPane
    @Test(expected = NullPointerException.class)
    public void testReset_NullChooserPane() {
        fontChooserDialog.setChooserPane(null);
        fontChooserDialog.reset();
    }
    
}