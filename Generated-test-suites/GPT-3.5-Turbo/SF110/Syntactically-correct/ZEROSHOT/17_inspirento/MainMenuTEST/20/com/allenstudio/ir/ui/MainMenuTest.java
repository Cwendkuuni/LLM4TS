import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {

    MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key");
        assertNotNull(item.getAccelerator());
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertion needed as the method does not return anything
    }

}