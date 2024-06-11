
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=100, concurrencyLevel=5");
        assertEquals(10, (int) spec.initialCapacity);
        assertEquals(100L, (long) spec.maximumSize);
        assertEquals(5, (int) spec.concurrencyLevel);
        assertNull(spec.maximumWeight);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(0L, spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(0L, spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(0L, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, (int) spec.initialCapacity);
        assertEquals(0L, (long) spec.maximumSize);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.maximumWeight);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(0L, spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(0L, spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(0L, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(CacheBuilder.class, builder.getClass());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, concurrencyLevel=5");
        assertEquals("initialCapacity=10, maximumSize=100, concurrencyLevel=5", spec.toParsableString());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}
