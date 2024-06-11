package osa.ora.server.client.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        // Test constructor with null values
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, null, null);
        // Verify if the dialog is not null
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, null, null);

        // Verify if the dialog is not visible
        assertFalse(dialog.isVisible());

        // Set the dialog visible
        dialog.setVisible(true);

        // Verify if the dialog is visible after calling setVisible(true)
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testReset() {
        // Create a dialog and set the font to a custom font
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, null, null);
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));

        // Reset the font using the reset() method
        dialog.reset();

        // Verify if the font is reset to the initial font
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        WindowEvent windowEvent = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);

        // Verify if the window is set to invisible
        assertFalse(windowEvent.getWindow().isVisible());
    }

    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        ComponentEvent componentEvent = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);

        // Verify if the window is disposed
        assertTrue(componentEvent.getComponent().isDisposed());
    }
}