package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new TestActionListener();
        ActionListener cancelListener = new TestActionListener();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Title", true, chooserPane, okListener, cancelListener);

        assertEquals("Title", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        assertNotNull(dialog.getContentPane());
    }

    @Test
    public void testSetVisible() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new TestActionListener();
        ActionListener cancelListener = new TestActionListener();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Title", true, chooserPane, okListener, cancelListener);

        assertFalse(dialog.isVisible());

        dialog.setVisible(true);
        assertTrue(dialog.isVisible());

        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new TestActionListener();
        ActionListener cancelListener = new TestActionListener();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Title", true, chooserPane, okListener, cancelListener);

        Font initialFont = dialog.getChooserPane().getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        dialog.getChooserPane().setFont(newFont);

        assertEquals(newFont, dialog.getChooserPane().getFont());

        dialog.reset();
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    private class TestActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // do nothing
        }
    }
}