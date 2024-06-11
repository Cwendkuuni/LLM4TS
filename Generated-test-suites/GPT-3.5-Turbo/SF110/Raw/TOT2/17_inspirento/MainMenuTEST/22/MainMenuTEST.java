import static org.junit.Assert.*;
import org.junit.Test;
import java.util.MissingResourceException;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        assertNotNull(mainMenu.createMenu("File"));
        assertNotNull(mainMenu.createMenu("Edit"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "FileNew");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        // No specific functionality to test, just confirm method does not throw an exception
        mainMenu.changed();
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithInvalidMenuLabel() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.createMenu("InvalidMenuLabel");
    }

    @Test
    public void testAddShortcutAndIconWithMissingResource() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidShortcutKey");
        // No specific behavior to check, just confirm method does not throw an exception
    }

    @Test
    public void testAddShortcutAndIconWithMissingIconResource() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidIconKey");
        // No specific behavior to check, just confirm method does not throw an exception
    }
}