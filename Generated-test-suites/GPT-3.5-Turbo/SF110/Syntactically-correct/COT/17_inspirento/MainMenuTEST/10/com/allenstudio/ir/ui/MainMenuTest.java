
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }
    
    // Test createMenu method
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("menu");
        assertNotNull(menu);
    }
    
    // Test addShortcutAndIcon method
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "key");
        assertNotNull(item.getAccelerator());
    }
    
    // Test changed method
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.changed();
        // No exception should occur
    }
}

