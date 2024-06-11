
import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        // Test case for FontChooserDialog constructor
        // Create a FontChooserDialog
        // Verify that the dialog is created successfully and all necessary components are added

        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        assertNotNull(dialog);
        assertNotNull(dialog.getContentPane().getComponents());
    }

    @Test
    public void testSetVisible() {
        // Test case for setVisible method
        // Create a FontChooserDialog
        // Set it to visible
        // Verify that the initialFont is set correctly

        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
    }

    @Test
    public void testReset() {
        // Test case for reset method
        // Create a FontChooserDialog
        // Set the font to a different value
        // Call reset method
        // Verify that the font is reset to its initial value

        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
}
