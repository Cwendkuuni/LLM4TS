
import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testConstructorWithNullMediator() {
        try {
            MainMenu mainMenu = new MainMenu(null);
            // Assert statements
            fail("Expected exception not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithNonNullMediator() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        // Assert statements
        // ...
    }

    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.changed();
        // Assert statements
        // ...
    }

}