import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.junit.Before;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action
            }
        };
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", false, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
        assertEquals("Test Dialog", fontChooserDialog.getTitle());
        assertFalse(fontChooserDialog.isModal());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        WindowListener closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        // Verify the window is not visible
        assertFalse(windowEvent.getWindow().isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        WindowListener disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = new ComponentEvent(null, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        // Verify the window is disposed
        assertTrue(componentEvent.getComponent().isDisposed());
    }
}