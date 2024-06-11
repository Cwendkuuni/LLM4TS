import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    
    @Before
    public void setUp() {
        beanDescription = new BeanDescription();
        creatorCollector = new CreatorCollector(beanDescription, true);
    }
    
    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        
        assertEquals(beanDescription.getType(), instantiator.getType());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
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
        CreatorProperty[] injectables = new CreatorProperty[5];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[5];
        creatorCollector.addPropertyCreator(creator, false, properties);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

}