import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilderSpec.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100");
        assertEquals(100, spec.initialCapacity);
    }

    @Test
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(100, spec.maximumSize);
    }

    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=100");
        assertEquals(100, spec.maximumWeight);
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=4");
        assertEquals(4, spec.concurrencyLevel);
    }

    @Test
    public void testParse_keyStrength_weakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }

    @Test
    public void testParse_valueStrength_softValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }

    @Test
    public void testParse_recordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        assertEquals(true, spec.recordStats);
    }

    @Test
    public void testParse_expireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
        assertEquals(1, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
    }

    @Test
    public void testParse_expireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=10m");
        assertEquals(10, spec.writeExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5s");
        assertEquals(5, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }

    @Test
    public void testDisableCaching_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100, builder.maximumSize());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        assertEquals("maximumSize=100", spec.toParsableString());
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
        CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=200");
        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(null));
        assertFalse(spec1.equals(spec3));
    }

}