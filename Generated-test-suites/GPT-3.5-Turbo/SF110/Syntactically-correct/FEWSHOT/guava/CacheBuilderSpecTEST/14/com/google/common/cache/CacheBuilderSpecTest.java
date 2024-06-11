package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        assertEquals(100, spec.initialCapacity.intValue());
        assertEquals(1000, spec.maximumSize.longValue());
        
        spec = CacheBuilderSpec.parse("maximumWeight=1000");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(1000, spec.maximumWeight.longValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(100, builder.initialCapacity());
        assertEquals(1000, builder.maximumSize());
        
        spec = CacheBuilderSpec.parse("maximumWeight=1000");
        builder = spec.toCacheBuilder();
        assertEquals(1000, builder.maximumWeight());
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        assertEquals("initialCapacity=100,maximumSize=1000", spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=200,maximumSize=2000");

        assertEquals(spec1.hashCode(), spec2.hashCode());
        assertNotEquals(spec1.hashCode(), spec3.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=200,maximumSize=2000");

        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
    }
}