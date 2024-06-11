import org.junit.Test;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        // Initialize necessary parameters for the constructor
        Component c = null; // replace with an actual component
        String title = "Test Title";
        boolean modal = true;
        JFontChooser chooserPane = null; // replace with an actual JFontChooser
        ActionListener okListener = null; // replace with an actual ActionListener
        ActionListener cancelListener = null; // replace with an actual ActionListener

        // Create an instance of FontChooserDialog
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Verify that the dialog is not null
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        // Initialize necessary parameters for the method
        FontChooserDialog dialog = new FontChooserDialog();
        boolean visible = true;

        // Set the visibility of the dialog
        dialog.setVisible(visible);

        // Verify that the visibility of the dialog is set correctly
        assertEquals(visible, dialog.isVisible());
    }

    @Test
    public void testReset() {
        // Initialize necessary parameters for the method
        FontChooserDialog dialog = new FontChooserDialog();

        // Reset the font of the dialog
        dialog.reset();

        // Verify that the font of the dialog is reset to the initial font
        assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        // Initialize necessary parameters for the method
        WindowEvent e = new WindowEvent(null, WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        // Call the windowClosing method
        closer.windowClosing(e);

        // Verify that the window is set to invisible
        assertFalse(e.getWindow().isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Initialize necessary parameters for the method
        ComponentEvent e = new ComponentEvent(null, ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        // Call the componentHidden method
        disposeOnClose.componentHidden(e);

        // Verify that the window is disposed
        assertTrue(((Window) e.getComponent()).isDisposed());
    }
}