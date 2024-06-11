import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test parsing valid cache builder specification
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertEquals(100L, (long) spec.maximumSize);
        assertEquals(50, (int) spec.initialCapacity);
    }
    
    @Test
    public void testDisableCaching() {
        // Test disableCaching method
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.specification);
    }
    
    @Test
    public void testToCacheBuilder() {
        // Test toCacheBuilder method with various configurations
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,concurrencyLevel=10");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100L, builder.maximumSize());
        assertEquals(10, builder.concurrencyLevel());
    }
  
    @Test
    public void testToParsableString() {
        // Test toParsableString method
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertEquals("maximumSize=100,initialCapacity=50", spec.toParsableString());
    }
    
    @Test
    public void testEquals() {
        // Test equals method with same CacheBuilderSpec object
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertTrue(spec1.equals(spec1));
        
        // Test equals method with different CacheBuilderSpec object
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertTrue(spec1.equals(spec2));
        
        // Test equals method with null object
        assertFalse(spec1.equals(null));
    }
    
    @Test
    public void testHashCode() {
        // Test hashCode method
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}