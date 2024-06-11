
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Test
    public void testCreateMenu() {
        // Test case for normal menu creation
        mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // Test case for adding shortcut and icon to menu item
        mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = new JMenu();
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Save");
        assertNotNull(item.getAccelerator());
    }
    
}
