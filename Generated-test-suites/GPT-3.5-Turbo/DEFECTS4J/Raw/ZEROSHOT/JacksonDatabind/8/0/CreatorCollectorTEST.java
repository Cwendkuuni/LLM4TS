import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, true);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = null;
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addStringCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addIntCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addLongCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addDoubleCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addBooleanCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorProperty[] injectables = null;
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorProperty[] properties = null;
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = null;
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
    }

}