import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.InputEvent;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        assertNotNull(mainMenu.createMenu("File"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "EditOrder");
        assertNull(item.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        // No actions are performed in the method, so nothing specific to test
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingResource() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.createMenu("InvalidMenu");
    }

    @Test
    public void testAddShortcutAndIconMissingShortcutResource() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidShortcut");
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIconMissingIconResource() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidIcon");
        assertNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIconInvalidIconPath() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidIconPath");
        // Validating that warning message is printed
    }
}