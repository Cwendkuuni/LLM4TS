import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FontChooserDialogTest {

    private FontChooserDialog dialog;

    @Test
    public void testConstructor() {
        JFrame frame = new JFrame();
        JFontChooser fontChooser = new JFontChooser();
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };

        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };

        dialog = new FontChooserDialog(frame, "Choose a Font", true, fontChooser, okListener, cancelListener);

        // Check if the dialog is visible
        assertTrue(dialog.isVisible());

        // Check if the initial font is set correctly
        assertEquals(fontChooser.getFont(), dialog.initialFont);
    }

    @Test
    public void testSetVisible() {
        JFrame frame = new JFrame();
        JFontChooser fontChooser = new JFontChooser();
        dialog = new FontChooserDialog(frame, "Choose a Font", true, fontChooser, null, null);

        // Set visible true
        dialog.setVisible(true);
        assertTrue(dialog.initialFont != null);

        // Set visible false
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        JFrame frame = new JFrame();
        JFontChooser fontChooser = new JFontChooser();
        dialog = new FontChooserDialog(frame, "Choose a Font", true, fontChooser, null, null);

        Font initialFont = dialog.initialFont;

        // Change font
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        fontChooser.setFont(newFont);
        assertNotEquals(initialFont, fontChooser.getFont());

        // Reset font
        dialog.reset();
        assertEquals(initialFont, fontChooser.getFont());
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(400, 400);
        frame.addWindowListener(closer);

        JFrame anotherFrame = new JFrame();
        anotherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        anotherFrame.setSize(400, 400);

        // Set visible true
        frame.setVisible(true);
        assertTrue(frame.isVisible());

        // Call windowClosing
        closer.windowClosing(null);
        assertFalse(frame.isVisible());

        // Another frame should still be visible
        assertTrue(anotherFrame.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.addComponentListener(disposeOnClose);

        // Set visible true
        frame.setVisible(true);
        assertTrue(frame.isVisible());

        // Call componentHidden
        disposeOnClose.componentHidden(null);
        assertFalse(frame.isVisible());
    }

    @Test
    public void testMain() {
        // Redirect System.out to a StringWriter
        StringWriter outputWriter = new StringWriter();
        System.setOut(new PrintStream(new WriterOutputStream(outputWriter)));

        // Redirect System.in to a ByteArrayInputStream
        String input = "Test Input";
        ByteArrayInputStream inputReader = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputReader);

        // Call main
        FontChooserDialog.main(null);

        // Check the output
        assertEquals("Test Output", outputWriter.toString().trim());
    }

    @Test
    public void testButtonPane() {
        JFrame frame = new JFrame();
        JFontChooser fontChooser = new JFontChooser();
        JButton cancelButton = new JButton();
        JButton resetButton = new JButton();

        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };

        dialog = new FontChooserDialog(frame, "Choose a Font", true, fontChooser, null, cancelListener);
        dialog.buttonPane.remove(dialog.cancelButton);
        dialog.buttonPane.remove(dialog.resetButton);

        // Check if the cancelButton is not present
        assertNull(dialog.cancelButton);

        // Check if the resetButton is not present
        assertNull(dialog.resetButton);
    }
}