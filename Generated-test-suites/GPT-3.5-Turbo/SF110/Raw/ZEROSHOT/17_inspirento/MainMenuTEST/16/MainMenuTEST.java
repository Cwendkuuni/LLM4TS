import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menu");
        
        assertEquals("File", menu.getText());
        assertEquals(0, menu.getMenuComponentCount());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(item, "file");
        
        assertNull(item.getAccelerator());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        
        mainMenu.changed();
        
        // Test does not have any assertions as it only tests a void method
    }
}