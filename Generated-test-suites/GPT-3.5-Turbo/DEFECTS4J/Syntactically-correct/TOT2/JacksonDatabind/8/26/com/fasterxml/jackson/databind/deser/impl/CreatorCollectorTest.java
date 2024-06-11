import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testDefaultValueInstantiator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNotNull(creatorCollector.constructValueInstantiator(null));
    }

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(null, false);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(null, false, null);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector);
    }
}