import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = new JFontChooser();
        
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Font Chooser", true, chooserPane, null, null);
        
        assertNotNull(dialog);
        assertEquals("Font Chooser", dialog.getTitle());
        assertEquals(true, dialog.isModal());
        assertEquals(chooserPane, dialog.chooserPane);
    }

    @Test
    public void testVisible() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Font Chooser", true, chooserPane, null, null);

        dialog.setVisible(true);
        
        assertEquals(initialFont, dialog.initialFont);
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new JPanel(), "Font Chooser", true, chooserPane, null, null);

        dialog.reset();
        
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        
        closer.windowClosing(windowEvent);
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        
        disposeOnClose.componentHidden(componentEvent);
    }

}