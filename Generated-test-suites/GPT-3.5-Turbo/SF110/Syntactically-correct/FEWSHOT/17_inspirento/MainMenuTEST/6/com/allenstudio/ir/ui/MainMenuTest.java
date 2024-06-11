import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void testCreateMenu_ValidMenu_ReturnsJMenu() {
        JMenu menu = mainMenu.createMenu("Menu");
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu_InvalidMenu_ReturnsNull() {
        JMenu menu = mainMenu.createMenu("InvalidMenu");
        assertNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon_ValidItem_AddsShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Item");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_InvalidShortcut_DoesNotAddShortcut() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidShortcut");
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon_InvalidIcon_DoesNotAddIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidIcon");
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        // No assertions required as method has no implementation
        mainMenu.changed();
    }
}