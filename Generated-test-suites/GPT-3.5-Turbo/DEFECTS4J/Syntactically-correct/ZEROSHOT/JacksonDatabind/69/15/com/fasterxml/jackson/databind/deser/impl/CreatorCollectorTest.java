import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = null;
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addStringCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addIntCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addLongCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addDoubleCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addBooleanCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] injectables = null;
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        assertEquals(creator, creatorCollector._creators[6]);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] properties = null;
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        assertEquals(creator, creatorCollector._creators[7]);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = null;
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector._creators[0] = null;
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector._creators[0] = new AnnotatedWithParamsCreator();
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector._creators[6] = null;
        assertFalse(creatorCollector.hasDelegatingCreator());
        creatorCollector._creators[6] = new AnnotatedWithParamsCreator();
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector._creators[7] = null;
        assertFalse(creatorCollector.hasPropertyBasedCreator());
        creatorCollector._creators[7] = new AnnotatedWithParamsCreator();
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
}