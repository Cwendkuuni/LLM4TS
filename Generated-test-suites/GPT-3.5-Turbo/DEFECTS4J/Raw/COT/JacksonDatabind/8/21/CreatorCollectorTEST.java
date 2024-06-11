
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNotNull("CreatorCollector instance is null", creatorCollector);
    }

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, false);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(null, false);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(null, false);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(null, false);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(null, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(null, false, null);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(null, false, null);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertFalse("Has default creator", creatorCollector.hasDefaultCreator());
    }
}
