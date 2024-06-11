
import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        JMenu menu = mainMenu.createMenu("file");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
        assertEquals(2, menu.getItemCount());
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "file");
        assertNotNull(menuItem.getAccelerator());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        mainMenu.changed();
        //No assertions, simply testing that no exceptions are thrown
    }
}

