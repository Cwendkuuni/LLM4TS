
import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.MissingResourceException;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("Test Menu");
        assertEquals("Test Menu", menu.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "Test Key");
        assertNull(menuItem.getAccelerator());
        assertNull(menuItem.getIcon());
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.changed();
        // No assertion needed, just test that it doesn't throw any exception
    }
}

