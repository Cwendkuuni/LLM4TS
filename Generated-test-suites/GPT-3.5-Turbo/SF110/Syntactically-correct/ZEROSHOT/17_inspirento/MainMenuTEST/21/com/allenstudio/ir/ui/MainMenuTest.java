import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // test createMenu with valid menu string
        JMenu jMenu = mainMenu.createMenu("menu1");
        assertNotNull(jMenu);
        
        // test createMenu with invalid menu string
        JMenu invalidMenu = mainMenu.createMenu("invalidMenu");
        assertNull(invalidMenu);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        // test addShortcutAndIcon with valid key
        mainMenu.addShortcutAndIcon(item, "key1");
        assertNotNull(item.getAccelerator());
        
        // test addShortcutAndIcon with invalid key
        mainMenu.addShortcutAndIcon(item, "invalidKey");
        assertNull(item.getAccelerator());
        
        // test addShortcutAndIcon with valid key and invalid icon path
        mainMenu.addShortcutAndIcon(item, "key2");
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        
        // test changed method
        mainMenu.changed();
        // no assertion needed as method does not return any value
    }
}