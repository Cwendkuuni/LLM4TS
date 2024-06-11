package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,maximumWeight=1000,concurrencyLevel=4,weakKeys,softValues,recordStats,expireAfterAccess=10m,expireAfterWrite=1h,refreshAfterWrite=1d");
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
        assertEquals(1000, spec.maximumWeight.longValue());
        assertEquals(4, spec.concurrencyLevel.intValue());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertEquals(true, spec.recordStats);
        assertEquals(10, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(1, spec.refreshDuration);
        assertEquals(TimeUnit.DAYS, spec.refreshTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.longValue());
        assertNull(spec.maximumWeight);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=4", spec.toParsableString());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        assertEquals(spec1, spec2);
    }
}