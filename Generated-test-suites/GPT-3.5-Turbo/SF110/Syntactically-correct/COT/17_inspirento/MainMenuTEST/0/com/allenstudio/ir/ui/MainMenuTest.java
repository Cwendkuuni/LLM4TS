
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // Test for normal menu creation
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
        
        // Test for menu with shortcut
        menu = mainMenu.createMenu("Edit");
        assertNotNull(menu);
        assertEquals("Edit", menu.getText());
        assertEquals('E', menu.getMnemonic());
        
        // Test for submenu
        menu = mainMenu.createMenu("Help");
        assertNotNull(menu);
        assertEquals("Help", menu.getText());
        assertEquals(2, menu.getMenuComponentCount());
        
        JMenuItem subMenuItem = (JMenuItem) menu.getMenuComponent(0);
        assertNotNull(subMenuItem);
        assertEquals("About", subMenuItem.getText());
        
        subMenuItem = (JMenuItem) menu.getMenuComponent(1);
        assertNotNull(subMenuItem);
        assertEquals("FAQ", subMenuItem.getText());
        
        // Test for submenu with shortcut
        menu = mainMenu.createMenu("Help-FAQ");
        assertNotNull(menu);
        assertEquals("FAQ", menu.getText());
        assertEquals('F', menu.getMnemonic());
        
        subMenuItem = (JMenuItem) menu.getMenuComponent(0);
        assertNotNull(subMenuItem);
        assertEquals("General Questions", subMenuItem.getText());
        
        subMenuItem = (JMenuItem) menu.getMenuComponent(1);
        assertNotNull(subMenuItem);
        assertEquals("Technical Questions", subMenuItem.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        
        // Test for normal menu item with shortcut
        mainMenu.addShortcutAndIcon(menuItem, "File-New");
        assertEquals(KeyStroke.getKeyStroke("ctrl N"), menuItem.getAccelerator());
        
        // Test for menu item without shortcut
        mainMenu.addShortcutAndIcon(menuItem, "File-Save");
        assertNull(menuItem.getAccelerator());
        
        // Test for menu item with icon
        mainMenu.addShortcutAndIcon(menuItem, "File-Open");
        assertNotNull(menuItem.getIcon());
    }
}
