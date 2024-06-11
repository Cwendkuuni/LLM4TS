import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testMainMenuConstructor() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        assertNotNull(mainMenu);
        // Add more assertions if needed
    }
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        // Add more assertions to test menu creation
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("Test");
        mainMenu.addShortcutAndIcon(item, "Test");
        // Add assertions to test shortcut and icon addition
    }
    
    // Add more test cases for other methods as needed
    
}