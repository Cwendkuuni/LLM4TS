import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=100,maximumSize=1000";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        assertEquals((Integer) 100, spec.initialCapacity);
        assertEquals((Long) 1000L, spec.maximumSize);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals((Long) 0L, spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertEquals(100, builder.initialCapacity());
        assertEquals(1000, builder.maximumSize());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        String parsableString = spec.toParsableString();
        
        assertEquals("initialCapacity=100,maximumSize=1000", parsableString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        
        assertTrue(spec1.equals(spec2));
    }
}