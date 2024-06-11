import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LocalCache;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private static final String CACHE_SPEC = "initialCapacity=10, maximumSize=100, maximumWeight=1000, " +
            "concurrencyLevel=4, weakKeys, softValues, recordStats, " +
            "expireAfterAccess=10m, expireAfterWrite=1h, refreshAfterWrite=5s";

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = CacheBuilderSpec.parse(CACHE_SPEC);
    }

    @Test
    public void testParse() {
        assertEquals((Integer) 10, cacheBuilderSpec.initialCapacity);
        assertEquals((Long) 100, cacheBuilderSpec.maximumSize);
        assertEquals((Long) 1000, cacheBuilderSpec.maximumWeight);
        assertEquals((Integer) 4, cacheBuilderSpec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.valueStrength);
        assertTrue(cacheBuilderSpec.recordStats);
        assertEquals(10, cacheBuilderSpec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, cacheBuilderSpec.accessExpirationTimeUnit);
        assertEquals(1, cacheBuilderSpec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, cacheBuilderSpec.writeExpirationTimeUnit);
        assertEquals(5, cacheBuilderSpec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, cacheBuilderSpec.refreshTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledSpec = CacheBuilderSpec.disableCaching();
        assertEquals((Long) 0L, disabledSpec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(1000, builder.maximumWeight());
        assertEquals(4, builder.concurrencyLevel());
        assertTrue(builder.weakKeys());
        assertTrue(builder.softValues());
        assertTrue(builder.recordStats());
        assertEquals(10, builder.expireAfterAccess().getDuration());
        assertEquals(TimeUnit.MINUTES, builder.expireAfterAccess().getTimeUnit());
        assertEquals(1, builder.expireAfterWrite().getDuration());
        assertEquals(TimeUnit.HOURS, builder.expireAfterWrite().getTimeUnit());
        assertEquals(5, builder.refreshAfterWrite().getDuration());
        assertEquals(TimeUnit.SECONDS, builder.refreshAfterWrite().getTimeUnit());
    }

    @Test
    public void testToParsableString() {
        assertEquals(CACHE_SPEC, cacheBuilderSpec.toParsableString());
    }

    @Test
    public void testToString() {
        String expected = "CacheBuilderSpec{initialCapacity=10, maximumSize=100, maximumWeight=1000, " +
                "concurrencyLevel=4, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, " +
                "accessExpirationDuration=10, accessExpirationTimeUnit=MINUTES, " +
                "writeExpirationDuration=1, writeExpirationTimeUnit=HOURS, " +
                "refreshDuration=5, refreshTimeUnit=SECONDS}";
        assertEquals(expected, cacheBuilderSpec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec other = CacheBuilderSpec.parse(CACHE_SPEC);
        assertEquals(cacheBuilderSpec.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec other = CacheBuilderSpec.parse(CACHE_SPEC);
        assertTrue(cacheBuilderSpec.equals(other));
    }
}