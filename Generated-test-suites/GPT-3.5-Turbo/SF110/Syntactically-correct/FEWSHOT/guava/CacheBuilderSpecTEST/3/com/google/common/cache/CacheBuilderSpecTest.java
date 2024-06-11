package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        String cacheSpec = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
        assertEquals(Integer.valueOf(5), spec.concurrencyLevel);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        String parsableString = spec.toParsableString();

        assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=5", parsableString);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertEquals(Integer.valueOf(10), builder.initialCapacity());
        assertEquals(Long.valueOf(100), builder.maximumSize());
        assertEquals(Integer.valueOf(5), builder.concurrencyLevel());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        assertTrue(spec1.equals(spec2));
    }
}