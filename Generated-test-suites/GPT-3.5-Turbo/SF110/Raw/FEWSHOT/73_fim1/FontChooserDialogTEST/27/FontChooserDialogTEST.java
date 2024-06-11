
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class FontChooserDialogTest {

    private JFontChooser chooserPane;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        chooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(new JLabel(), "Font Chooser Dialog", true, chooserPane,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Perform action on OK button click
                    }
                },
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Perform action on Cancel button click
                    }
                });
    }

    @Test
    public void testSetFont() {
        JLabel fontLabel = (JLabel) fontChooserDialog.getContentPane().getComponent(0);
        JTextField fontTextField = (JTextField) fontLabel.getComponent(1);
        JButton fontButton = (JButton) fontLabel.getComponent(2);

        assertEquals("Choose the Font", fontLabel.getText());
        
        // Test setting the font to a valid value
        fontChooserDialog.setVisible(true);

        String initialFontFamily = chooserPane.getFont().getFamily();

        fontTextField.setText("Arial");
        fontChooserDialog.reset();
        
        assertEquals("Arial", fontTextField.getText());
        assertEquals("Arial", chooserPane.getFont().getFamily());

        // Test setting the font to an invalid value (not supported by the system)
        fontChooserDialog.setVisible(true);

        String invalidFontFamily = "InvalidFontFamily";

        fontTextField.setText(invalidFontFamily);
        fontChooserDialog.reset();
        
        assertEquals(invalidFontFamily, fontTextField.getText());
        assertEquals(initialFontFamily, chooserPane.getFont().getFamily());
    }

    @Test
    public void testOKButtonActionListener() {
        JButton okButton = (JButton) fontChooserDialog.getContentPane().getComponent(1).getComponent(0);

        okButton.doClick();

        // Verify that the dialog is closed
        assertEquals(false, fontChooserDialog.isVisible());
    }

    @Test
    public void testCancelButtonActionListener() {
        JButton cancelButton = (JButton) fontChooserDialog.getContentPane().getComponent(1).getComponent(1);

        cancelButton.doClick();

        // Verify that the dialog is closed
        assertEquals(false, fontChooserDialog.isVisible());
    }
}
