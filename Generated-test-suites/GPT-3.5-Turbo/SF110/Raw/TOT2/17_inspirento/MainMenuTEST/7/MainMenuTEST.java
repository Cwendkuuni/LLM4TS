import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainMenuTest {

    MainMenu mainMenu;

    @Test
    public void testCreateMenu() {
        mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        mainMenu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "File");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testChanged() {
        mainMenu = new MainMenu(null);
        // Testing that the changed method does not throw any exceptions
        assertDoesNotThrow(() -> mainMenu.changed());
    }
}