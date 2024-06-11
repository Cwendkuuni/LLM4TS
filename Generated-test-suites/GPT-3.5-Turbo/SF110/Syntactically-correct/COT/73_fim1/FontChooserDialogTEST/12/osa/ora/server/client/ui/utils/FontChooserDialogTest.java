import org.junit.Test;
import org.junit.Assert.*;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
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
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        assertEquals(chooserPane.getFont(), dialog.getInitialFont());
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testReset() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        Font font = chooserPane.getFont();
        dialog.reset();
        assertEquals(font, chooserPane.getFont());
    }

    @Test
    public void testConstructorWithNullValues() {
        Component c = new Component();
        String title = null;
        boolean modal = true;
        JFontChooser chooserPane = null;
        ActionListener okListener = null;
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
        assertNull(dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertNull(dialog.getChooserPane());
    }

    @Test
    public void testConstructorWithNullOkListener() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = null;
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
    }

    @Test
    public void testResetWithNullInitialFont() {
        Component c = new Component();
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // test action
            }
        };

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        chooserPane.setFont(null);
        dialog.reset();
        assertNull(chooserPane.getFont());
    }

}