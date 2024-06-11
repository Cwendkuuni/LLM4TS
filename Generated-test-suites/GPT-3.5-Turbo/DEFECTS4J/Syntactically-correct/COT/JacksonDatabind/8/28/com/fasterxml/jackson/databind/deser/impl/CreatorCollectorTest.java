
import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.setDefaultCreator(null);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addStringCreator(null, false);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addIntCreator(null, false);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addLongCreator(null, false);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addDoubleCreator(null, false);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addBooleanCreator(null, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addDelegatingCreator(null, false, null);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addPropertyCreator(null, false, null);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        creatorCollector.addIncompeteParameter(null);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        // Test method functionality
        boolean result = creatorCollector.hasDefaultCreator();
        assertEquals(false, result);
    }

}
