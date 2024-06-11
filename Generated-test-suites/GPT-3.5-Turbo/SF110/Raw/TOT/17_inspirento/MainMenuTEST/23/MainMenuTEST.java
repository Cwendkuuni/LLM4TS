import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.event.KeyEvent;

public class MainMenuTest {

    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }
    
    @Test
    public void testCreateMenu_withValidMenu_returnsJMenu() {
        String menu = "Edit";
        JMenu result = mainMenu.createMenu(menu);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateMenu_withInvalidMenu_returnsNull() {
        String menu = "InvalidMenu";
        JMenu result = mainMenu.createMenu(menu);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateMenu_withMenuLabelContainingAsterisk_returnsJMenuWithCorrectLabel() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        
        assertEquals("MainMenu label", result.getText());
    }
    
    @Test
    public void testCreateMenu_withMenuLabelNotContainingAsterisk_returnsJMenuWithCorrectLabel() {
        String menu = "Help";
        JMenu result = mainMenu.createMenu(menu);
        
        assertEquals("Help", result.getText());
    }
    
    @Test
    public void testCreateMenu_withSubMenu_returnsJMenuWithSubMenu() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        
        assertEquals(2, result.getItemCount());
    }
    
    @Test
    public void testCreateMenu_withInvalidSubMenu_returnsJMenuWithoutSubMenu() {
        String menu = "InvalidSubMenu";
        JMenu result = mainMenu.createMenu(menu);
        
        assertEquals(0, result.getItemCount());
    }
    
    @Test
    public void testAddShortcutAndIcon_withValidKey_setsAcceleratorAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "fileMenuSave";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_withInvalidKey_doesNotSetAcceleratorAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "invalidKey";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged_doesNothing() {
        mainMenu.changed();
        
        // No assertion needed, just to test the method
    }
}