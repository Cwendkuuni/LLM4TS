import org.junit.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Font Chooser", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        
        assertTrue(dialog.isVisible());
        assertNotNull(dialog);
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = new JFontChooser();
        chooserPane.setFont(initialFont);
        
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Font Chooser", true, chooserPane, null, null);
        dialog.reset();
        
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        
        closer.windowClosing(windowEvent);
        
        assertFalse(windowEvent.getWindow().isVisible());
    }
  
    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        
        disposeOnClose.componentHidden(componentEvent);
        
        assertTrue(componentEvent.getComponent().isVisible());
        assertNull(componentEvent.getComponent());
    }
}