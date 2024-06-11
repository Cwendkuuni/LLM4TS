
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // Test a menu with only one item - success scenario
        JMenu menu = mainMenu.createMenu("File");
        assertEquals("File", menu.getText());
        
        // Test a menu with multiple items, including separators - success scenario
        menu = mainMenu.createMenu("Edit");
        assertEquals("Edit", menu.getText());
        assertEquals(8, menu.getItemCount());
        
        // Test a menu with a checkbox menu item - success scenario
        menu = mainMenu.createMenu("View");
        assertEquals("View", menu.getText());
        assertEquals(menu.getItem(0).getClass(), JCheckBoxMenuItem.class);
        
        // Test a menu that does not exist - error scenario
        menu = mainMenu.createMenu("NonExistentMenu");
        assertNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("Help");
        
        JMenuItem item = new JMenuItem();
        
        // Test adding a shortcut to an item - success scenario
        mainMenu.addShortcutAndIcon(item, "HelpLabel");
        assertEquals("ctrl H", item.getAccelerator().toString());
        
        // Test adding an icon to an item - success scenario
        mainMenu.addShortcutAndIcon(item, "HelpIcon");
        assertNotNull(item.getIcon());
        
        // Test adding a shortcut to an item that does not exist - error scenario
        mainMenu.addShortcutAndIcon(item, "NonExistentItem");
        assertNull(item.getAccelerator());
        
        // Test adding an icon to an item that does not exist - error scenario
        mainMenu.addShortcutAndIcon(item, "NonExistentItem");
        assertNull(item.getIcon());
    }
}
