import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(null, "Test Title", false, chooserPane, null, null);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new Frame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent); // No exception should be thrown
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new Frame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent); // No exception should be thrown
    }
}