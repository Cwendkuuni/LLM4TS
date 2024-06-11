import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", false, new JFontChooser(),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // okListener action performed
                    }
                },
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // cancelListener action performed
                    }
                });
    }

    @Test
    public void testSetVisible() {
        assertFalse(fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.getFont();
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
}