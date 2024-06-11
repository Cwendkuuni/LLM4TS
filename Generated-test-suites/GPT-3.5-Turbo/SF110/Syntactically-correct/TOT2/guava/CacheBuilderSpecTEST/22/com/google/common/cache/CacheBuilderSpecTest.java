package com.google.common.cache;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000, concurrencyLevel=10, weakKeys");
        assertNotNull(spec);
        assertEquals(Integer.valueOf(100), spec.initialCapacity);
        assertEquals(Long.valueOf(1000), spec.maximumSize);
        assertEquals(Integer.valueOf(10), spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h, expireAfterWrite=2h, refreshAfterWrite=3h");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        // Additional assertions on the builder configuration can be added here
    }
    
    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertNotNull(spec);
        assertEquals("maximumSize=100", spec.toString());
    }

    // Add more test cases for other methods
    
}