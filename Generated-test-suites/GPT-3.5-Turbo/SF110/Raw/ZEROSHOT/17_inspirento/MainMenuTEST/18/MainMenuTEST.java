import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        assertNotNull(mainMenu);

        // Test menu creation
        String menu = "File";
        JMenu jMenu = mainMenu.createMenu(menu);
        assertNotNull(jMenu);
        assertEquals("File", jMenu.getText());
        assertEquals(0, jMenu.getMenuComponentCount());

        // Test menu item creation
        String item = "Open";
        JMenuItem jMenuItem = mainMenu.createMenuItem(item);
        assertNotNull(jMenuItem);
        assertEquals("Open", jMenuItem.getText());
        assertNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());

        // Test menu with submenu creation
        menu = "Edit";
        jMenu = mainMenu.createMenu(menu);
        assertNotNull(jMenu);
        assertEquals("Edit", jMenu.getText());
        assertEquals(1, jMenu.getMenuComponentCount());

        JMenu subMenu = (JMenu)jMenu.getMenuComponent(0);
        assertNotNull(subMenu);
        assertEquals("Find", subMenu.getText());
        assertEquals(1, subMenu.getMenuComponentCount());

        JMenuItem subMenuItem = (JMenuItem)subMenu.getMenuComponent(0);
        assertNotNull(subMenuItem);
        assertEquals("Replace", subMenuItem.getText());
        assertNull(subMenuItem.getAccelerator());
        assertNull(subMenuItem.getIcon());

        // Test menu item with shortcut and icon creation
        item = "#Bold";
        jMenuItem = mainMenu.createMenuItem(item);
        assertNotNull(jMenuItem);
        assertEquals("Bold", jMenuItem.getText());
        assertNotNull(jMenuItem.getAccelerator());
        assertNotNull(jMenuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        assertNotNull(mainMenu);

        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Undo");
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Save");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        assertNotNull(mainMenu);

        // Testing an empty method
        mainMenu.changed();
    }
}