package osa.ora.server.client.ui.utils;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        Component c = new Component();
        String title = "Font Chooser";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Test OK listener functionality
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Test cancel listener functionality
            }
        };
    
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    
        assertNotNull(dialog);
        assertEquals(dialog.getTitle(), title);
        assertEquals(dialog.isModal(), modal);
        assertEquals(dialog.getContentPane().getComponent(0), chooserPane);
    }
    
    @Test
    public void testSetVisible() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", true, chooserPane, null, null);
    
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
    
        dialog.setVisible(false);
        assertNull(dialog.initialFont);
    }
    
    @Test
    public void testReset() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new Component(), "Font Chooser", true, chooserPane, null, null);
    
        Font initialFont = dialog.chooserPane.getFont();
    
        Font newFont = new Font("Arial", Font.BOLD, 14);
        dialog.chooserPane.setFont(newFont);
    
        dialog.reset();
    
        assertEquals(dialog.chooserPane.getFont(), initialFont);
    }
    
    @Test
    public void testFontChooserDialogWithNullValues() {
        FontChooserDialog dialog = new FontChooserDialog(null, null, true, null, null, null);
    
        assertNotNull(dialog);
    }
    
    @Test
    public void testSetVisibleTrue() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new Component(), "Font Chooser", true, chooserPane, null, null);
    
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
    
        dialog.setVisible(true);
        assertEquals(dialog.initialFont, chooserPane.getFont());
    }
    
    @Test
    public void testSetVisibleFalse() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new Component(), "Font Chooser", true, chooserPane, null, null);
    
        Font initialFont = dialog.chooserPane.getFont();
        dialog.setVisible(false);
    
        assertNull(dialog.initialFont);
        assertEquals(dialog.chooserPane.getFont(), initialFont);
    }
    
    @Test
    public void testResetWithDifferentInitialFonts() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new Component(), "Font Chooser", true, chooserPane, null, null);
    
        Font initialFont = dialog.chooserPane.getFont();
    
        Font newFont = new Font("Arial", Font.BOLD, 14);
        dialog.chooserPane.setFont(newFont);
    
        dialog.reset();
    
        assertEquals(dialog.chooserPane.getFont(), initialFont);
    
        Font anotherFont = new Font("Verdana", Font.PLAIN, 18);
        dialog.chooserPane.setFont(anotherFont);
    
        dialog.reset();
    
        assertEquals(dialog.chooserPane.getFont(), initialFont);
    }
}