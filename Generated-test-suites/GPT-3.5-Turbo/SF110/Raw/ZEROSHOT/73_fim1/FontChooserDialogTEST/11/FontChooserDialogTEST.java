import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.getFont();
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window window = new Frame();
        WindowEvent windowEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(window.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JFrame frame = new JFrame();
        ComponentEvent componentEvent = new ComponentEvent(frame, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(frame.isDisposed());
    }
}