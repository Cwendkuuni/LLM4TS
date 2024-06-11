import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(mock(InspirentoMediator.class));
    }

    @Test
    public void testCreateMenu() {
        // Test case 1
        JMenu menu1 = mainMenu.createMenu("menu1");
        assertEquals("Menu 1", menu1.getLabel());
        assertEquals('M', menu1.getMnemonic());

        JMenuItem item1 = (JMenuItem) menu1.getMenuComponent(0);
        assertEquals("Item 1", item1.getText());
        assertNull(item1.getIcon());
        assertNull(item1.getAccelerator());

        JMenuItem item2 = (JMenuItem) menu1.getMenuComponent(1);
        assertEquals("Item 2", item2.getText());
        assertNull(item2.getIcon());
        assertNull(item2.getAccelerator());

        // Test case 2
        JMenu menu2 = mainMenu.createMenu("menu2");
        assertEquals("Menu 2", menu2.getLabel());
        assertEquals('N', menu2.getMnemonic());

        JMenuItem item3 = (JMenuItem) menu2.getMenuComponent(0);
        assertEquals("Item 3", item3.getText());
        assertNull(item3.getIcon());
        assertNull(item3.getAccelerator());

        JMenuItem item4 = (JMenuItem) menu2.getMenuComponent(1);
        assertEquals("Item 4", item4.getText());
        assertNull(item4.getIcon());
        assertNull(item4.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test case 1
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "item1");
        assertNull(item1.getAccelerator());
        assertNull(item1.getIcon());

        // Test case 2
        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "item2");
        assertEquals(KeyStroke.getKeyStroke("control shift F"), item2.getAccelerator());
        assertNull(item2.getIcon());

        // Test case 3
        JMenuItem item3 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item3, "item3");
        assertNull(item3.getAccelerator());
        assertNotNull(item3.getIcon());

        // Test case 4
        JMenuItem item4 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item4, "item4");
        assertEquals(KeyStroke.getKeyStroke("control F"), item4.getAccelerator());
        assertNotNull(item4.getIcon());
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertion needed, method does not return any value
    }
}