package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JButton resetButton = new JButton("Reset");
        JFontChooser chooserPane = new JFontChooser();
        
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Test action performed on OK button
            }
        };
        
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Test action performed on Cancel button
            }
        };
        
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Test Title", true, chooserPane, okListener, cancelListener);
        
        assertTrue(dialog.isModal());
        assertEquals("Test Title", dialog.getTitle());
        assertEquals(okButton.getText(), UIManager.getString("ColorChooser.okText"));
        assertEquals(cancelButton.getText(), UIManager.getString("ColorChooser.cancelText"));
        assertEquals(resetButton.getText(), UIManager.getString("ColorChooser.resetText"));
        assertSame(dialog.getContentPane().getComponent(0), chooserPane);
        assertSame(dialog.getContentPane().getComponent(1), dialog.buttonPane);
        assertSame(dialog.getContentPane().getComponent(0).getParent(), dialog.getContentPane());
        assertSame(dialog.getContentPane().getComponent(1).getParent(), dialog.getContentPane());
        assertEquals(dialog.getContentPane().getLayout().getClass(), BorderLayout.class);
        assertEquals(dialog.buttonPane.getLayout().getClass(), FlowLayout.class);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Test Title", true, new JFontChooser(), null, null);
        
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        assertTrue(dialog.isVisible());
        
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = new JFontChooser();
        chooserPane.setFont(initialFont);
        
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Test Title", true, chooserPane, null, null);
        
        dialog.reset();
        assertSame(initialFont, chooserPane.getFont());
    }
}