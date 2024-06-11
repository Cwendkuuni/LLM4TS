import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {
    
    @Test
    public void testFontChooserDialogConstructor() {
        // Test the constructor by creating a FontChooserDialog object
        // and check if it is not null
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        assertNotNull(dialog);
    }
    
    @Test
    public void testFontChooserDialogReset() {
        // Test the reset method by setting a font, resetting it, and checking if it is the initial font
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testFontChooserDialogVisible() {
        // Test the setVisible method by checking if the initial font is set correctly
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
        dialog.setVisible(true);
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testCloserWindowClosing() {
        // Test the windowClosing method of Closer class by checking if the window is set to invisible
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
        closer.windowClosing(new WindowEvent(null, WindowEvent.WINDOW_CLOSING));
        assertFalse(null.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Test the componentHidden method of DisposeOnClose class by checking if the window is disposed
        FontChooserDialog.DisposeOnClose dispose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent e = new ComponentEvent(null, ComponentEvent.COMPONENT_HIDDEN);
        dispose.componentHidden(e);
        assertTrue(null.isDisposed());
    }
}