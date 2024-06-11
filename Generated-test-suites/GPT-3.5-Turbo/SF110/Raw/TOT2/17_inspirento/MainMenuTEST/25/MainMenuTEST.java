import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void testCreateMenu() {
        assertNotNull(mainMenu.createMenu("File"));
        assertNotNull(mainMenu.createMenu("Edit"));
        assertNotNull(mainMenu.createMenu("Help"));
    }

    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem item = new JMenuItem("TestItem");
        mainMenu.addShortcutAndIcon(item, "Test");
        assertNotNull(item.getAccelerator());
        // Additional test cases for edge cases and exceptions can be added here
    }

    // Additional test cases for other public methods can be added here

}