import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FontChooserDialogTest {
    
    @Test
    public void testFontChooserDialogConstructor() {
        // Test constructor functionality
        // Mock necessary components
        Component c = mock(Component.class);
        String title = "Test Title";
        boolean modal = false;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        
        // Create FontChooserDialog object
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        
        // Assert objects are initialized correctly
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.chooserPane);
    }
    
    @Test
    public void testFontChooserDialogSetVisible() {
        // Test setVisible method
        FontChooserDialog dialog = mock(FontChooserDialog.class);
        dialog.setVisible(true);
        
        // Verify initialFont is set correctly
        verify(dialog, times(1)).setVisible(true);
        assertNotNull(dialog.initialFont);
    }
    
    @Test
    public void testReset() {
        // Test reset method
        FontChooserDialog dialog = new FontChooserDialog();
        Font initialFont = dialog.chooserPane.getFont();
        
        // Set new font and reset
        dialog.chooserPane.setFont(new Font("Arial", Font.PLAIN, 12));
        dialog.reset();
        
        // Verify font is reset to initialFont
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }
    
    @Test
    public void testCloserWindowClosing() {
        // Test Closer class windowClosing method
        Closer closer = new Closer();
        WindowEvent e = mock(WindowEvent.class);
        Window w = mock(Window.class);
        when(e.getWindow()).thenReturn(w);
        
        // Call windowClosing method
        closer.windowClosing(e);
        
        // Verify window is set to invisible
        verify(w, times(1)).setVisible(false);
    }
    
    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Test DisposeOnClose class componentHidden method
        DisposeOnClose disposeOnClose = new DisposeOnClose();
        ComponentEvent e = mock(ComponentEvent.class);
        Window w = mock(Window.class);
        when(e.getComponent()).thenReturn(w);
        
        // Call componentHidden method
        disposeOnClose.componentHidden(e);
        
        // Verify window is disposed
        verify(w, times(1)).dispose();
    }
}