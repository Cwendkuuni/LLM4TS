import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }
    
    @After
    public void tearDown() {
        mainMenu = null;
    }
    
    @Test
    public void testCreateMenu_ValidMenu_ReturnsJMenu() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
        assertEquals("File", result.getText());
        assertEquals(0, result.getMenuComponentCount());
    }
    
    @Test
    public void testCreateMenu_InvalidMenu_ThrowsMissingResourceException() {
        String menu = "InvalidMenu";
        
        assertThrows(MissingResourceException.class, () -> {
            mainMenu.createMenu(menu);
        });
    }
    
    @Test
    public void testAddShortcutAndIcon_ValidKey_ItemHasShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "Save";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_InvalidKey_ItemHasNoShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidKey";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged_EmptyImplementation() {
        // No assertions needed, just testing if method runs without error
        mainMenu.changed();
    }
    
}