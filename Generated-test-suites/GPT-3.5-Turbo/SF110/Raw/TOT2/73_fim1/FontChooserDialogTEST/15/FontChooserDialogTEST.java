import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        // Create a FontChooserDialog instance
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, new JFontChooser(), null, null);

        // Check if the dialog is not null
        assertTrue(dialog != null);
    }

    @Test
    public void testVisibleMethod() {
        // Create a FontChooserDialog instance
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, new JFontChooser(), null, null);

        // Check if the setVisible method sets the initialFont correctly
        dialog.setVisible(true);
        assertTrue(dialog.initialFont != null);
    }

    @Test
    public void testResetMethod() {
        // Create a FontChooserDialog instance
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, new JFontChooser(), null, null);

        // Set the font to a new Font
        dialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));

        // Reset the font to the initialFont
        dialog.reset();

        // Check if the font is reset correctly
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }
}