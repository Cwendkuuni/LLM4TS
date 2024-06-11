import junit.framework.TestCase;

public class CreatorCollectorTest extends TestCase {

    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        assertNotNull(creatorCollector);
        assertEquals(beanDesc, creatorCollector._beanDesc);
        assertEquals(canFixAccess, creatorCollector._canFixAccess);
        assertNotNull(creatorCollector._creators);
        assertEquals(8, creatorCollector._creators.length);
        assertEquals(0, creatorCollector._explicitCreators);
        assertFalse(creatorCollector._hasNonDefaultCreator);
        assertNull(creatorCollector._delegateArgs);
        assertNull(creatorCollector._propertyBasedArgs);
        assertNull(creatorCollector._incompleteParameter);
    }

    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.setDefaultCreator(creator);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addStringCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addIntCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addLongCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addDoubleCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addBooleanCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        CreatorProperty[] injectables = new CreatorProperty[]{new CreatorProperty()};

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addDelegatingCreator(creator, explicit, injectables);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        CreatorProperty[] properties = new CreatorProperty[]{new CreatorProperty()};

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addPropertyCreator(creator, explicit, properties);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedParameter parameter = new AnnotatedParameter();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    public void testHasDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        AnnotatedWithParams creator = new AnnotatedWithParams();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        assertFalse(creatorCollector.hasDefaultCreator());

        creatorCollector.setDefaultCreator(creator);

        assertTrue(creatorCollector.hasDefaultCreator());
    }
}