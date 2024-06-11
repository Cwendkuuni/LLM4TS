import org.junit.Test;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogInitialization() {
        Component component = new JButton();
        String title = "Font Chooser";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = null;
        ActionListener cancelListener = null;

        FontChooserDialog fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(title, fontChooserDialog.getTitle());
        assertEquals(modal, fontChooserDialog.isModal());
        assertEquals(component, fontChooserDialog.getParent());
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Font Chooser", true, new JFontChooser(), null, null);

        fontChooserDialog.setVisible(true);
        assertEquals(true, fontChooserDialog.isVisible());

        fontChooserDialog.setVisible(false);
        assertEquals(false, fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Font Chooser", true, new JFontChooser(), null, null);
        fontChooserDialog.reset();

        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }
}