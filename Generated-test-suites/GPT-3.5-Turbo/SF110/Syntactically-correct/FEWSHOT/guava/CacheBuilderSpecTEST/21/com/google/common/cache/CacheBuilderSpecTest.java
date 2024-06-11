package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.CacheBuilderSpec;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals("", spec.toParsableString());
        
        cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, maximumWeight=1000, concurrencyLevel=5, "
                + "weakKeys, softValues, recordStats, expireAfterAccess=1m, expireAfterWrite=1h, refreshAfterWrite=1d, refreshInterval=1h";
        spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }
    
    @Test
    public void testDisableCaching() {
        String cacheBuilderSpecification = "maximumSize=0";
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertTrue(spec.toCacheBuilder() instanceof CacheBuilder);
    }

    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=1000";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }

    @Test
    public void testEqualsObject() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(spec1, spec2);
        
        spec1 = CacheBuilderSpec.parse("initialCapacity=10");
        spec2 = CacheBuilderSpec.parse("initialCapacity=20");
        assertNotEquals(spec1, spec2);
    }

}