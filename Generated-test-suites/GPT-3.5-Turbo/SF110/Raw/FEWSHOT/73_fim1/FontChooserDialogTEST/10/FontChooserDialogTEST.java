package osa.ora.server.client.ui.utils;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {
    
    @Test
    public void testConstructor() {
        // Create test objects
        Component c = new JPanel();
        String title = "Font Chooser";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do something
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do something
            }
        };
        
        // Create FontChooserDialog instance
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        // Test properties
        assertEquals(c, dialog.getParent());
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        
        // Test button actions
        Map<String, Action> expectedActionMap = new HashMap<>();
        expectedActionMap.put("OK", dialog.okAction);
        expectedActionMap.put("cancel", dialog.cancelAction);
        
        JButton okButton = (JButton) dialog.getRootPane().getDefaultButton();
        JButton cancelButton = (JButton) dialog.getRootPane().getContentPane().getComponent(1);
        
        assertEquals("OK", okButton.getActionCommand());
        assertEquals("cancel", cancelButton.getActionCommand());
        assertEquals(expectedActionMap.get("OK"), okButton.getActionListeners()[0]);
        assertEquals(expectedActionMap.get("cancel"), cancelButton.getActionListeners()[0]);
    }
    
    @Test
    public void testSetVisible() {
        Component c = new JPanel();
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(c, "Font Chooser", true, chooserPane, null, null);
        
        // Test initial font
        Font initialFont = chooserPane.getFont();
        assertEquals(initialFont, dialog.getInitialFont());
        
        // Test visibility
        dialog.setVisible(true);
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }
    
    @Test
    public void testReset() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Font Chooser", true, chooserPane, null, null);
        
        // Set new font
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(newFont);
        
        // Reset font
        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
    }
    
    @Test
    public void testCloser() {
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);
        
        JFrame frame = (JFrame) windowEvent.getWindow();
        assertEquals(false, frame.isVisible());
    }
    
    @Test
    public void testDisposeOnClose() {
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);
        
        JFrame frame = (JFrame) componentEvent.getComponent();
        assertEquals(true, frame.isDisposed());
    }
    
}