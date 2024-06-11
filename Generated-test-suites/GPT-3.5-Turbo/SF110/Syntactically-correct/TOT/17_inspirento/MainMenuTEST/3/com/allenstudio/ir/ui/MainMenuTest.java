import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(null);
        
        // Test creating a menu with valid label and mnemonic
        JMenu jMenu = menu.createMenu("File");
        assertEquals("File", jMenu.getText());
        assertEquals('F', jMenu.getMnemonic());

        // Test creating a menu with valid label and no mnemonic
        jMenu = menu.createMenu("Edit");
        assertEquals("Edit", jMenu.getText());
        assertEquals(0, jMenu.getMnemonic());

        // Test creating a menu with invalid label
        jMenu = menu.createMenu("InvalidMenu");
        assertNull(jMenu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        
        // Test adding a valid shortcut
        menu.addShortcutAndIcon(item, "File");
        assertNotNull(item.getAccelerator());

        // Test adding an invalid shortcut
        menu.addShortcutAndIcon(item, "InvalidShortcut");
        assertNull(item.getAccelerator());

        // Test adding a valid icon
        menu.addShortcutAndIcon(item, "File");
        assertNotNull(item.getIcon());

        // Test adding an invalid icon
        menu.addShortcutAndIcon(item, "InvalidIcon");
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu menu = new MainMenu(null);

        // Test the empty implementation of the changed() method
        menu.changed();
    }
}