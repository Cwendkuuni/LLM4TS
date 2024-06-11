
package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        fontChooserDialog = mock(FontChooserDialog.class);
    }

    @Test
    public void setVisible_true_setsInitialFont() {
        fontChooserDialog.setVisible(true);
        verify(fontChooserDialog).setVisible(true);
        verify(fontChooserDialog).getContentPane();
        verify(fontChooserDialog).getContentPane().setLayout(any(BorderLayout.class));
        verify(fontChooserDialog).getContentPane().add(any(JFontChooser.class), eq("Center"));
        verify(fontChooserDialog).getContentPane().add(any(JPanel.class), eq("South"));

        verify(fontChooserDialog).pack();
        verify(fontChooserDialog).getRootPane();
        verify(fontChooserDialog.getRootPane()).setDefaultButton(any(JButton.class));
        verify(fontChooserDialog.getRootPane().getDefaultButton()).setActionCommand("OK");

        verifyNoMoreInteractions(fontChooserDialog);

    }

    @Test
    public void reset_callsResetOnChooserPane() {
        fontChooserDialog.reset();
        verify(fontChooserDialog).reset();
        verify(fontChooserDialog).chooserPane.setFont(fontChooserDialog.initialFont);

        verifyNoMoreInteractions(fontChooserDialog);
    }

    @Test
    public void Closer_windowClosing_hidesWindow() {
        WindowEvent windowEvent = mock(WindowEvent.class);
        Window window = mock(Window.class);

        when(windowEvent.getWindow()).thenReturn(window);

        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);

        verify(window).setVisible(false);

        verifyNoMoreInteractions(window, windowEvent);
    }

    @Test
    public void DisposeOnClose_componentHidden_disposesWindow() {
        ComponentEvent componentEvent = mock(ComponentEvent.class);
        Window window = mock(Window.class);

        when(componentEvent.getComponent()).thenReturn(window);

        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);

        verify(window).dispose();

        verifyNoMoreInteractions(window, componentEvent);
    }

    @Test
    public void actionPerformed_actonEvent_okButton() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        JFontChooser chooserPane = mock(JFontChooser.class);

        doCallRealMethod().when(fontChooserDialog).actionPerformed(actionEvent);
        when(actionEvent.getActionCommand()).thenReturn("OK");
        doNothing().when(fontChooserDialog).setVisible(false);

        fontChooserDialog.actionPerformed(actionEvent);

        verify(fontChooserDialog).actionPerformed(actionEvent);
        verify(fontChooserDialog).setVisible(false);

        verifyNoMoreInteractions(fontChooserDialog, actionEvent);
        verifyZeroInteractions(chooserPane);
    }

}


