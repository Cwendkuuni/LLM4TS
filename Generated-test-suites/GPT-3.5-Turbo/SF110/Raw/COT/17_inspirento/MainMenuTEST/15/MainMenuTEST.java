import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        // Test case to check the functionality of createMenu method
        MainMenu mainMenu = new MainMenu(null);
        String menuName = "File";
        JMenu result = mainMenu.createMenu(menuName);
        assertNotNull(result);
        assertEquals(menuName, result.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case to check the functionality of addShortcutAndIcon method
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        String key = "menu.item";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());

        key = "menu.itemShortcut";
        mainMenu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());

        key = "menu.itemIcon";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        // Test case to check the functionality of changed method
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
        // No assertion as the method does not have any return value
    }
}