import org.junit.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog();
    }

    @Test
    public void testFontChooserDialogInitialFont() {
        Font initialFont = fontChooserDialog.chooserPane.getFont();
        Assert.assertNotNull(initialFont);
    }

    @Test
    public void testFontChooserDialogVisibility() {
        fontChooserDialog.setVisible(true);
        Assert.assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testFontChooserDialogReset() {
        fontChooserDialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));
        fontChooserDialog.reset();
        Font resetFont = fontChooserDialog.chooserPane.getFont();
        Assert.assertEquals(fontChooserDialog.initialFont, resetFont);
    }

    @Test
    public void testFontChooserDialogCloseOperation() {
        WindowAdapter closer = fontChooserDialog.getContentPane().getComponentCloser();
        Assert.assertTrue(closer instanceof FontChooserDialog.Closer);
        WindowEvent windowEvent = new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        Assert.assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testFontChooserDialogDisposeOnClose() {
        ComponentAdapter disposeOnClose = fontChooserDialog.getContentPane().getComponentDisposeOnClose();
        Assert.assertTrue(disposeOnClose instanceof FontChooserDialog.DisposeOnClose);
        ComponentEvent componentEvent = new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        Assert.assertTrue(fontChooserDialog.isDisposed());
    }
}