import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, null, false, null, null, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
        assertNotNull(fontChooserDialog.getInitialFont());
        assertNotNull(fontChooserDialog.getChooserPane());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.getInitialFont());
    }

    @Test
    public void testReset() {
        Font customFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.getChooserPane().setFont(customFont);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
    }

    @Test
    public void testNullParametersConstructor() {
        FontChooserDialog nullParametersDialog = new FontChooserDialog(null, null, false, null, null, null);
        assertNotNull(nullParametersDialog);
        assertNotNull(nullParametersDialog.getInitialFont());
        assertNotNull(nullParametersDialog.getChooserPane());
    }

    @Test
    public void testNullChooserPaneConstructor() {
        FontChooserDialog nullChooserPaneDialog = new FontChooserDialog(null, null, false, null, null, null);
        assertNotNull(nullChooserPaneDialog);
        assertNotNull(nullChooserPaneDialog.getInitialFont());
    }

    @Test
    public void testAlternateSetVisible() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(false);
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.getInitialFont());
    }

    @Test
    public void testResetWithoutSetVisible() {
        Font customFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.getChooserPane().setFont(customFont);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
    }

}