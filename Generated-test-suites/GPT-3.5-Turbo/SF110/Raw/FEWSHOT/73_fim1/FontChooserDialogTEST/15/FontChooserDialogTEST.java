package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

    private Component component;
    private JFontChooser choosenPane;

    @Before
    public void setUp() {
        component = Mockito.mock(Component.class);
        choosenPane = Mockito.mock(JFontChooser.class);
    }

    @Test
    public void testConstructor() {
        JOptionPane optionPane = new JOptionPane();
        UIManager.put("ColorChooser.okText", "OK");
        UIManager.put("ColorChooser.cancelText", "Cancel");
        UIManager.put("ColorChooser.resetText", "Reset");
        UIManager.put("ColorChooser.resetMnemonic", 1);

        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog dialog = new FontChooserDialog(component, "Title", true, choosenPane, okListener, cancelListener);

        assertEquals("Title", dialog.getTitle());
        assertEquals(true, dialog.isModal());

        verify(choosenPane).getFont();
        verify(component).getFont();
        verify(choosenPane).setFont(any(Font.class));
        verify(comp).setVisible(true);
        verifyNoMoreInteractions(comp);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(component, "Title", true, choosenPane, null, null);
        ActionListener okListener = mock(ActionListener.class);

        dialog.setVisible(true);

        verify(choosenPane).getFont();
        verifyNoMoreInteractions(comp);
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(component, "Title", true, choosenPane, null, null);

        dialog.reset();

        verify(choosenPane).setFont(any(Font.class));
        verifyNoMoreInteractions(comp);
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

        Window window = Mockito.mock(Window.class);
        WindowEvent event = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);

        closer.windowClosing(event);

        verify(window).setVisible(false);
        verify(window).dispose();
        verifyNoMoreInteractions(window);
    }

    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

        Window window = Mockito.mock(Window.class);
        ComponentEvent event = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);

        disposeOnClose.componentHidden(event);

        verify(window).dispose();
        verifyNoMoreInteractions(window);
    }

}