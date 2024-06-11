import org.junit.*;
import static org.junit.Assert.*;
import java.awt.event.*;
import javax.swing.*;

public class FontChooserDialogTest {

    private FontChooserDialog dialog;
    
    @Before
    public void setUp() {
        dialog = new FontChooserDialog(null, "Test Dialog", true, new JFontChooser(), null, null);
    }
    
    @After
    public void tearDown() {
        dialog = null;
    }
    
    // Test for the setVisible() method
    
    @Test
    public void testSetVisible_InitialFontNotNull_VisibleIsTrue() {
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testSetVisible_InitialFontNull_VisibleIsTrue() {
        dialog.initialFont = null;
        dialog.setVisible(true);
        assertNull(dialog.initialFont);
        assertTrue(dialog.isVisible());
    }
    
    @Test
    public void testSetVisible_VisibleIsFalse() {
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }
    
    // Test for the reset() method
    
    @Test
    public void testReset_InitialFontNotNull_ChooserFontReset() {
        dialog.reset();
        assertNotNull(dialog.chooserPane.getFont());
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
    
    @Test
    public void testReset_InitialFontNull_ChooserFontReset() {
        dialog.chooserPane.setFont(null);
        dialog.reset();
        assertNull(dialog.chooserPane.getFont());
        assertNull(dialog.initialFont);
    }
    
    // Test for the Closer class
    
    @Test
    public void testCloser_WindowClosing_WindowIsNotVisible() {
        WindowEvent event = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(event);
        assertFalse(dialog.isVisible());
    }
    
    // Test for the DisposeOnClose class
    
    @Test
    public void testDisposeOnClose_ComponentHidden_WindowIsDisposed() {
        ComponentEvent event = new ComponentEvent(dialog, ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(event);
        assertFalse(dialog.isVisible());
        assertNull(dialog.getParent());
    }
}