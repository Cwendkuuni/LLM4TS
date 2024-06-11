import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
    }

    @Test
    public void testFontChooserDialog() {
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(true, fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        fontChooserDialog.chooserPane.setFont(initialFont);
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialog_NullComponent() {
        new FontChooserDialog(null, "Test Dialog", false, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialog_NullTitle() {
        new FontChooserDialog(null, null, false, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialog_NullFontChooser() {
        new FontChooserDialog(null, "Test Dialog", false, null, null, null);
    }
    
    @Test
    public void testFontChooserDialog_NullOKListener() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
        assertNotNull(dialog);
    }
    
    @Test
    public void testFontChooserDialog_NullCancelListener() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible_MultipleTimes() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(true);
        assertEquals(true, fontChooserDialog.isVisible());
    }

    @Test
    public void testSetVisible_Modal() {
        FontChooserDialog modalDialog = new FontChooserDialog(null, "Test Modal Dialog", true, null, null, null);
        modalDialog.setVisible(true);
        assertEquals(true, modalDialog.isVisible());
    }

    @Test
    public void testSetVisible_NotModal() {
        FontChooserDialog notModalDialog = new FontChooserDialog(null, "Test Not Modal Dialog", false, null, null, null);
        notModalDialog.setVisible(true);
        assertEquals(true, notModalDialog.isVisible());
    }
}