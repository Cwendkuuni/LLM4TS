import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {
    
    @Test
    public void testSetDefaultCreator() {
        // Test setting default creator
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedConstructor(null, null, null);
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        // Test adding string creator
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedMethod(null, null, null);
        creatorCollector.addStringCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testAddIntCreator() {
        // Test adding int creator
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedConstructor(null, null, null);
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    // Additional test cases for other methods can be added here
    
}