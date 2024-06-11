import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        // Test the FontChooserDialog constructor
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);

        assertNotNull(fontChooserDialog);
        assertEquals("Test Dialog", fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
    }

    @Test
    public void testSetVisible() {
        // Test the setVisible method
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);

        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.initialFont);

        fontChooserDialog.setVisible(false);
        assertNull(fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        // Test the reset method
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);

        Font initialFont = fontChooserDialog.chooserPane.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.chooserPane.setFont(newFont);

        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
}