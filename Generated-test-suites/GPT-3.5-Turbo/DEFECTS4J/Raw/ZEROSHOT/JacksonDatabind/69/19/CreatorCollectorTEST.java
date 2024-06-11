import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        assertNotNull(creator);
        
        assertFalse(creator.hasDefaultCreator());
        assertFalse(creator.hasDelegatingCreator());
        assertFalse(creator.hasPropertyBasedCreator());
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.setDefaultCreator(annotatedWithParams);
        
        assertTrue(creator.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addStringCreator(annotatedWithParams, false);
        
        assertTrue(creator.hasStringCreator());
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addIntCreator(annotatedWithParams, false);
        
        assertTrue(creator.hasIntCreator());
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addLongCreator(annotatedWithParams, false);
        
        assertTrue(creator.hasLongCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addDoubleCreator(annotatedWithParams, false);
        
        assertTrue(creator.hasDoubleCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addBooleanCreator(annotatedWithParams, false);
        
        assertTrue(creator.hasBooleanCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addDelegatingCreator(annotatedWithParams, false, null);
        
        assertTrue(creator.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creator.addPropertyCreator(annotatedWithParams, false, null);
        
        assertTrue(creator.hasPropertyBasedCreator());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creator = new CreatorCollector(beanDesc, config);
        
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creator.addIncompeteParameter(annotatedParameter);
        
        assertNotNull(creator.getIncompleteParameter());
    }

}