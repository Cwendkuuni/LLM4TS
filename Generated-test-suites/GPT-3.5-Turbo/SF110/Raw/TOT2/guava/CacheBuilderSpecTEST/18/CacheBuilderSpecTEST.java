import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertEquals(100, spec.maximumSize.intValue());
        assertEquals(50, spec.initialCapacity.intValue());

        // Test edge case with additional config
        spec = CacheBuilderSpec.parse("maximumSize=0");
        assertEquals(0, spec.maximumSize.intValue());
        assertNull(spec.initialCapacity);

        // Test error scenario with blank key-value pair
        try {
            CacheBuilderSpec.parse("maximumSize=");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Add more test cases as needed
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.intValue());
        assertNull(spec.initialCapacity);
    }

    @Test
    public void testToCacheBuilder() {
        // Test typical use case
		CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
		CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
		assertNotNull(builder);

		// Test edge case with null values
		spec = CacheBuilderSpec.parse("maximumSize=0");
		builder = spec.toCacheBuilder();
		assertNotNull(builder);

		// Add more test cases as needed
    }

    @Test
    public void testToParsableString() {
        // Test typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50");
        assertEquals("maximumSize=100, initialCapacity=50", spec.toParsableString());

        // Add more test cases as needed
    }

    // Add more test cases for other methods if needed

}