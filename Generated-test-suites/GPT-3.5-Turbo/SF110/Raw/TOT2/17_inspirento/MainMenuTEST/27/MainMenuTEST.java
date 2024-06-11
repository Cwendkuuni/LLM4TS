import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testConstructor() {
        // Test constructor with a mock InspirentoMediator
        InspirentoMediator mockMediator = new MockInspirentoMediator();
        MainMenu mainMenu = new MainMenu(mockMediator);
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenu() {
        // Test createMenu method with valid menu name
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);

        // Test createMenu method with invalid menu name
        JMenu invalidMenu = mainMenu.createMenu("InvalidMenu");
        assertNull(invalidMenu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test addShortcutAndIcon method with valid key
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Save");
        assertNotNull(menuItem.getAccelerator());

        // Test addShortcutAndIcon method with invalid key
        JMenuItem invalidMenuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(invalidMenuItem, "InvalidKey");
        assertNull(invalidMenuItem.getAccelerator());
    }

    @Test
    public void testChanged() {
        // Test changed method
        // This method doesn't have any functionality, so just test if it executes without error
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
    }
}