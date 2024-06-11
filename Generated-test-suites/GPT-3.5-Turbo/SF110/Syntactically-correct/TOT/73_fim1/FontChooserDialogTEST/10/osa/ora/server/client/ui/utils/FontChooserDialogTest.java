import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RunWith(MockitoJUnitRunner.class)
public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;

    @Mock
    private Component component;

    @Mock
    private JFontChooser fontChooser;

    @Mock
    private ActionListener okListener;

    @Mock
    private ActionListener cancelListener;

    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog(component, "Font Chooser", true, fontChooser, okListener, cancelListener);
    }

    @Test
    public void testSetVisible_InitialFontSetToChooserPaneFont() {
        fontChooserDialog.setVisible(true);
        Mockito.verify(fontChooser).setFont(Mockito.eq(fontChooserDialog.getFont()));
    }

    @Test
    public void testReset_ResetButtonResetsFontChooserPane() {
        JButton resetButton = getResetButton();
        resetButton.doClick();
        Mockito.verify(fontChooser).reset();
    }


    private JButton getResetButton() {
        JPanel buttonPane = (JPanel) fontChooserDialog.getContentPane().getComponent(1);
        return (JButton) buttonPane.getComponent(2);
    }
}