import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testSetDefaultCreator() {
        assertNull(creatorCollector._creators[CreatorCollector.C_DEFAULT]);
        creatorCollector.setDefaultCreator(null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_PROPS]);
    }

    @Test
    public void testAddIncompeteParameter() {
        assertNull(creatorCollector._incompleteParameter);
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
}