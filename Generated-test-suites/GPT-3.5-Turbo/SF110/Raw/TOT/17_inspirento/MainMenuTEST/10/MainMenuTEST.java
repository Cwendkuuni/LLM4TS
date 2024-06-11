import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu_ValidMenu_ReturnsJMenuObject() {
        String menu = "File";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
        assertEquals(menu, result.getText());
    }
    
    @Test
    public void testCreateMenu_MenuWithLabelSuffix_ReturnsJMenuObjectWithFormattedText() {
        String menu = "File*F";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
        assertEquals("File", result.getText());
        assertEquals('F', result.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_Submenu_ReturnsJMenuObjectWithSubmenuItem() {
        String menu = "File>Open";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
        assertEquals(menu, result.getText());
        assertEquals(1, result.getMenuComponentCount());
    }
    
    @Test
    public void testCreateMenu_CheckboxMenuItem_ReturnsJCheckBoxMenuItemObject() {
        String menu = "#Settings";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
        assertEquals(menu, result.getText());
        assertEquals(JCheckBoxMenuItem.class, result.getClass());
    }
    
    @Test
    public void testAddShortcutAndIcon_ValidShortcutAndIconPath_SetMenuItemAcceleratorAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        mainMenu.addShortcutAndIcon(item, key);
        
        // Asserts that the accelerator is set correctly
        assertEquals(KeyStroke.getKeyStroke("ctrl F"), item.getAccelerator());
        // Asserts that the icon is set correctly
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_InvalidShortcut_NoAcceleratorSet() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidShortcut";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        mainMenu.addShortcutAndIcon(item, key);
        
        // Asserts that no accelerator is set
        assertNull(item.getAccelerator());
    }
    
    @Test
    public void testAddShortcutAndIcon_InvalidIconPath_NoIconSet() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidIcon";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        mainMenu.addShortcutAndIcon(item, key);
        
        // Asserts that no icon is set
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged_EmptyMethod_NoExceptionThrown() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        mainMenu.changed();
        
        // No assertions, only checks that no exception is thrown
    }
}