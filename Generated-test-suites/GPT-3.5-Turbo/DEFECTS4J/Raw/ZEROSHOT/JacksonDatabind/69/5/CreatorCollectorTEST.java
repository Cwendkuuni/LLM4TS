import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testGetDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector._incompleteParameter);
    }
}