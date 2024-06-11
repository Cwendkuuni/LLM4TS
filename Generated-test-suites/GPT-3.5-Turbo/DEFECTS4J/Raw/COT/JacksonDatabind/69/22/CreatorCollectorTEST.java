import org.junit.Test;

public class CreatorCollectorTest {
    
    // Test constructor
    
    @Test
    public void testCreatorCollectorConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        assertNotNull(creatorCollector);
    }
    
    // Test setDefaultCreator method
    
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    // Test addStringCreator method
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    // Test addIntCreator method
    
    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    // Test addLongCreator method
    
    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    // Test addDoubleCreator method
    
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    // Test addBooleanCreator method
    
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    // Test addDelegatingCreator method
    
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[]{};
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }
    
    // Test addPropertyCreator method
    
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[]{};
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }
    
    // Test addIncompeteParameter method
    
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}