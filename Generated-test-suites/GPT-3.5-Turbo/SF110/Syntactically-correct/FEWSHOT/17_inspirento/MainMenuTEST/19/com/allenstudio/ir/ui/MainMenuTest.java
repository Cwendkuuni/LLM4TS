import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        // Test when menu is null
        assertNull(mainMenu.createMenu(null));

        // Test when menu label length is less than 2
        assertNull(mainMenu.createMenu("a"));

        // Test when menu label length is greater than 2 and ends with '*'
        JMenu jMenu = mainMenu.createMenu("ab*");
        assertNotNull(jMenu);
        assertEquals("ab", jMenu.getText());
        assertEquals('*', jMenu.getMnemonic());

        // Test when menu label length is greater than 2 and does not end with '*'
        jMenu = mainMenu.createMenu("abc");
        assertNotNull(jMenu);
        assertEquals("abc", jMenu.getText());
        assertEquals('\u0000', jMenu.getMnemonic());

        // Test when item is null
        assertNull(mainMenu.createMenu("abc>"));

        // Test when item starts with '-' (separator)
        JMenu subMenu = mainMenu.createMenu("menu1");
        assertNotNull(subMenu);
        assertEquals(1, subMenu.getMenuComponentCount());
        assertTrue(subMenu.getMenuComponents()[0] instanceof JSeparator);

        // Test when item ends with '>' (sub menu)
        subMenu = mainMenu.createMenu("menu2>");
        assertNotNull(subMenu);
        assertEquals(1, subMenu.getMenuComponentCount());
        assertTrue(subMenu.getMenuComponents()[0] instanceof JMenu);

        // Test when item starts with '#' (checkbox menu item)
        subMenu = mainMenu.createMenu("#item1");
        assertNotNull(subMenu);
        assertEquals(1, subMenu.getMenuComponentCount());
        assertTrue(subMenu.getMenuComponents()[0] instanceof JCheckBoxMenuItem);

        // Test when item does not start with '#' (normal menu item)
        subMenu = mainMenu.createMenu("item2");
        assertNotNull(subMenu);
        assertEquals(1, subMenu.getMenuComponentCount());
        assertTrue(subMenu.getMenuComponents()[0] instanceof JMenuItem);
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test when key is null
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, null);
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        // Test when shortcut is missing
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key1");
        assertNull(item.getAccelerator());

        // Test when icon is missing
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key2");
        assertNull(item.getIcon());

        // Test when both shortcut and icon are present
        item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key3");
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        // Test if method does not throw an exception
        mainMenu.changed();
    }
}