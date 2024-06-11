import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "Open");
        assertNotNull(item.getAccelerator());

        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "UnknownShortcut");
        // No exception is thrown for missing resource
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        // No functionality to test in this method
    }
}