import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testSetDefaultCreator() {
        // Create a default creator
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(defaultCreator);

        // Verify that the default creator is set correctly
        assertEquals(defaultCreator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        // Create a string creator
        AnnotatedWithParams stringCreator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(stringCreator, false);

        // Verify that the string creator is set correctly
        assertEquals(stringCreator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    // Add test cases for other methods here...

}