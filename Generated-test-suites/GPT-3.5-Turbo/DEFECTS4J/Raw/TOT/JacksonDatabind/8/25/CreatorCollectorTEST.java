import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector(new BeanDescription(), true);
    }

    @Test
    public void testSetDefaultCreator() {
        // Typical use case: Setting a default creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        Assert.assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        // Typical use case: Adding a string creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);

        Assert.assertEquals(creator, creatorCollector._creators[1]);
    }

    @Test
    public void testAddStringCreator_duplicate() {
        // Error scenario: Adding a duplicate string creator
        AnnotatedWithParams creator1 = new AnnotatedWithParams();
        AnnotatedWithParams creator2 = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator1, true);
        creatorCollector.addStringCreator(creator2, false);

        Assert.assertEquals(creator1, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        // Typical use case: Adding an int creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);

        Assert.assertEquals(creator, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        // Typical use case: Adding a long creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);

        Assert.assertEquals(creator, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        // Typical use case: Adding a double creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);

        Assert.assertEquals(creator, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        // Typical use case: Adding a boolean creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);

        Assert.assertEquals(creator, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        // Typical use case: Adding a delegating creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[2];
        creatorCollector.addDelegatingCreator(creator, true, properties);

        Assert.assertEquals(creator, creatorCollector._creators[6]);
        Assert.assertEquals(properties, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        // Typical use case: Adding a property creator
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[2];
        creatorCollector.addPropertyCreator(creator, true, properties);

        Assert.assertEquals(creator, creatorCollector._creators[7]);
        Assert.assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        // Typical use case: Adding an incomplete parameter
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);

        Assert.assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        // Typical use case: Checking if there is a default creator
        Assert.assertFalse(creatorCollector.hasDefaultCreator());

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }
}