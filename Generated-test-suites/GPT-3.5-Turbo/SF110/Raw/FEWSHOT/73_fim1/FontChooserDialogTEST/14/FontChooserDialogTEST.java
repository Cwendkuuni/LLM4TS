package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.Window;

import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class FontChooserDialogTest {

    private Component mockComponent;
    private String mockTitle;
    private boolean mockModal;
    private JFontChooser mockChooserPane;
    private ActionListener mockOkListener;
    private ActionListener mockCancelListener;

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setup() {
        mockComponent = mock(Component.class);
        mockTitle = "Title";
        mockModal = true;
        mockChooserPane = mock(JFontChooser.class);
        mockOkListener = mock(ActionListener.class);
        mockCancelListener = mock(ActionListener.class);

        fontChooserDialog = new FontChooserDialog(mockComponent, mockTitle, mockModal, mockChooserPane, mockOkListener, mockCancelListener);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);

        // Test initialFont was set
        assertEquals(mockChooserPane.getFont(), fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        fontChooserDialog.reset();

        // Test chooserPane.setFont was called with initialFont
        verify(mockChooserPane).setFont(fontChooserDialog.initialFont);
    }

    @Test
    public void testCloser() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        // Test windowClosing
        WindowEvent mockWindowEvent = mock(WindowEvent.class);
        closer.windowClosing(mockWindowEvent);

        Window mockWindow = mockWindowEvent.getWindow();
        // Test window.setVisible was called with false
        verify(mockWindow).setVisible(false);
    }

    @Test
    public void testDisposeOnClose() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        // Test componentHidden
        ComponentEvent mockComponentEvent = mock(ComponentEvent.class);
        disposeOnClose.componentHidden(mockComponentEvent);

        Window mockWindow = (Window) mockComponentEvent.getComponent();
        // Test window.dispose was called
        verify(mockWindow).dispose();
    }
}