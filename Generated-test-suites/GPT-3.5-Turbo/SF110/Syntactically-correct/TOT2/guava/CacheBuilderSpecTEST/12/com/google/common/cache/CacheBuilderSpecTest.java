package com.google.common.cache;
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    CacheBuilderSpec cacheBuilderSpec;

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=100,maximumSize=100,maximumWeight=100,concurrencyLevel=8";
        cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(cacheBuilderSpec);
        assertEquals(Integer.valueOf(100), cacheBuilderSpec.initialCapacity);
        assertEquals(Long.valueOf(100), cacheBuilderSpec.maximumSize);
        assertEquals(Long.valueOf(100), cacheBuilderSpec.maximumWeight);
        assertEquals(Integer.valueOf(8), cacheBuilderSpec.concurrencyLevel);
    }

    @Test
    public void testDisableCaching() {
        cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        assertNotNull(cacheBuilderSpec);
        assertEquals("maximumSize=0", cacheBuilderSpec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=15");
        cacheBuilderSpec.toCacheBuilder();
        // Add assertions here
    }

    @Test
    public void testToString() {
        cacheBuilderSpec = new CacheBuilderSpec("maximumSize=50");
        String expected = "CacheBuilderSpec{maximumSize=50}";
        assertEquals(expected, cacheBuilderSpec.toString());
    }

    @Test
    public void testHashCode() {
        cacheBuilderSpec = new CacheBuilderSpec("maximumWeight=75");
        CacheBuilderSpec anotherCacheBuilderSpec = new CacheBuilderSpec("maximumWeight=75");
        assertEquals(cacheBuilderSpec.hashCode(), anotherCacheBuilderSpec.hashCode());
    }

    @Test
    public void testEquals() {
        cacheBuilderSpec = new CacheBuilderSpec("concurrencyLevel=5");
        CacheBuilderSpec anotherCacheBuilderSpec = new CacheBuilderSpec("concurrencyLevel=5");
        assertTrue(cacheBuilderSpec.equals(anotherCacheBuilderSpec));
    }

}