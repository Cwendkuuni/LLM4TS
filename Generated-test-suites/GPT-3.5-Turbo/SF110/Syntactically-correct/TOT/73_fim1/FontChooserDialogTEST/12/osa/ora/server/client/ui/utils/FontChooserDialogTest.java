
import org.junit.Test;
import org.junit.Assert;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        Assert.assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        dialog.setVisible(true);
        Assert.assertNotNull(dialog);
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", false, null, null, null);
        Font initialFont = dialog.chooserPane.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        dialog.chooserPane.setFont(newFont);
        dialog.reset();
        Assert.assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JButton closeButton = new JButton();
        ActionEvent event = new ActionEvent(closeButton, ActionEvent.ACTION_PERFORMED, "Close");
        closer.windowClosing(new WindowEvent(closeButton, WindowEvent.WINDOW_CLOSING));
        Assert.assertTrue(closeButton.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JButton closeButton = new JButton();
        ComponentEvent event = new ComponentEvent(closeButton, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(event);
        Assert.assertTrue(closeButton.isVisible());
    }
}

