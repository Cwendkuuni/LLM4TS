import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.awt.Font;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    private JFontChooser jFontChooser;

    @Before
    public void setUp() {
        jFontChooser = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, jFontChooser, null, null);
    }

    @After
    public void tearDown() {
        fontChooserDialog = null;
        jFontChooser = null;
    }

    @Test
    public void testSetVisible() {
        assertFalse(fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.getJFontChooser().getFont();
        Font modifiedFont = new Font("Arial", Font.PLAIN, 12);
        
        // Set a modified font
        fontChooserDialog.getJFontChooser().setFont(modifiedFont);
        assertEquals(modifiedFont, fontChooserDialog.getJFontChooser().getFont());
        
        // Reset font
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getJFontChooser().getFont());
    }

    @Test
    public void testWindowClosing() {
        fontChooserDialog.addWindowListener(new FontChooserDialog.Closer());
        fontChooserDialog.windowClosing(null);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testComponentHidden() {
        fontChooserDialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
        fontChooserDialog.componentHidden(null);
        assertTrue(fontChooserDialog.isDisposed());
    }

    @Test
    public void testOkButtonActionPerformed() {
        // Test action listener for OK button to close the dialog
        fontChooserDialog.getJFontChooser().setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Simulate OK button click
        fontChooserDialog.getJButton("OK").getActionListeners()[0].actionPerformed(new ActionEvent(new Object(), 0, "OK"));
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testCancelButtonActionPerformed() {
        // Test action listener for Cancel button to close the dialog
        fontChooserDialog.getJFontChooser().setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Simulate Cancel button click
        fontChooserDialog.getJButton("Cancel").getActionListeners()[0].actionPerformed(new ActionEvent(new Object(), 0, "cancel"));
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testResetButtonActionPerformed() {
        // Test action listener for Reset button to reset the font
        fontChooserDialog.getJFontChooser().setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Simulate Reset button click
        fontChooserDialog.getJButton("Reset").getActionListeners()[0].actionPerformed(new ActionEvent(new Object(), 0, "reset"));
        assertEquals(jFontChooser.getFont(), fontChooserDialog.getInitialFont());
    }
}