import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontChooserDialog;
    private Component component;
    private String title;
    private boolean modal;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;
    
    @Before
    public void setUp() {
        component = new JComponent() {};
        title = "Font Chooser Dialog";
        modal = true;
        chooserPane = new JFontChooser();
        okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {};
        };
        cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {};
        };
        fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(fontChooserDialog);
        assertEquals(title, fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
        assertEquals(chooserPane, fontChooserDialog.getChooserPane());
    }
    
    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.getInitialFont());
        assertFalse(fontChooserDialog.isVisible());
    }
    
    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.getChooserPane().getFont();
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
    }
}