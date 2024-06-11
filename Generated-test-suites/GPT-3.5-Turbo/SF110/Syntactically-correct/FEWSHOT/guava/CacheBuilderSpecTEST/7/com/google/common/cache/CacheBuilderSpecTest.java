package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.cache.CacheBuilder.Object;
import com.google.common.cache.CacheBuilder.Object;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=100");
        assertEquals(10, spec1.initialCapacity);
        assertEquals(100, spec1.maximumSize);
        
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumWeight=200, concurrencyLevel=4");
        assertEquals(200, spec2.maximumWeight);
        assertEquals(4, spec2.concurrencyLevel);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidSpec() {
        CacheBuilderSpec.parse("initialCapacity=10, maximumSize=");
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.intValue());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertEquals("initialCapacity=10, maximumSize=100", spec.toParsableString());
    }
    
    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertEquals("CacheBuilderSpec{initialCapacity=10, maximumSize=100}", spec.toString());
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertTrue(spec1.hashCode() == spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }
}