import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        JFontChooser fontChooser = new JFontChooser();
        JFrame frame = new JFrame();
        fontChooserDialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, fontChooser, null, null);
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
    public void testWindowClosing() {
        WindowEvent windowEvent = new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING);
        fontChooserDialog.Closer.windowClosing(windowEvent);
        assertFalse(fontChooserDialog.isVisible());
    }
    
    @Test
    public void testComponentHidden() {
        ComponentEvent componentEvent = new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN);
        fontChooserDialog.DisposeOnClose.componentHidden(componentEvent);
        assertFalse(fontChooserDialog.isVisible());
        assertTrue(fontChooserDialog.isDisposed());
    }

}