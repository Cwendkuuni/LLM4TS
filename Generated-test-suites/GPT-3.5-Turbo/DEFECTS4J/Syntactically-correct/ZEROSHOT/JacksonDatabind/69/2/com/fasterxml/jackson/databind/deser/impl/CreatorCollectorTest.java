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
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDelegatingCreator(creator, false, null);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addPropertyCreator(creator, false, null);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
}