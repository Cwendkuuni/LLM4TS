import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        // Typical use case
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        
        // Edge case: Menu label with special characters
        menu = mainMenu.createMenu("File*");
        assertNotNull(menu);
        
        // Error scenario: No label available
        menu = mainMenu.createMenu("InvalidMenu");
        assertNull(menu);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        
        // Typical use case
        mainMenu.addShortcutAndIcon(item, "Save");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
        
        // Edge case: Missing shortcut
        mainMenu.addShortcutAndIcon(item, "New");
        assertNotNull(item.getIcon());
        assertNull(item.getAccelerator());
        
        // Error scenario: Invalid icon path
        mainMenu.addShortcutAndIcon(item, "Invalid");
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        // No specific functionality to test in changed() method
        
    }
}