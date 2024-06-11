import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FontChooserDialogTest {

    private Font initialFont;
    private JFontChooser chooserPane;

    @Before
    public void setUp() {
        initialFont = Font.getFont(Font.SANS_SERIF);
        chooserPane = Mockito.mock(JFontChooser.class);
    }

    @Test
    public void testFontChooserDialog_NullListeners() {
        // Arrange
        Component c = Mockito.mock(Component.class);
        String title = "Test Title";
        boolean modal = true;
        ActionListener okListener = null;
        ActionListener cancelListener = null;

        // Act
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Assert
        assertEquals(title, fontChooserDialog.getTitle());
    }

    @Test
    public void testFontChooserDialog_NullInputMapAndActionMap() {
        // Arrange
        Component c = Mockito.mock(Component.class);
        String title = "Test Title";
        boolean modal = true;
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);
        UIManager.put("ColorChooser.cancelText", "Cancel");

        // Act
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Assert
        JButton cancelButton = (JButton) fontChooserDialog.getContentPane().getComponent(1);
        assertNull(cancelButton.getInputMap(2));
        assertNull(cancelButton.getActionMap());
    }

    @Test
    public void testFontChooserDialog_InvalidMnemonic() {
        // Arrange
        Component c = Mockito.mock(Component.class);
        String title = "Test Title";
        boolean modal = true;
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);
        UIManager.put("ColorChooser.resetText", "Reset");
        UIManager.put("ColorChooser.resetMnemonic", -1);

        // Act
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Assert
        JButton resetButton = (JButton) fontChooserDialog.getContentPane().getComponent(2);
        assertEquals(-1, resetButton.getMnemonic());
    }

    @Test
    public void testReset() {
        // Arrange
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "", false, chooserPane, null, null);
        fontChooserDialog.setVisible(true);

        // Act
        fontChooserDialog.reset();

        // Assert
        Mockito.verify(chooserPane).setFont(initialFont);
    }

    @Test
    public void testCloser_WindowClosing() {
        // Arrange
        WindowEvent windowEvent = Mockito.mock(WindowEvent.class);
        Window window = Mockito.mock(Window.class);
        Mockito.when(windowEvent.getWindow()).thenReturn(window);

        // Act
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowEvent);

        // Assert
        Mockito.verify(window).setVisible(false);
    }

    @Test
    public void testDisposeOnClose_ComponentHidden() {
        // Arrange
        ComponentEvent componentEvent = Mockito.mock(ComponentEvent.class);
        Window window = Mockito.mock(Window.class);
        Mockito.when(componentEvent.getComponent()).thenReturn(window);

        // Act
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentEvent);

        // Assert
        Mockito.verify(window).dispose();
    }

}