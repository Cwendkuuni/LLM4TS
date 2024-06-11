import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertNotNull(menu.createMenu("menu1"));
    }

    @Test
    public void testAddShortcutAndIcon_KeyExists() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        menu.addShortcutAndIcon(item, "key1");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_KeyNotExists() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        menu.addShortcutAndIcon(item, "key2");
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_IconExists() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        menu.addShortcutAndIcon(item, "key3");
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_IconNotExists() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        menu.addShortcutAndIcon(item, "key4");
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        menu.changed(); // Just invoking the method for code coverage
        assertTrue(true);
    }
}