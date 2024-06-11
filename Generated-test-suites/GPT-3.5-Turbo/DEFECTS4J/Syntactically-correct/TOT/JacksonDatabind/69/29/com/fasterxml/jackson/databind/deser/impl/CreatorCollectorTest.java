
import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);

        assertNotNull(creatorCollector);
    }

    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        DeserializationConfig config = new DeserializationConfig();

        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);

        assertNotNull(valueInstantiator);
    }

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();

        creatorCollector.setDefaultCreator(creator);

        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        creatorCollector.addStringCreator(creator, explicit);

        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        creatorCollector.addIntCreator(creator, explicit);

        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        creatorCollector.addLongCreator(creator, explicit);

        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        creatorCollector.addDoubleCreator(creator, explicit);

        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        creatorCollector.addBooleanCreator(creator, explicit);

        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];

        creatorCollector.addDelegatingCreator(creator, explicit, injectables);

        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        SettableBeanProperty[] properties = new SettableBeanProperty[1];

        creatorCollector.addPropertyCreator(creator, explicit, properties);

        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedParameter parameter = new AnnotatedParameter();

        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector.getIncompleteParameter());
    }

}
