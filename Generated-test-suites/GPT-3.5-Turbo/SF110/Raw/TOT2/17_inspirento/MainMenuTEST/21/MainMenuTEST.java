import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        assertNotNull(mainMenu.createMenu("File"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "menu");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        mainMenu.changed(); // method does not have any functionality, so just test that it runs without errors
    }

    @Test
    public void testExceptionMissingResource() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        try {
            mainMenu.createMenu("NonExistentMenu");
            fail("Expected a MissingResourceException to be thrown");
        } catch (MissingResourceException e) {
            // Exception successfully caught
        }
    }

    @Test
    public void testExceptionShortcutMalformed() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        JMenuItem item = new JMenuItem();
        try {
            mainMenu.addShortcutAndIcon(item, "MalformedShortcutMenu");
            fail("Expected a MalformedURLException to be thrown");
        } catch (MalformedURLException e) {
            // Exception successfully caught
        }
    }

    @Test
    public void testExceptionIconMissing() {
        MainMenu mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
        JMenuItem item = new JMenuItem();
        try {
            mainMenu.addShortcutAndIcon(item, "MissingIconMenu");
        } catch (MissingResourceException e) {
            // No icon found, should just print a warning
            // No need to fail the test
        }
    }
}