import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        
        DeserializationConfig deserializationConfig = new DeserializationConfig();
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(deserializationConfig);
        
        assertNotNull(valueInstantiator);
    }
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.setDefaultCreator(creator);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addStringCreator(creator);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addIntCreator(creator);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addLongCreator(creator);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addDoubleCreator(creator);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addBooleanCreator(creator);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        SettableBeanProperty[] injectables = new SettableBeanProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedMethod creator = new AnnotatedMethod();
        SettableBeanProperty[] properties = new SettableBeanProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addPropertyCreator(creator, false, properties);
        
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        CreatorCollector creatorCollector = new CreatorCollector();
        
        creatorCollector.addIncompeteParameter(parameter);
        
        assertNotNull(creatorCollector.getIncompleteParameter());
    }
}