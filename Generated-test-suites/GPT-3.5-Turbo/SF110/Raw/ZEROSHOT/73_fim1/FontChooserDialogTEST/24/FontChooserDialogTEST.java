import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        // Test case for FontChooserDialog constructor
        Component c = new Component() {
        };
        String title = "Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
    }

    @Test
    public void testSetVisible() {
        // Test case for setVisible method
        Component c = new Component() {
        };
        String title = "Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
    }

    @Test
    public void testReset() {
        // Test case for reset method
        Component c = new Component() {
        };
        String title = "Title";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.reset();
        assertNotNull(dialog.chooserPane.getFont());
    }
}