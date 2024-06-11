import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;
import java.awt.event.ActionEvent;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setup() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testCreateMenu() {
        String menu = "File";
        assertNotNull(mainMenu.createMenu(menu));
    }
    
    @Test
    public void testAddShortcutAndIconWithMissingShortcut() {
        JMenuItem item = new JMenuItem();
        String key = "MissingShortcut";
        mainMenu.addShortcutAndIcon(item, key);
        // No exception should be thrown even if shortcut is missing
    }

    @Test
    public void testAddShortcutAndIconWithMissingIcon() {
        JMenuItem item = new JMenuItem();
        String key = "MissingIcon";
        mainMenu.addShortcutAndIcon(item, key);
        // No exception should be thrown even if icon is missing
    }

    @Test
    public void testChanged() {
        // No functionality to test for changed method
    }
}