import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
    
    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = ClassUtil.getBeanDescription(CreatorCollector.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, false);
        DeserializationConfig config = new DeserializationConfig(null);
        
        assertEquals(null, creatorCollector.constructValueInstantiator(config).createUsingDefault(null));
        
        creatorCollector.setDefaultCreator(null);
        assertEquals(null, creatorCollector.constructValueInstantiator(config).createUsingDefault(null));
        
        creatorCollector.setPrototypeOf(null);
        assertEquals(null, creatorCollector.constructValueInstantiator(config).createUsingDefault(null));
    }
    
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    // other test methods for remaining methods

}