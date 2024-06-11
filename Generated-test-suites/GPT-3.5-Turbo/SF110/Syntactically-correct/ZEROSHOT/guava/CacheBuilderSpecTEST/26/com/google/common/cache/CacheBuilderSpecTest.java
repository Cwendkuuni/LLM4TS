import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, concurrencyLevel=4";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(spec);
        
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
        assertEquals(Integer.valueOf(4), spec.concurrencyLevel);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
        
        assertEquals("initialCapacity=10, maximumSize=100, concurrencyLevel=4", parsableString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=4");
        
        assertTrue(spec1.equals(spec2));
    }
}