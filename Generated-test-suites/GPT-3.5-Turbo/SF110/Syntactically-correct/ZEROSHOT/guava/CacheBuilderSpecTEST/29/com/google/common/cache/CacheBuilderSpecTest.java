import com.google.common.cache.CacheBuilder;
import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
        assertEquals(4, spec.concurrencyLevel.intValue());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.longValue());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
        assertTrue(builder.isWeakKeys());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys", parsableString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,weakKeys");
        assertTrue(spec1.equals(spec2));
    }
}