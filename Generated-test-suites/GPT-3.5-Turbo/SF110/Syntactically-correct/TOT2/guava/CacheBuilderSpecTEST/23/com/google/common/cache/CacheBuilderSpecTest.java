package com.google.common.cache;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=100");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);

        // Test edge case with empty input
        spec = CacheBuilderSpec.parse("");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        // Test typical use case
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    // Add more test cases for other methods as per requirements

}