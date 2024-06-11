import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        
        spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(Long.valueOf(100), spec.maximumSize);
        
        spec = CacheBuilderSpec.parse("maximumWeight=1000");
        assertEquals(Long.valueOf(1000), spec.maximumWeight);
        
        spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertEquals(Integer.valueOf(5), spec.concurrencyLevel);
        
        spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        
        spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        
        spec = CacheBuilderSpec.parse("recordStats");
        assertTrue(spec.recordStats);
        
        spec = CacheBuilderSpec.parse("expireAfterAccess=10m");
        assertEquals(10, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        
        spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        
        spec = CacheBuilderSpec.parse("refreshAfterWrite=30s");
        assertEquals(30, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,weakKeys");
        assertEquals("maximumSize=100, weakKeys", spec.toParsableString());
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }
}