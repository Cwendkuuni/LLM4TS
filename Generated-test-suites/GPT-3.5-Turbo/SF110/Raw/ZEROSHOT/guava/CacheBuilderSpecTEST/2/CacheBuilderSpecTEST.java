import com.google.common.cache.CacheBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;
    
    @Before
    public void setUp() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }
    
    @Test
    public void testParse_invalidKey() {
        cacheBuilderSpec.parse("invalidKey=123");
        assertNull(cacheBuilderSpec.initialCapacity);
    }
    
    @Test
    public void testParse_validKey_withValue() {
        cacheBuilderSpec.parse("initialCapacity=100");
        assertEquals(100, (int) cacheBuilderSpec.initialCapacity);
    }
    
    @Test
    public void testParse_validKey_withoutValue() {
        cacheBuilderSpec.parse("recordStats");
        assertTrue(cacheBuilderSpec.recordStats);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledCache = CacheBuilderSpec.disableCaching();
        assertEquals(0, (long) disabledCache.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.refreshDuration = 5;
        cacheBuilderSpec.refreshTimeUnit = TimeUnit.MINUTES;
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(100, cacheBuilder.maximumSize());
    }
    
    @Test
    public void testToParsableString() {
        cacheBuilderSpec.initialCapacity = 10;
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.recordStats = true;
        String parsableString = cacheBuilderSpec.toParsableString();
        assertEquals("initialCapacity=10, maximumSize=100, recordStats", parsableString);
    }
    
    @Test
    public void testHashCode() {
        cacheBuilderSpec.initialCapacity = 10;
        cacheBuilderSpec.maximumSize = 100;
        int hashCode = cacheBuilderSpec.hashCode();
        assertEquals(-1706769648, hashCode);
    }
    
    @Test
    public void testEquals_sameObject() {
        assertTrue(cacheBuilderSpec.equals(cacheBuilderSpec));
    }
    
    @Test
    public void testEquals_differentClass() {
        assertFalse(cacheBuilderSpec.equals(new Object()));
    }
    
    @Test
    public void testEquals_differentInitialCapacity() {
        CacheBuilderSpec other = new CacheBuilderSpec("");
        other.initialCapacity = 20;
        assertFalse(cacheBuilderSpec.equals(other));
    }
    
    @Test
    public void testEquals_differentMaximumSize() {
        CacheBuilderSpec other = new CacheBuilderSpec("");
        other.maximumSize = 200;
        assertFalse(cacheBuilderSpec.equals(other));
    }
    
    @Test
    public void testEquals_sameValues() {
        CacheBuilderSpec other = new CacheBuilderSpec("");
        other.initialCapacity = 10;
        other.maximumSize = 100;
        assertTrue(cacheBuilderSpec.equals(other));
    }
    
    @Test
    public void testDurationInNanos_nullUnit() {
        assertNull(CacheBuilderSpec.durationInNanos(5, null));
    }
    
    @Test
    public void testDurationInNanos_nonNullUnit() {
        assertEquals(300000000000L, (long) CacheBuilderSpec.durationInNanos(5, TimeUnit.MINUTES));
    }
}