import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JOptionPane.class, UIManager.class, FontChooserDialog.class})
public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    private Component componentMock;
    private String titleMock;
    private boolean modalMock;
    private JFontChooser fontChooserMock;
    private ActionListener okListenerMock;
    private ActionListener cancelListenerMock;

    @Before
    public void setup() {
        componentMock = Mockito.mock(Component.class);
        titleMock = "Test Title";
        modalMock = true;
        fontChooserMock = Mockito.mock(JFontChooser.class);
        okListenerMock = Mockito.mock(ActionListener.class);
        cancelListenerMock = Mockito.mock(ActionListener.class);
        
        PowerMockito.mockStatic(JOptionPane.class);
        PowerMockito.mockStatic(UIManager.class);
        PowerMockito.when(JOptionPane.getFrameForComponent(componentMock)).thenReturn(Mockito.mock(JFrame.class));
        PowerMockito.when(UIManager.getString(Mockito.anyString())).thenReturn("Test String");
    }

    @Test
    public void testConstructor() {
        // Test mock objects
        Mockito.when(chooserPaneMock.getFont()).thenReturn(Mockito.mock(Font.class));
        
        // Expected values
        final String okString = "Test String";
        final String cancelString = "Test String";
        final String resetString = "Test String";

        // Call constructor
        fontChooserDialog = new FontChooserDialog(componentMock, titleMock, modalMock, fontChooserMock, okListenerMock, cancelListenerMock);
        
        // Verify method calls
        PowerMockito.verifyStatic(JOptionPane.class);
        JOptionPane.getFrameForComponent(componentMock);
        Mockito.verify(fontChooserDialog).getRootPane();
        Mockito.verify(okButtonMock).setActionCommand("OK");
        Mockito.verify(okButtonMock).addActionListener(okListenerMock);
        Mockito.verify(okButtonMock).addActionListener(Mockito.any(ActionListener.class));
        Mockito.verify(buttonPaneMock).add(okButtonMock);
        Mockito.verify(cancelButtonMock).setActionCommand("cancel");
        Mockito.verify(cancelButtonMock).addActionListener(cancelListenerMock);
        Mockito.verify(cancelButtonMock).addActionListener(Mockito.any(ActionListener.class));
        Mockito.verify(buttonPaneMock).add(cancelButtonMock);
        Mockito.verify(resetButtonMock).addActionListener(Mockito.any(ActionListener.class));
        Mockito.verify(buttonPaneMock).add(resetButtonMock);
        Mockito.verify(fontChooserDialog).getContentPane();
        Mockito.verify(contentPaneMock).setLayout(Mockito.any(BorderLayout.class));
        Mockito.verify(contentPaneMock).add(chooserPaneMock, "Center");
        Mockito.verify(contentPaneMock).add(buttonPaneMock, "South");
        Mockito.verify(fontChooserDialog).pack();
        Mockito.verify(fontChooserDialog).setLocationRelativeTo(componentMock);
        Mockito.verify(chooserPaneMock).setFont(Mockito.any(Font.class));
    }

    @Test
    public void testSetVisible() {
        // Call setVisible
        fontChooserDialog.setVisible(true);
        
        // Verify method calls
        Mockito.verify(chooserPaneMock).getFont();
        Mockito.verify(fontChooserDialog).setVisible(true);
    }

    @Test
    public void testReset() {
        // Call reset
        fontChooserDialog.reset();
        
        // Verify method calls
        Mockito.verify(chooserPaneMock).setFont(initialFontMock);
    }

    @Test
    public void testCloserWindowClosing() {
        // Setup
        WindowEvent windowEventMock = Mockito.mock(WindowEvent.class);
        Window windowMock = Mockito.mock(Window.class);
        
        // Call windowClosing
        fontChooserDialog.new Closer().windowClosing(windowEventMock);
        
        // Verify method calls
        Mockito.verify(windowMock).setVisible(false);
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        // Setup
        ComponentEvent componentEventMock = Mockito.mock(ComponentEvent.class);
        Window windowMock = Mockito.mock(Window.class);
        
        // Call componentHidden
        fontChooserDialog.new DisposeOnClose().componentHidden(componentEventMock);
        
        // Verify method calls
        Mockito.verify(windowMock).dispose();
    }
}