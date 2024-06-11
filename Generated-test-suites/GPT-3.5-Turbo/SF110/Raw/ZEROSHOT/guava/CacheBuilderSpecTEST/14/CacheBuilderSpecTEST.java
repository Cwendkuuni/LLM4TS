package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=50");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(5, builder.initialCapacity());
        assertEquals(50, builder.maximumSize());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=50");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=5,maximumSize=50", parsableString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(spec1, spec2);
    }
    
}