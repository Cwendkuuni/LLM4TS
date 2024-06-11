package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(100, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, recordStats");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=5, recordStats", parsableString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=200");
        
        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
    }
    
}