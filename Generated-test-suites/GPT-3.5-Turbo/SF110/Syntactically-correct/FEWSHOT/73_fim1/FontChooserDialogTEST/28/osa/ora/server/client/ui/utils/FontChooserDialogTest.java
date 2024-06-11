package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser(); // create a dummy JFontChooser
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };
        fontChooserDialog = new FontChooserDialog(null, "Test", false, chooserPane, okListener, cancelListener);
    }
    
    @Test
    public void testSetAndGetVisible() {
        // test set visible
        fontChooserDialog.setVisible(true);
        assertEquals(true, fontChooserDialog.isVisible());
        
        // test set invisible
        fontChooserDialog.setVisible(false);
        assertEquals(false, fontChooserDialog.isVisible());
    }
    
    @Test
    public void testReset() {
        // set up initial font
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.chooserPane.setFont(initialFont);
        
        // reset to initial font
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
        
        // reset to different font
        Font newFont = new Font("Times New Roman", Font.BOLD, 14);
        fontChooserDialog.chooserPane.setFont(newFont);
        assertNotEquals(initialFont, fontChooserDialog.chooserPane.getFont());
        
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
    
    @Test
    public void testSetAndGetContentPane() {
        Container contentPane = new JPanel();
        fontChooserDialog.setContentPane(contentPane);
        assertEquals(contentPane, fontChooserDialog.getContentPane());
    }
    
    @Test
    public void testSetAndGetLocationRelativeTo() {
        Component component = new JPanel();
        fontChooserDialog.setLocationRelativeTo(component);
        assertEquals(component, fontChooserDialog.getLocationRelativeTo());
    }
    
    @Test
    public void testSetAndGetInitialFont() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.setInitialFont(initialFont);
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
    }
    
    @Test
    public void testSetAndGetChooserPane() {
        JFontChooser chooserPane = new JFontChooser();
        fontChooserDialog.setChooserPane(chooserPane);
        assertEquals(chooserPane, fontChooserDialog.getChooserPane());
    }
    
    // Additional tests can be added to cover other methods and scenarios
    
}