package com.google.common.cache;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertNotNull(spec);
        
        // Edge case: Empty string
        spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        
        // Error scenario: Invalid key
        try {
            spec = CacheBuilderSpec.parse("invalidKey=10");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals(0, spec.maximumSize.longValue());
    }

    // Add more test cases for other public methods here

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10");

        assertTrue(spec1.equals(spec2));

        spec1 = CacheBuilderSpec.parse("initialCapacity=10");
        spec2 = CacheBuilderSpec.parse("initialCapacity=20");

        assertFalse(spec1.equals(spec2));
    }

    // Add test cases for other public methods to cover edge cases and exceptions

}