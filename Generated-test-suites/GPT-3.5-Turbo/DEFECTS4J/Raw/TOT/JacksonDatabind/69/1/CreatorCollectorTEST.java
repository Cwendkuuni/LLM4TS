import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[0]);
        
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(defaultCreator);

        assertNotNull(creatorCollector._creators[0]);
        assertEquals(defaultCreator, creatorCollector._creators[0]);
    }
    
    @Test
    public void testStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[1]);
        
        AnnotatedWithParams stringCreator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(stringCreator, false);

        assertNotNull(creatorCollector._creators[1]);
        assertEquals(stringCreator, creatorCollector._creators[1]);
    }

    @Test
    public void testIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[2]);
        
        AnnotatedWithParams intCreator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(intCreator, false);

        assertNotNull(creatorCollector._creators[2]);
        assertEquals(intCreator, creatorCollector._creators[2]);
    }
    
    @Test
    public void testLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[3]);
        
        AnnotatedWithParams longCreator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(longCreator, false);

        assertNotNull(creatorCollector._creators[3]);
        assertEquals(longCreator, creatorCollector._creators[3]);
    }
    
    @Test
    public void testDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[4]);
        
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(doubleCreator, false);

        assertNotNull(creatorCollector._creators[4]);
        assertEquals(doubleCreator, creatorCollector._creators[4]);
    }
    
    @Test
    public void testBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[5]);
        
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(booleanCreator, false);

        assertNotNull(creatorCollector._creators[5]);
        assertEquals(booleanCreator, creatorCollector._creators[5]);
    }

    @Test
    public void testDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[6]);
        
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams();
        creatorCollector.addDelegatingCreator(delegatingCreator, false, null);

        assertNotNull(creatorCollector._creators[6]);
        assertEquals(delegatingCreator, creatorCollector._creators[6]);
    }
    
    @Test
    public void testPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._creators[7]);
        
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams();
        creatorCollector.addPropertyCreator(propertyCreator, false, null);

        assertNotNull(creatorCollector._creators[7]);
        assertEquals(propertyCreator, creatorCollector._creators[7]);
    }
    
    @Test
    public void testIncompleteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertNull(creatorCollector._incompleteParameter);
        
        AnnotatedParameter incompleteParameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(incompleteParameter);

        assertNotNull(creatorCollector._incompleteParameter);
        assertEquals(incompleteParameter, creatorCollector._incompleteParameter);
    }
}