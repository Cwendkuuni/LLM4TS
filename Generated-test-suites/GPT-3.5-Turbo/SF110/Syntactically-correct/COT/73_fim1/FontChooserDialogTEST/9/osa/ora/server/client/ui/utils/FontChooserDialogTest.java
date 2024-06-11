package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        Component c = new Component();
        String title = "Font Chooser";
        boolean modal = false;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        };
        
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
    }

    @Test
    public void testSetVisible() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        dialog.getChooserPane().setFont(initialFont);
        dialog.reset();
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testSetVisible_WithValidInitialFont() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        dialog.setVisible(true);
        assertEquals(initialFont, dialog.getChooserPane().getFont());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testSetVisible_WithInvalidInitialFont() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        chooserPane.setFont(null);
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        dialog.setVisible(true);
    }

    @Test
    public void testReset_WithNullInitialFont() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        Font initialFont = null;
        chooserPane.setFont(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        dialog.reset();
        assertNull(dialog.getChooserPane().getFont());
    }

    @Test
    public void testSetVisible_CancelAction() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.getChooserPane().setFont(null);
            }
        };
        dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, cancelListener);
        
        dialog.setVisible(true);
        assertNull(dialog.getChooserPane().getFont());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testSetVisible_OkAction() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        chooserPane.setFont(null);
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.getChooserPane().setFont(new Font("Arial", Font.PLAIN, 12));
            }
        };
        dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, okListener, null);
        
        dialog.setVisible(true);
        assertNotNull(dialog.getChooserPane().getFont());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testSetVisible_CancelAction() {
        Component c = new Component();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, null);
        
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        };
        dialog = new FontChooserDialog(c, "Font Chooser", false, chooserPane, null, cancelListener);
        
        dialog.setVisible(true);
        assertFalse(dialog.isVisible());
    }

}