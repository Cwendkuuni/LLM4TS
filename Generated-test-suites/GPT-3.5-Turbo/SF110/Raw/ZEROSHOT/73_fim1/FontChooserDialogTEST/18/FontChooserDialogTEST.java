package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    private Component mockComponent;
    private Font mockFont;
    private JFontChooser mockChooserPane;
    private ActionListener mockOkListener;
    private ActionListener mockCancelListener;

    @Before
    public void setUp() {
        mockComponent = mock(Component.class);
        mockFont = mock(Font.class);
        mockChooserPane = mock(JFontChooser.class);
        mockOkListener = mock(ActionListener.class);
        mockCancelListener = mock(ActionListener.class);
        
        final String title = "Test Title";
        final boolean modal = true;

        fontChooserDialog = new FontChooserDialog(mockComponent, title, modal, mockChooserPane, mockOkListener, mockCancelListener);
    }

    @Test
    public void testSetVisible() {
        // Arrange
        boolean visible = true;
        when(mockChooserPane.getFont()).thenReturn(mockFont);

        // Act
        fontChooserDialog.setVisible(visible);

        // Assert
        assertEquals(mockFont, fontChooserDialog.initialFont);
        verify(mockChooserPane, times(1)).getFont();
        verify(mockChooserPane, never()).setFont(any(Font.class));
        verify(mockChooserPane, never()).reset();
        verify(mockComponent, never()).setVisible(visible);
    }

    @Test
    public void testReset() {
        // Arrange
        when(mockChooserPane.getFont()).thenReturn(mockFont);

        // Act
        fontChooserDialog.reset();

        // Assert
        verify(mockChooserPane, times(1)).setFont(mockFont);
        verify(mockComponent, never()).setVisible(anyBoolean());
    }

    @Test
    public void testCloserWindowClosing() {
        // Arrange
        WindowEvent mockWindowEvent = mock(WindowEvent.class);
        Window mockWindow = mock(Window.class);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        when(mockWindowEvent.getWindow()).thenReturn(mockWindow);

        // Act
        closer.windowClosing(mockWindowEvent);

        // Assert
        verify(mockWindow, times(1)).setVisible(false);
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Arrange
        ComponentEvent mockComponentEvent = mock(ComponentEvent.class);
        Window mockWindow = mock(Window.class);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        when(mockComponentEvent.getComponent()).thenReturn(mockWindow);

        // Act
        disposeOnClose.componentHidden(mockComponentEvent);

        // Assert
        verify(mockWindow, times(1)).dispose();
    }
}