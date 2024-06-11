
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }
    
    @Test
    public void testCreateMenu() {
        // Test creating a menu
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test adding a shortcut and icon
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Open");
        assertNotNull(item.getAccelerator());
    }
    
    @Test
    public void testChanged() {
        // Test the "changed" method
        mainMenu.changed();
        // No specific assert as the method does not have any visible side effects
    }
}
