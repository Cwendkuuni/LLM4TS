import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        // Test case 1
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menu1");
        assertNotNull(menu);
        
        // Test case 2
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menu2");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case 1
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key1");
        assertNotNull(item.getAccelerator());
        
        // Test case 2
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key2");
        assertNotNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
    }
}