import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    // Test case for setDefaultCreator method
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    // Test case for addStringCreator method
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    // Test case for addIntCreator method
    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    // Test case for addLongCreator method
    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    // Test case for addDoubleCreator method
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    // Test case for addBooleanCreator method
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    // Test case for addDelegatingCreator method
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }
    
    // Test case for addPropertyCreator method
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }
    
    // Test case for addIncompeteParameter method
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}