
package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(
                new JPanel(),
                "Font Chooser",
                true,
                chooserPane,
                null,
                null
        );
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        Font initialFont = fontChooserDialog.getChooserPane().getFont();
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        Font initialFont = fontChooserDialog.getInitialFont();
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        JButton resetButton = findResetButton(fontChooserDialog.getContentPane());
        resetButton.doClick();
        assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
    }

    private JButton findResetButton(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                String buttonText = ((JButton) component).getText();
                if (buttonText.equals(UIManager.getString("ColorChooser.resetText"))) {
                    return (JButton) component;
                }
            } else if (component instanceof Container) {
                JButton resetButton = findResetButton((Container) component);
                if (resetButton != null) {
                    return resetButton;
                }
            }
        }
        return null;
    }
}
