import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private boolean canFixAccess;

    @Before
    public void setUp() {
        beanDescription = new BeanDescription();
        canFixAccess = true;
        creatorCollector = new CreatorCollector(beanDescription, canFixAccess);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(annotatedWithParams);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        boolean explicit = false;
        creatorCollector.addStringCreator(annotatedWithParams, explicit);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        boolean explicit = false;
        creatorCollector.addIntCreator(annotatedWithParams, explicit);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        boolean explicit = false;
        creatorCollector.addLongCreator(annotatedWithParams, explicit);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        boolean explicit = false;
        creatorCollector.addDoubleCreator(annotatedWithParams, explicit);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        boolean explicit = false;
        creatorCollector.addBooleanCreator(annotatedWithParams, explicit);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[0];
        boolean explicit = false;
        creatorCollector.addDelegatingCreator(annotatedWithParams, explicit, injectables);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[6]);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[0];
        boolean explicit = false;
        creatorCollector.addPropertyCreator(annotatedWithParams, explicit, properties);
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertNotNull(creators);
        assertEquals(1, creators.length);
        assertEquals(annotatedWithParams, creators[7]);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(annotatedParameter);
        AnnotatedParameter incompleteParameter = creatorCollector.getIncompleteParameter();
        assertNotNull(incompleteParameter);
        assertEquals(annotatedParameter, incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(annotatedWithParams);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        assertNotNull(valueInstantiator);
        // additional assertions can be added
    }
}