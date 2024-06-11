import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        // Test menu creation
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test adding shortcut and icon to menu item
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "item");
        assertNotNull(menuItem.getAccelerator());
        assertNotNull(menuItem.getIcon());
    }

    @Test
    public void testChanged() {
        // Test changed method
        mainMenu.changed();
        // Add assertions here
    }
}