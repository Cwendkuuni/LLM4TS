import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        // Add more assertions as needed
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("File");
        JMenuItem item = new JMenuItem("TestItem");
        mainMenu.addShortcutAndIcon(item, "TestItem");
        // Add assertions to check if shortcut and icon are set correctly
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        // Add test cases for the changed method, if needed
    }
}