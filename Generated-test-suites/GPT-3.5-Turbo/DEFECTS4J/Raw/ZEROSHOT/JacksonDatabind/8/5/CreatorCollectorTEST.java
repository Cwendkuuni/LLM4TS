import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector = new CreatorCollector(null, true);

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = null;
        creatorCollector.setDefaultCreator(creator);
        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addStringCreator(creator, explicit);
        assertNotNull(creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addIntCreator(creator, explicit);
        assertNotNull(creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addLongCreator(creator, explicit);
        assertNotNull(creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addDoubleCreator(creator, explicit);
        assertNotNull(creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addBooleanCreator(creator, explicit);
        assertNotNull(creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorProperty[] injectables = null;
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        assertNotNull(creatorCollector._creators[6]);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorProperty[] properties = null;
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        assertNotNull(creatorCollector._creators[7]);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = null;
        creatorCollector.addIncompeteParameter(parameter);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
}