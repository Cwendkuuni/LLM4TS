import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull("Menu not created", menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key");
        assertNull("Shortcut not added", item.getAccelerator());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
        // No assertions since the method does not return anything
    }
}