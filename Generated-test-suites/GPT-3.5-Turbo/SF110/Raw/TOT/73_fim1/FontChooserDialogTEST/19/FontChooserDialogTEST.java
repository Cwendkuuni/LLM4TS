
import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

    @BeforeClass
    public static void setUpClass() {
        // Initialize necessary resources before running test cases
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up resources after running all test cases
    }

    @Before
    public void setUp() {
        // Initialize necessary resources before each test case
    }

    @After
    public void tearDown() {
        // Clean up resources after each test case
    }

    @Test
    public void testSetVisible_True() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog.getChooserPane().getFont());
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testSetVisible_False() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", false, null, null, null);
        Font initialFont = dialog.getChooserPane().getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 14);
        dialog.getChooserPane().setFont(newFont);
        dialog.reset();
        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window window = new Window(new Frame());
        WindowEvent windowEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(windowEvent);
        assertFalse(window.isVisible());
    }

    @Test
    public void testComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window window = new Window(new Frame());
        ComponentEvent componentEvent = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(componentEvent);
        assertTrue(window.isDisposed());
    }

}
