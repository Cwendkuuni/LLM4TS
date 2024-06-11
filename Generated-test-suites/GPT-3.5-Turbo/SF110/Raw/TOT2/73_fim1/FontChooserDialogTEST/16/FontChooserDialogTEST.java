import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import javax.swing.JButton;
import javax.swing.JFrame;

public class FontChooserDialogTest {
    
    FontChooserDialog fontChooserDialog;
    Font initialFont;
    JFontChooser chooserPane;
    
    @Before
    public void setUp() {
        chooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(new JFrame(), "Test FontChooserDialog", true, chooserPane, null, null);
    }
    
    @Test
    public void testVisibility() {
        assertFalse(fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }
    
    @Test
    public void testReset() {
        fontChooserDialog.setVisible(true);
        initialFont = chooserPane.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(newFont);
        fontChooserDialog.reset();
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        // Test closing window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(closer);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        assertFalse(frame.isVisible());
    }
    
    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        // Test component hidden
        JFrame frame = new JFrame();
        frame.addComponentListener(disposeOnClose);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSED));
        assertFalse(frame.isDisplayable());
    }
    
}