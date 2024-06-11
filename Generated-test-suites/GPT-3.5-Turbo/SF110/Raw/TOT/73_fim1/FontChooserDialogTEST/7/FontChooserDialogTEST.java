import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(),"Title", true,
                new JFontChooser(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // okListener
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // cancelListener
            }
        });

        assertEquals("Title", dialog.getTitle());
        assertEquals(true, dialog.isModal());
        assertEquals("OK", dialog.getRootPane().getDefaultButton().getText());
    }

    @Test
    public void testSetVisible_InitialFontNotNull() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(),"Title", true,
                new JFontChooser(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // okListener
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // cancelListener
            }
        });

        dialog.setVisible(true);

        assertEquals(Font.class, dialog.getChooserPane().getFont().getClass());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(),"Title", true,
                new JFontChooser(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // okListener
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // cancelListener
            }
        });

        dialog.getChooserPane().setFont(new Font("Arial", Font.BOLD, 12));
        dialog.reset();

        assertEquals(Font.class, dialog.getChooserPane().getFont().getClass());
        assertEquals("Title", dialog.getTitle());
        assertEquals(true, dialog.isModal());
    }
}