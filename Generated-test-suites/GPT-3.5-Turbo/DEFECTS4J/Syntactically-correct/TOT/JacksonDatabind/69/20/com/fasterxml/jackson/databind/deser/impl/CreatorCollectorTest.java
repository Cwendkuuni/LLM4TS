
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    
    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        
        assertNotNull(creatorCollector);
        assertTrue(creatorCollector.hasDefaultCreator());
        assertFalse(creatorCollector.hasDelegatingCreator());
        assertFalse(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        
        assertNotNull(creatorCollector._creators[0]);
        assertEquals(creator, creatorCollector._creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        
        assertNotNull(creatorCollector._creators[1]);
        assertEquals(creator, creatorCollector._creators[1]);
    }
    
    // Add tests for other methods
    
}

class TestAnnotatedWithParams implements AnnotatedWithParams {
    // Implement methods from AnnotatedWithParams interface
}
