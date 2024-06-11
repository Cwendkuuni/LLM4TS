import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu menu;

    @Before
    public void setUp() {
        menu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void testCreateMenu_ValidMenu_ReturnJMenu() {
        String menuName = "File";
        JMenu result = menu.createMenu(menuName);
        assertNotNull(result);
        assertEquals(menuName, result.getText());
    }

    @Test
    public void testCreateMenu_InvalidMenu_ReturnNull() {
        String menuName = "InvalidMenu";
        JMenu result = menu.createMenu(menuName);
        assertNull(result);
    }

    @Test
    public void testAddShortcutAndIcon_ValidShortcut_ReturnMenuItemWithAccelerator() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_InvalidShortcut_DoNothing() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidShortcut";
        menu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_ValidIcon_ReturnMenuItemWithIcon() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_InvalidIcon_DoNothing() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidIcon";
        menu.addShortcutAndIcon(item, key);
        assertNull(item.getIcon());
    }

}