
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.setDefaultCreator(creator);
        assertEquals(creator, collector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.addStringCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.addIntCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_INT]);
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.addLongCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_LONG]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.addDoubleCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        collector.addBooleanCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, collector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector collector = new CreatorCollector();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, collector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, collector._propertyBasedArgs);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        CreatorCollector collector = new CreatorCollector();
        collector.addIncompeteParameter(parameter);
        assertEquals(parameter, collector._incompleteParameter);
    }
    
    @Test
    public void testHasDefaultCreator() {
        CreatorCollector collector = new CreatorCollector();
        assertFalse(collector.hasDefaultCreator());
        collector.setDefaultCreator(new AnnotatedWithParams());
        assertTrue(collector.hasDefaultCreator());
    }
    
    @Test
    public void testHasDelegatingCreator() {
        CreatorCollector collector = new CreatorCollector();
        assertFalse(collector.hasDelegatingCreator());
        collector.addDelegatingCreator(new AnnotatedWithParams(), false, null);
        assertTrue(collector.hasDelegatingCreator());
    }
    
    @Test
    public void testHasPropertyBasedCreator() {
        CreatorCollector collector = new CreatorCollector();
        assertFalse(collector.hasPropertyBasedCreator());
        collector.addPropertyCreator(new AnnotatedWithParams(), false, null);
        assertTrue(collector.hasPropertyBasedCreator());
    }
}

