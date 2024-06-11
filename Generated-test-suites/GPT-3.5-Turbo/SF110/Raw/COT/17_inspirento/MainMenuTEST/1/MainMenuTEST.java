import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testConstructorWithNullMediator() {
        // Arrange
        InspirentoMediator mediator = null;

        // Act
        MainMenu mainMenu = new MainMenu(mediator);

        // Assert
        assertNotNull(mainMenu);
    }

    @Test
    public void testConstructorWithNonNullMediator() {
        // Arrange
        InspirentoMediator mediator = new InspirentoMediator();

        // Act
        MainMenu mainMenu = new MainMenu(mediator);

        // Assert
        assertNotNull(mainMenu);
    }

    @Test
    public void testChanged() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());

        // Act
        mainMenu.changed();

        // Assert
        // Add your assertions here based on the expected behavior of the method
    }
}