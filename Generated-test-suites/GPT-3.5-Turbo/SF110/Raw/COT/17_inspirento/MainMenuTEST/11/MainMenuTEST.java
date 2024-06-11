import org.junit.Test;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenuWithSubMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("subMenu->subSubMenu");
        assertNotNull(menu);
        assertEquals("subSubMenu", menu.getText());
    }

    @Test
    public void testCreateMenuWithCheckboxItem() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menuWithCheckbox");
        JMenuItem item = menu.getMenuComponent(0);
        assertNotNull(item);
        assertTrue(item instanceof JCheckBoxMenuItem);
    }

    @Test
    public void testCreateMenuWithSeparator() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("menuWithSeparator");
        int itemCount = menu.getMenuComponentCount();
        assertEquals(3, itemCount);
    }

    @Test
    public void testAddShortcutAndIconWithShortcut() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "menuitemWithShortcut");
        KeyStroke accelerator = menuItem.getAccelerator();
        assertNotNull(accelerator);
    }

    @Test
    public void testAddShortcutAndIconWithIcon() throws MalformedURLException {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "menuitemWithIcon");
        Icon icon = menuItem.getIcon();
        assertNotNull(icon);
    }

    @Test
    public void testAddShortcutAndIconWithMissingResource() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "missingResource");
        // No exception should be thrown
    }
}

