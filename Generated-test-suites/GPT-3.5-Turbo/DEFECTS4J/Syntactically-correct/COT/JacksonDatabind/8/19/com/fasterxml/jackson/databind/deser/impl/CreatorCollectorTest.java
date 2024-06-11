import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        assertNull(creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        assertNull(creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        assertNull(creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        assertNull(creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        assertNull(creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        assertNull(creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNull(creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertNull(creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        assertNull(creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertNull(creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        assertNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        assertFalse(creatorCollector.hasDefaultCreator());
    }
}