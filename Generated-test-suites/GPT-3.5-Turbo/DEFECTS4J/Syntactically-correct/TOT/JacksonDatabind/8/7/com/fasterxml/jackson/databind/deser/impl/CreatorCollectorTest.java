import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        // Create a new instance of CreatorCollector for each test method
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testSetDefaultCreator() {
        // Test case for setDefaultCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.setDefaultCreator(creator);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        // Test case for addStringCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.addStringCreator(creator, true);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        // Test case for addIntCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.addIntCreator(creator, true);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        // Test case for addLongCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.addLongCreator(creator, true);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        // Test case for addDoubleCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.addDoubleCreator(creator, true);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        // Test case for addBooleanCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // Act
        creatorCollector.addBooleanCreator(creator, true);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        // Test case for addDelegatingCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[2];

        // Act
        creatorCollector.addDelegatingCreator(creator, true, injectables);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        // Test case for addPropertyCreator method

        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[2];

        // Act
        creatorCollector.addPropertyCreator(creator, true, properties);

        // Assert
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        // Test case for addIncompeteParameter method

        // Arrange
        AnnotatedParameter parameter = new AnnotatedParameter();

        // Act
        creatorCollector.addIncompeteParameter(parameter);

        // Assert
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}