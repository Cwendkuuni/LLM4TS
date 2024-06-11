import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        // Test case for setDefaultCreator method
        
        // Given
        CreatorCollector collector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        collector.setDefaultCreator(creator);

        // Then
        assertEquals(creator, collector.getDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        // Test case for addStringCreator method
        
        // Given
        CreatorCollector collector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        collector.addStringCreator(creator, true);

        // Then
        assertEquals(creator, collector.getStringCreator());
    }

    @Test
    public void testAddIntCreator() {
        // Test case for addIntCreator method
        
        // Given
        CreatorCollector collector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();

        // When
        collector.addIntCreator(creator, true);

        // Then
        assertEquals(creator, collector.getIntCreator());
    }

    // Other test cases for each method...
    // ...

}