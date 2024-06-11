import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    
    MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void testCreateMenu() {
        // Test typical scenario
        assertNotNull(mainMenu.createMenu("File"));
        
        // Test edge case - empty menu
        assertNull(mainMenu.createMenu(""));
        
        // Test exception handling - MissingResourceException
        try {
            assertNull(mainMenu.createMenu("NonExistentMenu"));
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test typical scenario - shortcut and icon present
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "FileNew");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
        
        // Test edge case - shortcut and icon missing
        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "FileOpen");
        assertNull(item2.getAccelerator());
        assertNull(item2.getIcon());
        
        // Error scenario - testing for handling of exception
        try {
            mainMenu.addShortcutAndIcon(item, "NonExistentKey");
        } catch (MissingResourceException e) {
            fail("Should not throw MissingResourceException");
        }
    }

}