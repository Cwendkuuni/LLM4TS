import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", false, new JFontChooser(),
                                                  null, null);
    }

    @After
    public void tearDown() {
        fontChooserDialog = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Dialog", fontChooserDialog.getTitle());
        assertFalse(fontChooserDialog.isModal());
    }

    @Test
    public void testSetVisible() {
        assertFalse(fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        JFontChooser chooserPane = fontChooserDialog.getChooserPane();
        Font initialFont = fontChooserDialog.getInitialFont();
        Font newFont = new Font("Arial", Font.BOLD, 14);
        chooserPane.setFont(newFont);
        fontChooserDialog.reset();
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        Window window = new Window(new Frame());
        ActionListener closer = new FontChooserDialog.Closer();
        ActionEvent event = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "Window Closing");
        closer.actionPerformed(event);
        assertFalse(window.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        Window window = new Window(new Frame());
        ComponentAdapter disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent event = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(event);
        assertFalse(window.isDisplayable());
    }

}