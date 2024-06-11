package com.google.common.cache;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertNotNull(spec);
        assertEquals("maximumSize=100", spec.toParsableString());

        // Edge case: Empty spec
        CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
        assertNotNull(emptySpec);
        assertEquals("", emptySpec.toParsableString());

        // Error scenario: Invalid key-value pair
        try {
            CacheBuilderSpec invalidSpec = CacheBuilderSpec.parse("invalidPair");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        // Add more assertions here based on the expected behavior of toCacheBuilder()
    }

    @Test
    public void testToParsableString() {
        // Typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
        assertEquals("maximumSize=100", parsableString);
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.disableCaching();
        CacheBuilderSpec spec2 = CacheBuilderSpec.disableCaching();
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec.hashCode());
    }
}