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
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        assertNotNull(result);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "New";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
    }
    
    @Test
    public void testChanged() {
        mainMenu.changed();
        // no assertions, method does not return a value
    }
}