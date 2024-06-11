import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertNotNull(spec);
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.intValue());
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.intValue());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertNotNull(spec);
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(5, builder.concurrencyLevel());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=50");
        assertNotNull(spec);
        assertEquals("maximumSize=50", spec.toParsableString());
    }
    
    // Add more test cases for edge cases and error scenarios
    
}