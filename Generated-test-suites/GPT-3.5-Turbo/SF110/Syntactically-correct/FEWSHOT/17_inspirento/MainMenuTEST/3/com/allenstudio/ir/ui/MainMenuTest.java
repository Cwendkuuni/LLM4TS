import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private InspirentoMediator mediator;
    private MainMenu mainMenu;
    
    @Before
    public void setup() {
        mediator = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(mediator, mainMenu.mediator);
    }
    
    @Test
    public void testCreateMenu() {
        // TODO: Implement test cases for createMenu method
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        // TODO: Implement test cases for addShortcutAndIcon method
    }
    
    @Test
    public void testChanged() {
        // TODO: Implement test case for changed method
    }
}