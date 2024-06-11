import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import javax.swing.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertEquals("File", menu.getText());
    }

    @Test
    public void testCreateMenu_Submenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertEquals(2, menu.getItemCount());
        assertTrue(menu.getItem(1) instanceof JMenu);
        assertEquals("Open", ((JMenu) menu.getItem(1)).getText());
    }

    @Test
    public void testCreateMenu_Item() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertEquals(3, menu.getItemCount());
        assertTrue(menu.getItem(2) instanceof JMenuItem);
        assertEquals("Save", ((JMenuItem) menu.getItem(2)).getText());
    }

    @Test
    public void testCreateMenu_Separator() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertEquals(4, menu.getItemCount());
        assertTrue(menu.getItem(3) instanceof JSeparator);
    }

    @Test
    public void testAddShortcutAndIcon_Shortcut() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Open");
        assertNotNull(item.getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK), item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_Icon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Open");
        assertNotNull(item.getIcon());
        assertEquals(new ImageIcon(this.getClass().getResource("/icons/open.png")), item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_NoShortcut() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Save");
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_NoIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Save");
        assertNull(item.getIcon());
    }
}