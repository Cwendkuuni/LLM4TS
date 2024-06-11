import static org.junit.Assert.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Typical use case
        String cacheSpec = "maximumSize=100,initialCapacity=50";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);
        assertNotNull(spec);
        
        // Edge cases
        cacheSpec = "";
        spec = CacheBuilderSpec.parse(cacheSpec);
        assertNotNull(spec);
        
        cacheSpec = "maximumSize=100, maximumWeight=50";
        spec = CacheBuilderSpec.parse(cacheSpec);
        assertNotNull(spec);
        
        // Error scenarios
        cacheSpec = "invalidSpec";
        try {
            spec = CacheBuilderSpec.parse(cacheSpec);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDisableCaching() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        
        // Edge cases
        // No edge cases for this method
        
        // Error scenarios
        // No error scenarios for this method
    }

    @Test
    public void testToCacheBuilder() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        
        // Edge cases
        // No edge cases for this method
        
        // Error scenarios
        // No error scenarios for this method
    }

    @Test
    public void testToParsableString() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=50,recordStats");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=50,recordStats", parsableString);
        
        // Edge cases
        // No edge cases for this method
        
        // Error scenarios
        // No error scenarios for this method
    }

    // Add more test cases for hashCode, equals, and other methods as necessary

}