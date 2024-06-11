package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec.specification);

        spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals("maximumSize=100", spec.specification);

        spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=100");
        assertEquals("initialCapacity=10, maximumSize=100", spec.specification);
        
        // Add more test cases for other key-value pairs
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", spec.specification);
    }
    
    // Add more test methods to cover all methods in CacheBuilderSpec
    
}