package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    FontChooserDialog dialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        };
        dialog = new FontChooserDialog(new JPanel(), "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testSetVisible() {
        dialog.setVisible(true);
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
        dialog.setVisible(false);
    }

    @Test
    public void testReset() {
        Font initialFont = dialog.initialFont;
        dialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));
        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }
}