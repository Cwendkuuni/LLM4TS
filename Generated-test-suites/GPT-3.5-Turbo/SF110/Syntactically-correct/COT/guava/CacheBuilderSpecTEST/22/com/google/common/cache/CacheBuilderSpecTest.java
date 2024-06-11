import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, initialCapacity=50");
        assertNotNull(spec);
        assertEquals(100l, (long)spec.maximumSize);
        assertEquals(50, (int)spec.initialCapacity);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals(0l, (long)spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100l, builder.maximumSize());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
        assertEquals("maximumSize=100", parsableString);
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        int hashCode = spec.hashCode();
        assertNotNull(hashCode);
        assertNotEquals(0, hashCode);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testExtraKeyValuePairs() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, initialCapacity=50, concurrencyLevel=5");
        assertNotNull(spec);
        assertEquals(100l, (long)spec.maximumSize);
        assertEquals(50, (int)spec.initialCapacity);
        assertEquals(5, (int)spec.concurrencyLevel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyValuePair() {
        CacheBuilderSpec.parse("maximumSize=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeUnit() {
        CacheBuilderSpec.parse("expireAfterWrite=10z");
    }

    @Test
    public void testInvalidKeyValueParser() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=10");
        assertNotNull(spec);
        assertNull(spec.maximumSize);
    }

    // 
    // Add more individual test cases if needed
    // 
}