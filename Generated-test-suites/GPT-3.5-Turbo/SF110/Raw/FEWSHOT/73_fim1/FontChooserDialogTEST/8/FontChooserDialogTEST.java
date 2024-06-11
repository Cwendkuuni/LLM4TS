package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FontChooserDialogTest {
    
    @Test
    public void testConstructor() {
        // Create sample components required for the constructor
        Component component = new Component();
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

        // Create an instance of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        // Assertion: Verify that the created dialog is not null
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        // Create sample components required for the method
        Component component = new Component();
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

        // Create an instance of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        // Set the visibility of the dialog to true
        dialog.setVisible(true);

        // Assertion: Verify that after setting the visibility to true, the initialFont field is not null
        assertNotNull(dialog.initialFont);
    }

    @Test
    public void testReset() {
        // Create sample components required for the method
        Component component = new Component();
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

        // Create an instance of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        // Reset the chooser pane font
        dialog.reset();

        // Assertion: Verify that after resetting the font, the font should be equal to the initial font
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
}