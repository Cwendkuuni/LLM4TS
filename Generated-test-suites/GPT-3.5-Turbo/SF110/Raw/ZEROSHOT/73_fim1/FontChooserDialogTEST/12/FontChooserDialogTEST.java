package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        Component c = new JPanel();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform OK action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform Cancel action
            }
        };
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        // Test when visible is false
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
        
        // Test when visible is true
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testReset() {
        Component c = new JPanel();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform OK action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform Cancel action
            }
        };
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        // Set initial font
        Font initialFont = dialog.getChooserPane().getFont();
        dialog.reset();
        
        // Test if font is reset
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    // Add test methods for other methods in FontChooserDialog class

}