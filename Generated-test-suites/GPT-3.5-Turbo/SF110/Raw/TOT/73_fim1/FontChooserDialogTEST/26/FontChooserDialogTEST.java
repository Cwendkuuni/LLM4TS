
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFontChooser;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Mock
    private Component mockComponent;
    @Mock
    private ActionListener mockOkListener;
    @Mock
    private ActionListener mockCancelListener;
    @Mock
    private ActionListener mockChooserOkListener;
    @Mock
    private ActionListener mockChooserCancelListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fontChooserDialog_VisibleInitially_SetsInitialFont() {
        // Arrange
        JFontChooser mockChooserPane = mock(JFontChooser.class);
        fontChooserDialog = new FontChooserDialog(mockComponent, "Title", true,
                mockChooserPane, mockOkListener, mockCancelListener);

        // Act
        fontChooserDialog.setVisible(true);

        // Assert
        verify(mockChooserPane).getFont();
    }

    @Test
    public void fontChooserDialog_OkButtonClicked_CallsOkListenerAndHidesDialog() {
        // Arrange
        JFontChooser mockChooserPane = mock(JFontChooser.class);
        fontChooserDialog = new FontChooserDialog(mockComponent, "Title", true,
                mockChooserPane, mockOkListener, mockCancelListener);

        // Act
        ActionListener okButtonAction = getActionListenerFromButton(fontChooserDialog,
                "OK");
        okButtonAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        // Assert
        verify(mockOkListener).actionPerformed(any(ActionEvent.class));
        verify(mockComponent).setVisible(false);
    }

    @Test
    public void fontChooserDialog_CancelButtonClicked_CallsCancelListenerAndHidesDialog() {
        // Arrange
        JFontChooser mockChooserPane = mock(JFontChooser.class);
        fontChooserDialog = new FontChooserDialog(mockComponent, "Title", true,
                mockChooserPane, mockOkListener, mockCancelListener);

        // Act
        ActionListener cancelButtonAction = getActionListenerFromButton(fontChooserDialog,
                "cancel");
        cancelButtonAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        // Assert
        verify(mockCancelListener).actionPerformed(any(ActionEvent.class));
        verify(mockComponent).setVisible(false);
    }

    @Test
    public void resetButton_Clicked_ResetsFontToInitial() {
        // Arrange
        JFontChooser mockChooserPane = mock(JFontChooser.class);
        fontChooserDialog = new FontChooserDialog(mockComponent, "Title", true,
                mockChooserPane, mockOkListener, mockCancelListener);
        when(mockChooserPane.getFont()).thenReturn(mock(Font.class));

        // Act
        ActionListener resetButtonAction = getActionListenerFromButton(fontChooserDialog,
                "reset");
        resetButtonAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        // Assert
        verify(mockChooserPane).setFont(any(Font.class));
    }

    private ActionListener getActionListenerFromButton(FontChooserDialog dialog, String command) {
        Component[] components = dialog.getContentPane().getComponents();
        for(Component component: components) {
            if (component instanceof JPanel) {
                Component[] buttonComponents = ((JPanel)component).getComponents();
                for(Component buttonComponent: buttonComponents) {
                    if (buttonComponent instanceof JButton &&
                            ((JButton)buttonComponent).getActionCommand().equals(command)) {
                        return ((JButton)buttonComponent).getActionListeners()[0];
                    }
                }
            }
        }
        return null;
    }
}
