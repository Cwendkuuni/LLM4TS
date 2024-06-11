import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void parse_emptySpec_shouldReturnEmptyCacheBuilderSpec() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("", spec.toParsableString());
    }

    @Test
    public void parse_specWithInitialCapacityAndMaximumSize_shouldReturnCacheBuilderSpec() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }

    @Test
    public void parse_specWithInitialCapacityAndMaximumWeight_shouldReturnCacheBuilderSpec() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumWeight=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(Long.valueOf(100), spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }

    // Continue testing for all methods in the CacheBuilderSpec class

}