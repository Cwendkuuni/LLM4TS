
import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        // Test creating a menu with a label containing '*' and mnemonic
        JMenu menu = mainMenu.createMenu("#file");
        assertEquals("file", menu.getText());
        assertEquals('f', menu.getMnemonic());
        
        // Test creating a menu without '*' and mnemonic
        menu = mainMenu.createMenu("edit");
        assertEquals("edit", menu.getText());
        assertEquals(0, menu.getMnemonic());
        
        // Test creating a menu with submenu
        menu = mainMenu.createMenu("file>new");
        assertEquals("new", menu.getText());
        assertEquals(0, menu.getMnemonic());
        
        // Test creating a menu with separator
        menu = mainMenu.createMenu("file>-");
        assertEquals("", menu.getText());
        assertEquals(0, menu.getMnemonic());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        // Test adding shortcut to JMenuItem
        mainMenu.addShortcutAndIcon(item, "fileNew");
        assertNotNull(item.getAccelerator());
        
        // Test adding icon to JMenuItem
        mainMenu.addShortcutAndIcon(item, "fileNew");
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        // Test changed method, no assertion since it doesn't return anything
        mainMenu.changed();
    }
    
}
