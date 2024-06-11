import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FontChooserDialogTest {
    
    private FontChooserDialog fontChooserDialog;
    private Component component;
    private JFontChooser fontChooser;
    private ActionListener okListener;
    private ActionListener cancelListener;
    
    @Before
    public void setup() {
        component = mock(Component.class);
        fontChooser = mock(JFontChooser.class);
        okListener = mock(ActionListener.class);
        cancelListener = mock(ActionListener.class);
        fontChooserDialog = new FontChooserDialog(component, "Test", true, fontChooser, okListener, cancelListener);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullComponent() {
        new FontChooserDialog(null, "Test", true, fontChooser, okListener, cancelListener);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        new FontChooserDialog(component, null, true, fontChooser, okListener, cancelListener);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullChooserPane() {
        new FontChooserDialog(component, "Test", true, null, okListener, cancelListener);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullListeners() {
        new FontChooserDialog(component, "Test", true, fontChooser, null, null);
    }
    
    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertNotNull(fontChooserDialog.initialFont);
        verify(fontChooser).getFont();
        
        fontChooserDialog.setVisible(false);
        assertTrue(fontChooserDialog.initialFont == null);
    }
    
    @Test
    public void testReset() {
        fontChooserDialog.reset();
        verify(fontChooser).setFont(null);
    }
}