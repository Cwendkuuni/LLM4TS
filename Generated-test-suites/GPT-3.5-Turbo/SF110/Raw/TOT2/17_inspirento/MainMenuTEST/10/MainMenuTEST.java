import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu_validMenu() {
        // Typical use case
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        
        // Edge case: menu with special characters
        menu = mainMenu.createMenu("#Edit");
        assertNotNull(menu);
        
        // Error scenario: menu not found
        menu = mainMenu.createMenu("InvalidMenu");
        assertNull(menu);
    }
    
    @Test
    public void testAddShortcutAndIcon_validItem() {
        // Typical use case
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JCheckBoxMenuItem item = new JCheckBoxMenuItem();
        mainMenu.addShortcutAndIcon(item, "#Copy");
        
        // Edge case: item with no shortcut
        item = new JCheckBoxMenuItem();
        mainMenu.addShortcutAndIcon(item, "Paste");
        
        // Error scenario: icon not found
        item = new JCheckBoxMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidItem");
    }
    
    @Test
    public void testChanged() {
        // No functionality to test for the method changed()
    }
}