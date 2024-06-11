import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    private Component testComponent;
    private JFontChooser testFontChooser;
    private ActionListener testOkListener;
    private ActionListener testCancelListener;

    @Before
    public void setUp() {
        testComponent = new JPanel();
        testFontChooser = new JFontChooser();
        testOkListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OK button clicked");
            }
        };
        testCancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel button clicked");
            }
        };
    }

    @Test
    public void testFontChooserDialogConstructor_ValidInput_ShouldCreateInstance() {
        FontChooserDialog dialog = new FontChooserDialog(testComponent, "Test Dialog", true, testFontChooser, testOkListener, testCancelListener);
        
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(testComponent, dialog.getOwner());
        assertEquals(testFontChooser, dialog.getChooserPane());
        assertEquals(testOkListener, dialog.getOKButton().getActionListeners()[0]);
        assertEquals(testCancelListener, dialog.getCancelButton().getActionListeners()[0]);
    }

    @Test
    public void testFontChooserDialogConstructor_NullInput_ShouldCreateInstanceWithDefaultValues() {
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, null, null);

        assertEquals("", dialog.getTitle());
        assertFalse(dialog.isModal());
        assertNull(dialog.getOwner());
        assertNull(dialog.getChooserPane());
        assertNull(dialog.getOKButton().getActionListeners());
        assertNull(dialog.getCancelButton().getActionListeners());
    }

    @Test
    public void testSetVisible_True_ShouldSetInitialFont() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        testFontChooser.setFont(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(testComponent, "Test Dialog", true, testFontChooser, testOkListener, testCancelListener);

        dialog.setVisible(true);

        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testSetVisible_False_ShouldNotSetInitialFont() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        testFontChooser.setFont(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(testComponent, "Test Dialog", true, testFontChooser, testOkListener, testCancelListener);

        dialog.setVisible(false);

        assertNotEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testReset_ShouldResetFontToInitialFont() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        Font newFont = new Font("Times New Roman", Font.BOLD, 14);
        testFontChooser.setFont(newFont);
        FontChooserDialog dialog = new FontChooserDialog(testComponent, "Test Dialog", true, testFontChooser, testOkListener, testCancelListener);

        dialog.reset();

        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testCloserWindowClosing_ShouldSetWindowVisibleFalse() {
        JFrame frame = new JFrame();
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        WindowEvent windowEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);

        assertFalse(frame.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden_ShouldDisposeWindow() {
        JFrame frame = new JFrame();
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        ComponentEvent componentEvent = new ComponentEvent(frame, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);

        assertTrue(frame.isDisposed());
    }
}