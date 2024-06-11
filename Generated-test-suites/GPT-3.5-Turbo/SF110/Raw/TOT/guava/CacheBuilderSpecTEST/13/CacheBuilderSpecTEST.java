import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setup() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }

    @Test
    public void testParse_WithEmptyCacheBuilderSpecification_ShouldReturnEmptyCacheBuilderSpec() {
        CacheBuilderSpec result = CacheBuilderSpec.parse("");
        assertNull(result.toParsableString());
    }

    @Test
    public void testParse_WithCacheBuilderSpecificationContainingInitialCapacity_ShouldSetInitialCapacity() {
        CacheBuilderSpec result = CacheBuilderSpec.parse("initialCapacity=100");
        assertEquals("initialCapacity=100", result.toParsableString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithCacheBuilderSpecificationContainingInvalidInitialCapacity_ShouldThrowIllegalArgumentException() {
        CacheBuilderSpec.parse("initialCapacity=abc");
    }

    @Test
    public void testDisableCaching_ShouldReturnCacheBuilderSpecWithMaximumSizeSetToZero() {
        CacheBuilderSpec result = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", result.toParsableString());
    }

    @Test
    public void testToCacheBuilder_WithAllAttributesSet_ShouldReturnCacheBuilderWithAllAttributesSet() {
        cacheBuilderSpec.initialCapacity = 100;
        cacheBuilderSpec.maximumSize = 1000L;
        cacheBuilderSpec.maximumWeight = 2000L;
        cacheBuilderSpec.concurrencyLevel = 4;
        cacheBuilderSpec.keyStrength = LocalCache.Strength.WEAK;
        cacheBuilderSpec.valueStrength = LocalCache.Strength.SOFT;
        cacheBuilderSpec.recordStats = true;
        cacheBuilderSpec.writeExpirationDuration = 1;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.HOURS;
        cacheBuilderSpec.accessExpirationDuration = 2;
        cacheBuilderSpec.accessExpirationTimeUnit = TimeUnit.DAYS;
        cacheBuilderSpec.refreshDuration = 3;
        cacheBuilderSpec.refreshTimeUnit = TimeUnit.MINUTES;

        CacheBuilder<Object, Object> result = cacheBuilderSpec.toCacheBuilder();

        assertEquals(100, result.initialCapacity());
        assertEquals(1000L, result.maximumSize());
        assertEquals(2000L, result.maximumWeight());
        assertEquals(4, result.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.valueStrength);
        assertTrue(result.isRecordingStats());
        assertEquals(1, result.getExpireAfterWriteDuration());
        assertEquals(TimeUnit.HOURS, result.getExpireAfterWriteTimeUnit());
        assertEquals(2, result.getExpireAfterAccessDuration());
        assertEquals(TimeUnit.DAYS, result.getExpireAfterAccessTimeUnit());
        assertEquals(3, result.getRefreshAfterWriteDuration());
        assertEquals(TimeUnit.MINUTES, result.getRefreshAfterWriteTimeUnit());
    }

    @Test
    public void testHashCode_WithSameAttributesSet_ShouldReturnSameHashCode() {
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("");

        cacheBuilderSpec.initialCapacity = 100;
        cacheBuilderSpec2.initialCapacity = 100;

        int hashCode1 = cacheBuilderSpec.hashCode();
        int hashCode2 = cacheBuilderSpec2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals_WithSameAttributesSet_ShouldReturnTrue() {
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("");

        cacheBuilderSpec.initialCapacity = 100;
        cacheBuilderSpec2.initialCapacity = 100;

        assertTrue(cacheBuilderSpec.equals(cacheBuilderSpec2));
    }

    @Test
    public void testEquals_WithDifferentAttributesSet_ShouldReturnFalse() {
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("");

        cacheBuilderSpec.initialCapacity = 100;
        cacheBuilderSpec2.initialCapacity = 200;

        assertFalse(cacheBuilderSpec.equals(cacheBuilderSpec2));
    }

}