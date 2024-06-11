import static org.junit.Assert.*;
import org.junit.Test;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        // Test the FontChooserDialog constructor
        // Create a FontChooserDialog object
        // Check if the FontChooserDialog object is not null
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testVisibleMethod() {
        // Test the setVisible method
        // Create a FontChooserDialog object
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        // Check if the initial font is set correctly when visible is true
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
    }

    @Test
    public void testResetMethod() {
        // Test the reset method
        // Create a FontChooserDialog object
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", true, null, null, null);
        // Set a font and then reset it
        dialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));
        dialog.reset();
        // Check if the font is reset to initial font
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
}