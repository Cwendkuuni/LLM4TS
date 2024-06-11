import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {
    
    private CreatorCollector creatorCollector;
    private BeanDescription beanDesc;
    private MapperConfig mapperConfig;
    private DeserializationConfig config;
    
    @Before
    public void setup() {
        beanDesc = new BeanDescription();
        mapperConfig = new MapperConfig();
        creatorCollector = new CreatorCollector(beanDesc, mapperConfig);
        config = new DeserializationConfig();
    }
    
    @Test
    public void testConstructValueInstantiator() {
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        
        assertNotNull(valueInstantiator);
        assertTrue(valueInstantiator instanceof StdValueInstantiator);
    }
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
        
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        
        assertNotNull(creatorCollector._incompleteParameter);
    }
}