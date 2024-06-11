package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,maximumWeight=200,concurrencyLevel=4");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
        assertEquals(Long.valueOf(200), spec.maximumWeight);
        assertEquals(Integer.valueOf(4), spec.concurrencyLevel);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,maximumWeight=200,concurrencyLevel=4");
        assertEquals("initialCapacity=10,maximumSize=100,maximumWeight=200,concurrencyLevel=4", spec.toParsableString());
    }

    @Test
    public void testToParsableStringWithNullValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        assertNull(spec.toParsableString());
    }

    // Add more test cases for other methods in CacheBuilderSpec class
}