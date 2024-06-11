import org.junit.*;
import org.mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = Mockito.spy(new FontChooserDialog(null, "Test", false, null, null, null));

        dialog.setVisible(true);
        Mockito.verify(dialog).setVisible(true);

        dialog.setVisible(false);
        Mockito.verify(dialog).setVisible(false);
    }

    @Test
    public void testReset() {
        Font initialFont = Mockito.mock(Font.class);
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, chooserPane, null, null);

        dialog.setVisible(true);
        Mockito.verify(chooserPane).setFont(initialFont);
    }

    @Test
    public void testOkButtonListener() {
        ActionListener okListener = Mockito.mock(ActionListener.class);
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, okListener, null);

        JButton okButton = Mockito.mock(JButton.class);
        Mockito.when(okButton.getActionCommand()).thenReturn("OK");

        dialog.setVisible(true);
        okButton.getActionListeners()[0].actionPerformed(null);

        Mockito.verify(okListener).actionPerformed(ArgumentMatchers.any(ActionEvent.class));
        Mockito.verify(dialog).setVisible(false);
    }

    @Test
    public void testCancelButtonListener() {
        ActionListener cancelListener = Mockito.mock(ActionListener.class);
        FontChooserDialog dialog = new FontChooserDialog(null, null, false, null, null, cancelListener);

        JButton cancelButton = Mockito.mock(JButton.class);
        Mockito.when(cancelButton.getActionCommand()).thenReturn("cancel");

        dialog.setVisible(true);
        cancelButton.getActionListeners()[0].actionPerformed(null);

        Mockito.verify(cancelListener).actionPerformed(ArgumentMatchers.any(ActionEvent.class));
        Mockito.verify(dialog).setVisible(false);
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = Mockito.spy(new FontChooserDialog.Closer());

        WindowEvent windowEvent = Mockito.mock(WindowEvent.class);
        Window window = Mockito.mock(Window.class);
        Mockito.when(windowEvent.getWindow()).thenReturn(window);

        closer.windowClosing(windowEvent);

        Mockito.verify(window).setVisible(false);
    }

    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = Mockito.spy(new FontChooserDialog.DisposeOnClose());

        ComponentEvent componentEvent = Mockito.mock(ComponentEvent.class);
        Component component = Mockito.mock(Component.class);
        Window window = Mockito.mock(Window.class);
        Mockito.when(componentEvent.getComponent()).thenReturn(component);
        Mockito.when(component instanceof Window).thenReturn(true);
        Mockito.when((Window) component).thenReturn(window);

        disposeOnClose.componentHidden(componentEvent);

        Mockito.verify(window).dispose();
    }

    // 
    // Include all the test cases above
    // 
}