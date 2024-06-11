import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;

public class FontChooserDialogTest {

    private FontChooserDialog.FontChooserDialog dialog;

    @Before
    public void setUp() {
        dialog = new FontChooserDialog(new JFrame(), "Font Chooser Dialog", true, new JFontChooser(), null, null);
    }

    @Test
    public void testSetFontAndGetFont() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        dialog.chooserPane.setFont(font);
        assertEquals(font, dialog.chooserPane.getFont());
    }

    @Test
    public void testReset() {
        Font initialFont = dialog.chooserPane.getFont();
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 14));
        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testVisible() {
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testWindowClosing() {
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);
        assertFalse(windowEvent.getWindow().isVisible());
    }

    @Test
    public void testComponentHidden() {
        Window window = new JFrame();
        ComponentEvent componentEvent = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);
        assertFalse(window.isVisible());
    }
}