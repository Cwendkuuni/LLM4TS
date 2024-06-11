
package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class FontChooserDialogTest {

    private FontChooserDialog dialog;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;

    @Before
    public void setUp() {
        chooserPane = mock(JFontChooser.class);
        okListener = mock(ActionListener.class);
        cancelListener = mock(ActionListener.class);
        dialog = new FontChooserDialog(null, "", false, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testConstructor() {
        assertEquals(JOptionPane.getRootFrame(), dialog.getOwner());
        assertEquals("", dialog.getTitle());
        assertFalse(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
    }

    @Test
    public void testDialogButtons() {
        JButton okButton = dialog.getOkButton();
        JButton cancelButton = dialog.getCancelButton();
        JButton resetButton = dialog.getResetButton();

        assertEquals(UIManager.getString("ColorChooser.okText"), okButton.getText());
        assertEquals(UIManager.getString("ColorChooser.cancelText"), cancelButton.getText());
        assertEquals(UIManager.getString("ColorChooser.resetText"), resetButton.getText());

        assertEquals("OK", okButton.getActionCommand());
        assertEquals("cancel", cancelButton.getActionCommand());

        assertTrue(okButton.getActionListeners().contains(okListener));
        assertTrue(cancelButton.getActionListeners().contains(cancelListener));

        assertTrue(okButton.getActionListeners().contains(dialog.getDefaultActionListener()));
        assertTrue(cancelButton.getActionListeners().contains(dialog.getDefaultActionListener()));
        assertTrue(resetButton.getActionListeners().contains(dialog.getResetActionListener()));
    }

    @Test
    public void testDialogVisibility() {
        dialog.setVisible(true);
        assertEquals(dialog.getFontChooserPane().getFont(), dialog.getInitialFont());

        dialog.setVisible(false);
        assertFalse(dialog.isShowing());
    }

    @Test
    public void testReset() {
        Font testFont = new Font("Test", Font.PLAIN, 12);
        dialog.setFontChooserPane(chooserPane);
        dialog.setInitialFont(testFont);

        dialog.reset();
        assertEquals(testFont, dialog.getFontChooserPane().getFont());
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        WindowEvent windowEvent = mock(WindowEvent.class);
        Window window = mock(Window.class);

        when(windowEvent.getWindow()).thenReturn(window);
        closer.windowClosing(windowEvent);

        assertTrue(window.isShowing());
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        ComponentEvent componentEvent = mock(ComponentEvent.class);
        Window window = mock(Window.class);

        when(componentEvent.getComponent()).thenReturn(window);
        disposeOnClose.componentHidden(componentEvent);

        assertTrue(window.isShowing());
    }
}

