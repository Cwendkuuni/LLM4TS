package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

    private Font initialFont;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        initialFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane = new JFontChooser();
        okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // some implementation
            }
        };
        cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // some implementation
            }
        };
        fontChooserDialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testConstructor() {
        assertEquals(null, fontChooserDialog.getModalityType());
        assertEquals("Font Chooser", fontChooserDialog.getTitle());
        assertEquals(chooserPane, fontChooserDialog.getChooserPane());
        assertEquals(okListener, fontChooserDialog.getOkListener());
        assertEquals(cancelListener, fontChooserDialog.getCancelListener());
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
        fontChooserDialog.setVisible(false);
        assertNotEquals(initialFont, fontChooserDialog.getInitialFont());
    }

    @Test
    public void testReset() {
        Font newFont = new Font("Times New Roman", Font.BOLD, 16);
        chooserPane.setFont(newFont);
        fontChooserDialog.reset();
        assertEquals(initialFont, chooserPane.getFont());
    }
}