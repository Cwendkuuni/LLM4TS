import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    private InspirentoMediator mediator;
    
    @Before
    public void setUp() {
        mediator = Mockito.mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testMainMenuConstructorWithNullMediator() {
        MainMenu menu = new MainMenu(null);
        assertNotNull(menu);
        // Additional assertions for any expected behavior
    }
    
    @Test
    public void testMainMenuConstructorWithValidMediator() {
        assertNotNull(mainMenu);
        // Additional assertions for any expected behavior
    }
    
    @Test
    public void testChangedMethod() {
        // No specific testing required for changed() method
    }
}