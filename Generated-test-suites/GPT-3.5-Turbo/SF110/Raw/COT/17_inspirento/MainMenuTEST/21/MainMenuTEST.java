import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }
    
    @Test
    public void testCreateMenu() {
        // Test menu creation
        JMenu menu = mainMenu.createMenu("SomeMenu");
        
        assertNotNull(menu);
        assertEquals("SomeMenu", menu.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test adding shortcut and icon to menu item
        JMenuItem item = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(item, "SomeKey");
        
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        // Test changed method
        mainMenu.changed();
        
        // No assertion, just test if the method runs without errors
    }
}