package osa.ora.server.client.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class FontChooserDialogTest {
    
    @Test
    public void testConstructor() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };
        
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(c, dialog.getOwner());
        assertEquals(chooserPane, dialog.getChooserPane());
    }
    
    @Test
    public void testSetVisible() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, null, null);
        
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        
    }
    
    @Test
    public void testReset() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, null, null);
        
        Font initialFont = chooserPane.getFont();
        
        dialog.reset();
        
        assertEquals(initialFont, chooserPane.getFont());
    }
}