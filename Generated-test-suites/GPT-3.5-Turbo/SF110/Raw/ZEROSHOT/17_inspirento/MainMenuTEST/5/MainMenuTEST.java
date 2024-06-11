import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.lang.reflect.*;
import java.util.MissingResourceException;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }
    
    @Test
    public void testCreateMenu() {
        String menuString = "file";
        JMenu menu = mainMenu.createMenu(menuString);
        
        assertNotNull(menu);
        assertEquals("File", menu.getText());
        assertEquals(KeyEvent.VK_UNDEFINED, menu.getMnemonic());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "open";
        
        mainMenu.addShortcutAndIcon(item, key);
        
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        mainMenu.changed();
    }
}