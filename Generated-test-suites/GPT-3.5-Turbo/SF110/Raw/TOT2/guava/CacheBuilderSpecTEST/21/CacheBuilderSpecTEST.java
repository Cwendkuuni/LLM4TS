import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test parsing with valid cacheBuilderSpecification
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, concurrencyLevel=5");
        assertEquals(100, (int) spec.maximumSize);
        assertEquals(5, (int) spec.concurrencyLevel);
        
        // Test parsing with empty cacheBuilderSpecification
        spec = CacheBuilderSpec.parse("");
        assertNull(spec.maximumSize);
        assertNull(spec.concurrencyLevel);
        
        // Test parsing with invalid key-value pair format
        try {
            CacheBuilderSpec.parse("maximumSize:100");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, (int) spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        // Test toCacheBuilder method with sample values
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        // Add assertions to check builder configuration based on spec
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals("maximumSize=100", spec.toParsableString());
    }

    // Add more test cases for other methods in CacheBuilderSpec class

}