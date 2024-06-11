
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");

        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "File_Save");

        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    // Edge Cases and Exception Handling
    @Test
    public void testCreateMenuWithEmptyLabel() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("");

        assertNotNull(menu);
        assertEquals("", menu.getText());
    }

    @Test
    public void testCreateMenuWithInvalidLabel() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("InvalidLabel");

        assertNotNull(menu);
        assertEquals("InvalidLabel", menu.getText());
    }

    @Test
    public void testAddShortcutAndIconWithEmptyKey() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "");

        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIconWithInvalidKey() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidKey");

        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }
}

