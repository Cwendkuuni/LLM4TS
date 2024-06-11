import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(new JPanel(), "Test Font Chooser", true, new JFontChooser(), null, null);
    }

    @Test
    public void testFontChooserDialog() {
        assertEquals("Test Font Chooser", fontChooserDialog.getTitle());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.chooserPane.getFont(), fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        fontChooserDialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 16));
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window window = new Window(new JFrame());
        window.addWindowListener(closer);
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        assertEquals(false, window.isVisible());
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JFrame frame = new JFrame();
        frame.addComponentListener(disposeOnClose);
        frame.dispatchEvent(new ComponentEvent(frame, ComponentEvent.COMPONENT_HIDDEN));
        assertEquals(true, frame.isShowing()); // Frame should be disposed
    }

    // Add more test cases here

}