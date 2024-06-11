import static org.junit.Assert.*;
import org.junit.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(mediator);
        JMenu jMenu = menu.createMenu("menu");
        
        assertNotNull(jMenu);
        
        // Add test cases to assert the behavior of createMenu() method
        // ...
        // ...
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(mediator);
        JMenuItem item = new JMenuItem();
        
        // Add test cases to assert the behavior of addShortcutAndIcon() method
        // ...
        // ...
    }
    
    // Add more test methods to cover all the methods in MainMenu class
    
}