import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", false, null, null, null);
        assertNotNull(fontChooserDialog);
        assertEquals("Title", fontChooserDialog.getTitle());
        assertFalse(fontChooserDialog.isModal());
        assertNull(fontChooserDialog.getChooserPane());
        assertNull(fontChooserDialog.getInitialFont());
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, null, false, null, null, null);
        assertFalse(fontChooserDialog.isVisible());

        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());

        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, null, false, null, null, null);
        fontChooserDialog.reset();

        // Verify that the chooser pane font is reset to initial font
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
    }
}