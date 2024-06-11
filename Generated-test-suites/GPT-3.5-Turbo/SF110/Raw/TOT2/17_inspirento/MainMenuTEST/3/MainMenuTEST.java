import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    // Test for MainMenu constructor
    @Test
    public void testMainMenuConstructor() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }
    
    // Test for createMenu method with valid menu name
    @Test
    public void testCreateMenuValid() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
    }
    
    // Test for createMenu method with invalid menu name
    @Test
    public void testCreateMenuInvalid() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("InvalidMenu");
        assertNull(menu);
    }
    
    // Test for addShortcutAndIcon method with valid key
    @Test
    public void testAddShortcutAndIconValid() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "File");
        assertNotNull(item.getIcon());
    }
    
    // Test for addShortcutAndIcon method with invalid key
    @Test
    public void testAddShortcutAndIconInvalid() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "InvalidKey");
        assertNull(item.getIcon());
    }
    
    // Test for changed method
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.changed();
        // No need to assert anything since the method does not return anything
    }
}