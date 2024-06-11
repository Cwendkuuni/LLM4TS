package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("");
        assertEquals("", spec1.toParsableString());

        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals("maximumSize=100", spec2.toParsableString());

        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=50,concurrencyLevel=4");
        assertEquals("initialCapacity=50,concurrencyLevel=4", spec3.toParsableString());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=8");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(8, builder.concurrencyLevel());
        assertTrue(builder.build() instanceof Cache);
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals("CacheBuilderSpec{maximumSize=100}", spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(spec1, spec2);
    }
}