import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu_withValidMenu_shouldCreateJMenu() {
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu_withInvalidMenu_shouldReturnNull() {
        JMenu menu = mainMenu.createMenu("InvalidMenu");
        assertNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon_withValidKey_shouldAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Open");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_withInvalidKey_shouldNotAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidKey");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged_shouldNotThrowException() {
        try {
            mainMenu.changed();
        } catch (Exception e) {
            fail("Changing MainMenu state threw an exception");
        }
    }

}