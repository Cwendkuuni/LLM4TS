import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        InspirentoMediator mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu_TypicalMenu_ReturnsJMenu() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);

        // Verify that the returned JMenu has the correct label
        assertEquals(menu, result.getText());

        // Verify that the JMenu has the correct mnemonic
        assertEquals('F', result.getMnemonic());

        // Verify that the JMenu contains the expected menu items
        assertEquals(3, result.getItemCount());
        assertEquals("Open", result.getItem(0).getText());
        assertEquals("Save", result.getItem(1).getText());
        assertEquals("Exit", result.getItem(2).getText());
    }

    @Test
    public void testCreateMenu_MissingMenuLabel_ThrowsException() {
        String menu = "NonExistentMenu";

        // Verify that creating a menu with a missing label throws a MissingResourceException
        assertThrows(MissingResourceException.class, () -> mainMenu.createMenu(menu));
    }

    @Test
    public void testAddShortcutAndIcon_MenuItemWithShortcutAndIcon_SetsShortcutAndIcon() {
        String key = "File";
        JMenuItem item = new JMenuItem();

        mainMenu.addShortcutAndIcon(item, key);

        // Verify that the JMenuItem has the correct accelerator based on the key
        assertEquals(KeyStroke.getKeyStroke("ctrl O"), item.getAccelerator());

        // Verify that the JMenuItem has the correct icon based on the key
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_MenuItemWithoutShortcutAndIcon_DoesNotSetShortcutAndIcon() {
        String key = "NonExistentItem";
        JMenuItem item = new JMenuItem();

        mainMenu.addShortcutAndIcon(item, key);

        // Verify that the JMenuItem does not have any accelerator
        assertNull(item.getAccelerator());

        // Verify that the JMenuItem does not have any icon
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged_EmptyImplementation_NoActionTaken() {
        // Verify that the method does not perform any action
        mainMenu.changed();
    }
}