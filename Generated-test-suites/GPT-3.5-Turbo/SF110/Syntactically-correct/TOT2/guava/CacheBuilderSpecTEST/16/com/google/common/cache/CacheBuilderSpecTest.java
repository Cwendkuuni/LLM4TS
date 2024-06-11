package com.google.common.cache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = CacheBuilderSpec.parse("maximumSize=10,initialCapacity=5,concurrencyLevel=3");
    }

    @Test
    public void testParse() {
        assertEquals("maximumSize=10,initialCapacity=5,concurrencyLevel=3", cacheBuilderSpec.toParsableString());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledCache = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", disabledCache.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(cacheBuilder);
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec sameSpec = CacheBuilderSpec.parse("maximumSize=10,initialCapacity=5,concurrencyLevel=3");
        assertTrue(cacheBuilderSpec.equals(sameSpec));

        CacheBuilderSpec differentSpec = CacheBuilderSpec.parse("maximumSize=20,initialCapacity=5,concurrencyLevel=3");
        assertFalse(cacheBuilderSpec.equals(differentSpec));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec sameSpec = CacheBuilderSpec.parse("maximumSize=10,initialCapacity=5,concurrencyLevel=3");
        assertEquals(cacheBuilderSpec.hashCode(), sameSpec.hashCode());

        CacheBuilderSpec differentSpec = CacheBuilderSpec.parse("maximumSize=20,initialCapacity=5,concurrencyLevel=3");
        assertNotEquals(cacheBuilderSpec.hashCode(), differentSpec.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSpecParsing() {
        CacheBuilderSpec.parse("invalidKey=10");
    }
}