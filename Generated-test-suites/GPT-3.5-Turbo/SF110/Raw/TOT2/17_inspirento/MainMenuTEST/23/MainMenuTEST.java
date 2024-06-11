import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new MockInspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new MockInspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Save");
        
        assertNotNull(item.getAccelerator());
    }
    
    // Edge case: item key not found for shortcut
    @Test
    public void testAddShortcutAndIcon_missingShortcut() {
        MainMenu mainMenu = new MainMenu(new MockInspirentoMediator());
        JMenuItem item = new JMenuItem();
        
        assertDoesNotThrow(() -> mainMenu.addShortcutAndIcon(item, "NonExistentKey"));
    }
    
    // Edge case: item icon not found
    @Test
    public void testAddShortcutAndIcon_missingIcon() {
        MainMenu mainMenu = new MainMenu(new MockInspirentoMediator());
        JMenuItem item = new JMenuItem();
        
        assertDoesNotThrow(() -> mainMenu.addShortcutAndIcon(item, "NonExistentIcon"));
    }
    
    // Error scenario: Invalid menu key
    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_invalidKey() {
        MainMenu mainMenu = new MainMenu(new MockInspirentoMediator());
        mainMenu.createMenu("InvalidMenuKey");
    }

    // Mock class for InspirentoMediator
    private class MockInspirentoMediator implements InspirentoMediator {
        @Override
        public void notify(String message) {
            // Do nothing
        }
    }
}