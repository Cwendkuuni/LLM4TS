package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {
    private Font initialFont;
    private JFontChooser chooserPane;

    @Test
    public void testConstructor() {
        Frame frame = new Frame();
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

        FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, okListener, cancelListener);

        assertEquals("Test Dialog", dialog.getTitle());
        assertEquals(true, dialog.isModal());

        dialog.setVisible(true);

        assertEquals(false, dialog.isVisible());
    }

    @Test
    public void testReset() {
        Frame frame = new Frame();
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

        FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, okListener, cancelListener);

        Font font = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(font);

        dialog.reset();

        assertEquals(initialFont, chooserPane.getFont());
    }
}