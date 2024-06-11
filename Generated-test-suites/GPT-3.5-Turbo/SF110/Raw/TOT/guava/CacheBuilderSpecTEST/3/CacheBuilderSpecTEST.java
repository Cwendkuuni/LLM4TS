import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptyCacheBuilderSpecification_ReturnsEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals("", spec.toParsableString());
    }
    
    @Test
    public void testParse_InvalidKeyValuePair_ThrowsIllegalArgumentException() {
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("unknown key invalidKey", e.getMessage());
        }
    }
    
    @Test
    public void testParse_InvalidValueForIntegerKey_ThrowsIllegalArgumentException() {
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=abc");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("key initialCapacity value set to abc, must be integer", e.getMessage());
        }
    }
    
    @Test
    public void testParse_ValidCacheBuilderSpecification_ReturnsValidSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,recordStats");
        assertNotNull(spec);
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100L, spec.maximumSize.longValue());
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals("initialCapacity=10, maximumSize=100, recordStats", spec.toParsableString());
    }
    
    @Test
    public void testDisableCaching_ReturnsSpecWithMaximumSizeZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertNull(spec.initialCapacity);
        assertEquals(0L, spec.maximumSize.longValue());
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals("maximumSize=0", spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder_ReturnsCacheBuilderObjectWithSpecifiedValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,recordStats");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(10, builder.initialCapacity());
        assertEquals(100L, builder.maximumSize());
        assertFalse(builder.recordStats());
    }
    
    // Additional test cases can be added to cover more methods and scenarios
    
}