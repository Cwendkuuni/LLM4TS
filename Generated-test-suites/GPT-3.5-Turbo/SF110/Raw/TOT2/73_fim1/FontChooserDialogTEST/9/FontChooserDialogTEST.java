import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import java.awt.*;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    private Font initialFont;
    private JFontChooser chooserPane;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        initialFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane = Mockito.mock(JFontChooser.class);
        fontChooserDialog = new FontChooserDialog(Mockito.mock(Component.class), "Test Font Chooser", true, chooserPane, null, null);
    }

    @Test
    public void testResetMethod() {
        // Given initial font
        Font expectedFont = new Font("Times New Roman", Font.BOLD, 14);
        Mockito.when(chooserPane.getFont()).thenReturn(expectedFont);

        // Reset font
        fontChooserDialog.reset();
        
        // Assert that font is reset to initial font
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testVisibility() {
        // Set visible true
        fontChooserDialog.setVisible(true);

        // Assert initial font is set correctly
        assertEquals(initialFont, fontChooserDialog.initialFont);

        // Set visible false
        fontChooserDialog.setVisible(false);

        // Assert that visible is set to false
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window window = Mockito.mock(Window.class);
        WindowEvent windowEvent = Mockito.mock(WindowEvent.class);
        Mockito.when(windowEvent.getWindow()).thenReturn(window);

        // Close the window
        closer.windowClosing(windowEvent);

        // Assert that window is not visible
        Mockito.verify(window, Mockito.times(1)).setVisible(false);
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window window = Mockito.mock(Window.class);
        ComponentEvent componentEvent = Mockito.mock(ComponentEvent.class);
        Mockito.when(componentEvent.getComponent()).thenReturn(window);

        // Hide the window
        disposeOnClose.componentHidden(componentEvent);

        // Assert that window is disposed
        Mockito.verify(window, Mockito.times(1)).dispose();
    }
}