package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(10, spec.initialCapacity.intValue());

        spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(100L, spec.maximumSize.longValue());

        spec = CacheBuilderSpec.parse("maximumWeight=1000");
        assertEquals(1000L, spec.maximumWeight.longValue());

        spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertEquals(5, spec.concurrencyLevel.intValue());

        spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);

        spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);

        spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);

        spec = CacheBuilderSpec.parse("recordStats");
        assertTrue(spec.recordStats);

        spec = CacheBuilderSpec.parse("expireAfterAccess=1m");
        assertEquals(1L, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);

        spec = CacheBuilderSpec.parse("expireAfterWrite=2h");
        assertEquals(2L, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);

        spec = CacheBuilderSpec.parse("refreshAfterWrite=30s");
        assertEquals(30L, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        assertEquals("", spec.toParsableString());

        spec = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        assertEquals("maximumSize=100, initialCapacity=10", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);

        spec = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100L, builder.maximumSize());
        assertEquals(10, builder.initialCapacity());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        assertTrue(spec1.equals(spec2));

        spec1 = new CacheBuilderSpec("maximumSize=100");
        spec2 = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        assertFalse(spec1.equals(spec2));
    }
}