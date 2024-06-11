import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test case for parsing CacheBuilderSpec
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertNotNull(spec);
        assertEquals(100, spec.maximumSize.longValue());
    }

    @Test
    public void testDisableCaching() {
        // Test case for disabling caching
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        // Test case for converting CacheBuilderSpec to CacheBuilder
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(10, builder.initialCapacity());
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}