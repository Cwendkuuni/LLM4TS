import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        JMenu result = mainMenu.createMenu("menu");
        
        assertNotNull(result);
        assertEquals(3, result.getMenuComponentCount());
        assertEquals("File", result.getText());
        
        JMenuItem fileNew = (JMenuItem) result.getMenuComponent(0);
        assertEquals("New", fileNew.getText());
        assertNull(fileNew.getIcon());
        assertNull(fileNew.getAccelerator());
        
        JMenuItem fileOpen = (JMenuItem) result.getMenuComponent(1);
        assertEquals("Open", fileOpen.getText());
        assertNull(fileOpen.getIcon());
        assertNull(fileOpen.getAccelerator());
        
        JMenuItem fileExit = (JMenuItem) result.getMenuComponent(2);
        assertEquals("Exit", fileExit.getText());
        assertNull(fileExit.getIcon());
        assertNull(fileExit.getAccelerator());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(item, "menuItem");
        
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
}