package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(spec);
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<?, ?> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100, builder.maximumSize());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
        assertEquals("maximumSize=100", parsableString);
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
        assertEquals(spec1, spec2);
    }
}