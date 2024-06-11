import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class FontChooserDialogTest {

    private FontChooserDialog fontChooserDialog;
    private JFontChooser fontChooserPane;
    
    @Before
    public void setUp() {
        fontChooserPane = new JFontChooser();
        fontChooserDialog = new FontChooserDialog(null, "Test Dialog", true, fontChooserPane, null, null);
    }

    @Test
    public void testVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.initialFont, fontChooserPane.getFont());
    }

    @Test
    public void testReset() {
        Font newFont = new Font("Serif", Font.PLAIN, 12);
        fontChooserPane.setFont(newFont);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserPane.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JButton closeButton = new JButton();
        JDialog dialog = new JDialog();
        closeButton.addActionListener((ActionEvent e) -> {
            dialog.dispatchEvent(new java.awt.event.WindowEvent(dialog, java.awt.event.WindowEvent.WINDOW_CLOSING));
        });
        dialog.addWindowListener(closer);
        closeButton.doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JButton closeButton = new JButton();
        JDialog dialog = new JDialog();
        closeButton.addActionListener((ActionEvent e) -> {
            dialog.dispatchEvent(new java.awt.event.ComponentEvent(dialog, java.awt.event.ComponentEvent.COMPONENT_HIDDEN));
        });
        dialog.addComponentListener(disposeOnClose);
        closeButton.doClick();
        assertFalse(dialog.isDisplayable());
    }

}