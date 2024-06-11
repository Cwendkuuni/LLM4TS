import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        // Test typical scenario
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        
        // Test edge case - menu item with no label
        JMenu emptyMenu = mainMenu.createMenu("");
        assertNull(emptyMenu);
        
        // Test exception handling - MissingResourceException
        JMenu errorMenu = mainMenu.createMenu("NonExistingMenu");
        assertNull(errorMenu);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem menuItem = new JMenuItem();
        
        // Test typical scenario - adding shortcut and icon
        mainMenu.addShortcutAndIcon(menuItem, "Save");
        assertNotNull(menuItem.getAccelerator());
        assertNotNull(menuItem.getIcon());
        
        // Test edge case - missing shortcut
        mainMenu.addShortcutAndIcon(menuItem, "Print");
        assertNull(menuItem.getAccelerator());
        
        // Test edge case - missing icon
        mainMenu.addShortcutAndIcon(menuItem, "Export");
        assertNotNull(menuItem.getAccelerator());
        assertNull(menuItem.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        // Test method does not throw exception
        try {
            mainMenu.changed();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}