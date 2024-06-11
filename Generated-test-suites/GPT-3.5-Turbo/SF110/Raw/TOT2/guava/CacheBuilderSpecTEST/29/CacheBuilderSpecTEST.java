import com.google.common.cache.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        assertNotNull(spec);
        
        // Edge case: empty string
        spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        
        // Error scenario: blank key-value pair
        try {
            CacheBuilderSpec.parse("=100");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        // Need to test all setters and getters
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=1000");
        String parsableString = spec.toParsableString();
        assertEquals("maximumSize=1000", parsableString);
    }

    // add more test cases for additional methods if needed

}