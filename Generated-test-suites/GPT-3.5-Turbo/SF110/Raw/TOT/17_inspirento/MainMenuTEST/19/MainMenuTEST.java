import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testCreateMenu_InvalidMenu_MissingResourceException() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        Exception exception = assertThrows(MissingResourceException.class, () -> {
            mainMenu.createMenu("InvalidMenu");
        });
        String expectedMessage = "Cannot load certain menu label(s).";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testCreateMenu_ValidMenu_MenusCreatedSuccessfully() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu1 = mainMenu.createMenu("Menu1");
        assertNotNull(menu1);
        JMenu menu2 = mainMenu.createMenu("Menu2");
        assertNotNull(menu2);
        // Assert other menu creations
    }

    @Test
    public void testAddShortcutAndIcon_ValidShortcut_ShortcutAddedSuccessfully() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "Item1");
        KeyStroke expectedShortcut = KeyStroke.getKeyStroke("ctrl T");
        assertEquals(expectedShortcut, item1.getAccelerator());
        // Assert other valid shortcuts
    }

    @Test
    public void testAddShortcutAndIcon_ValidIcon_IconAddedSuccessfully() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "Item1");
        // Assert the icon is added successfully
    }

    @Test
    public void testAddShortcutAndIcon_InvalidShortcut_MissingResourceException() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item1 = new JMenuItem();
        Exception exception = assertThrows(MissingResourceException.class, () -> {
            mainMenu.addShortcutAndIcon(item1, "InvalidShortcut");
        });
        String expectedMessage = "No resource found for key 'InvalidShortcutShortcut'";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testAddShortcutAndIcon_InvalidIcon_MissingResourceException() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item1 = new JMenuItem();
        Exception exception = assertThrows(MissingResourceException.class, () -> {
            mainMenu.addShortcutAndIcon(item1, "InvalidIcon");
        });
        String expectedMessage = "No resource found for key 'InvalidIconIcon'";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    // Add test cases for other methods

}