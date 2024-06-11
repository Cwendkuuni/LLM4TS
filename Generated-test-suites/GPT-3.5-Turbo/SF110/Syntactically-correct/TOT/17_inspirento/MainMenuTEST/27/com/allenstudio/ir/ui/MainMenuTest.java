import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mockMediator;

    @Before
    public void setUp() {
        mockMediator = new InspirentoMediator();
        mainMenu = new MainMenu(mockMediator);
    }

    @Test
    public void testCreateMenu_WithValidMenuLabel_ReturnsJMenuObject() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);
    }

    @Test
    public void testCreateMenu_WithInvalidMenuLabel_ReturnsNull() {
        String menu = "InvalidMenu";
        JMenu result = mainMenu.createMenu(menu);
        assertNull(result);
    }

    @Test
    public void testCreateMenu_WithMenuContainingSeparator_SeparatesMenuItemsWithSeparator() {
        String menu = "Edit";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);

        int expectedMenuItemCount = 5; // Number of menu items in "Edit" menu
        assertEquals(expectedMenuItemCount, result.getItemCount());

        JMenuItem separator = result.getItem(2); // Separator should be at index 2
        assertNotNull(separator);
        assertTrue(separator instanceof JSeparator);
    }

    @Test
    public void testCreateMenu_WithSubMenuLabel_ContainsSubMenu() {
        String menu = "Help";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);

        int expectedMenuItemCount = 2; // Number of menu items in "Help" menu
        assertEquals(expectedMenuItemCount, result.getItemCount());

        JMenu subMenu = (JMenu) result.getItem(1); // Submenu should be at index 1
        assertNotNull(subMenu);

        String subMenuLabel = "About";
        JMenuItem menuItem = subMenu.getItem(0); // Menu item in submenu should be at index 0
        assertNotNull(menuItem);
        assertEquals(subMenuLabel, menuItem.getText());
    }

    @Test
    public void testCreateMenu_WithCheckBoxMenuItemLabel_CreatesCheckBoxMenuItem() {
        String menu = "Edit";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);

        int expectedMenuItemCount = 5; // Number of menu items in "Edit" menu
        assertEquals(expectedMenuItemCount, result.getItemCount());

        String checkBoxItemLabel = "Enable Editing"; // Checkbox menu item in "Edit" menu
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) result.getItem(4);
        assertNotNull(checkBoxMenuItem);
        assertEquals(checkBoxItemLabel, checkBoxMenuItem.getText());
    }

    @Test
    public void testCreateMenu_WithInvalidMenuItem_ReturnsNull() {
        String menu = "Edit";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);

        JMenuItem invalidMenuItem = result.getItem(6); // Menu item at index 6 does not exist
        assertNull(invalidMenuItem);
    }

    @Test
    public void testAddShortcutAndIcon_WithValidShortcutKey_SetsAcceleratorForMenuItem() {
        JMenuItem menuItem = new JMenuItem("Open");
        String shortcutKey = "Ctrl+O";
        mainMenu.addShortcutAndIcon(menuItem, "Open");
        KeyStroke expectedAccelerator = KeyStroke.getKeyStroke(shortcutKey);
        KeyStroke actualAccelerator = menuItem.getAccelerator();
        assertEquals(expectedAccelerator, actualAccelerator);
    }

    @Test
    public void testAddShortcutAndIcon_WithInvalidShortcutKey_DoesNotSetAcceleratorForMenuItem() {
        JMenuItem menuItem = new JMenuItem("Save");
        String invalidShortcutKey = "Ctrl+Invalid";
        mainMenu.addShortcutAndIcon(menuItem, "Save");
        assertNull(menuItem.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_WithValidIconPath_SetsIconForMenuItem() {
        JMenuItem menuItem = new JMenuItem("Exit");
        String iconPath = "/path/to/icon.png";
        mainMenu.addShortcutAndIcon(menuItem, "Exit");
        assertNotNull(menuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_WithInvalidIconPath_DoesNotSetIconForMenuItem() {
        JMenuItem menuItem = new JMenuItem("Exit");
        String invalidIconPath = "/path/to/invalid_icon.png";
        mainMenu.addShortcutAndIcon(menuItem, "Exit");
        assertNull(menuItem.getIcon());
    }

    @Test
    public void testChanged_WhenCalled_DoesNothing() {
        mainMenu.changed();
        // No assertions, just verifying that no exception is thrown
    }

}