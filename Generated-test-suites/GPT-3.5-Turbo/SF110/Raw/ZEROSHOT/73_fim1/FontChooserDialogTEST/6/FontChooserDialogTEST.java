import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {
    
    @Test
    public void testSetVisible() {
        // Create test objects
        FontChooserDialog dialog = new FontChooserDialog(new Component() {}, "", true, null, null, null);
        boolean visible = true;
        
        // Set visible
        dialog.setVisible(visible);
        
        // Check if the initial font is set correctly
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }
    
    @Test
    public void testReset() {
        // Create test objects
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        FontChooserDialog dialog = new FontChooserDialog(new Component() {}, "", true, null, null, null);
        dialog.chooserPane.setFont(initialFont);
        
        // Reset the font
        dialog.reset();
        
        // Check if the font is reset to the initial font
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }
    
    // Add tests for other methods in FontChooserDialog
    
}