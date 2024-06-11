package osa.ora.server.client.ui.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", false, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", false, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog);
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test", false, null, null, null);
        dialog.reset();
        assertNotNull(dialog);
    }
    
    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(event);
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent event = new ComponentEvent(new JFrame(), ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(event);
    }
}