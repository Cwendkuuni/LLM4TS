
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class FontChooserDialogTest {
    @Test
    public void testConstructor() {
        // Create a Component object for testing
        Component c = new Component() {};

        // Create an ActionListener object for testing
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

        // Create a JFontChooser object for testing
        JFontChooser chooserPane = new JFontChooser();

        // Create an instance of FontChooserDialog for testing
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);

        // Test the constructor
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testReset() {
        // Create a JFontChooser object for testing
        JFontChooser chooserPane = new JFontChooser();

        // Create an instance of FontChooserDialog for testing
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, null, true, chooserPane, null, null);

        // Set the font of the chooserPane
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        fontChooserDialog.chooserPane.setFont(initialFont);

        // Reset the font of the chooserPane
        fontChooserDialog.reset();

        // Test the reset method
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
}
