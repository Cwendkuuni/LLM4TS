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
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);

        assertEquals(creator, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);

        assertEquals(creator, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);

        assertEquals(creator, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);

        assertEquals(creator, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);

        assertEquals(creator, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[3];
        creatorCollector.addDelegatingCreator(creator, false, injectables);

        assertEquals(creator, creatorCollector._creators[6]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[3];
        creatorCollector.addPropertyCreator(creator, false, properties);

        assertEquals(creator, creatorCollector._creators[7]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}