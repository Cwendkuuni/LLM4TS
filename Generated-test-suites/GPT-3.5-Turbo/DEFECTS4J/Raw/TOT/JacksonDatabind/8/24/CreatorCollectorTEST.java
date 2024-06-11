import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector(null, false);
    }

    // Test case for setDefaultCreator method
    @Test
    public void testSetDefaultCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.setDefaultCreator(creator);

        // Then
        assertEquals(creator, creatorCollector._creators[0]);
    }

    // Test case for addStringCreator method
    @Test
    public void testAddStringCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.addStringCreator(creator, true);

        // Then
        assertEquals(creator, creatorCollector._creators[1]);
    }

    // Test case for addIntCreator method
    @Test
    public void testAddIntCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.addIntCreator(creator, true);

        // Then
        assertEquals(creator, creatorCollector._creators[2]);
    }

    // Test case for addLongCreator method
    @Test
    public void testAddLongCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.addLongCreator(creator, true);

        // Then
        assertEquals(creator, creatorCollector._creators[3]);
    }

    // Test case for addDoubleCreator method
    @Test
    public void testAddDoubleCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.addDoubleCreator(creator, true);

        // Then
        assertEquals(creator, creatorCollector._creators[4]);
    }

    // Test case for addBooleanCreator method
    @Test
    public void testAddBooleanCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        creatorCollector.addBooleanCreator(creator, true);

        // Then
        assertEquals(creator, creatorCollector._creators[5]);
    }

    // Test case for addDelegatingCreator method
    @Test
    public void testAddDelegatingCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];

        // When
        creatorCollector.addDelegatingCreator(creator, true, injectables);

        // Then
        assertEquals(creator, creatorCollector._creators[6]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    // Test case for addPropertyCreator method
    @Test
    public void testAddPropertyCreator() {
        // Given
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];

        // When
        creatorCollector.addPropertyCreator(creator, true, properties);

        // Then
        assertEquals(creator, creatorCollector._creators[7]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    // Test case for addIncompeteParameter method
    @Test
    public void testAddIncompeteParameter() {
        // Given
        AnnotatedParameter parameter = new AnnotatedParameter();

        // When
        creatorCollector.addIncompeteParameter(parameter);

        // Then
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

}