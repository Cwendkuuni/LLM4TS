import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "", false, null, null, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible_true() {
        fontChooserDialog.setVisible(true);
        assertNotEquals(null, fontChooserDialog.initialFont);
    }

    @Test
    public void testSetVisible_false() {
        fontChooserDialog.setVisible(false);
        assertEquals(null, fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.initialFont;
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
}