import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setup() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec.initialCapacity = 10;
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.maximumWeight = 200;
        cacheBuilderSpec.concurrencyLevel = 5;
        cacheBuilderSpec.keyStrength = LocalCache.Strength.WEAK;
        cacheBuilderSpec.valueStrength = LocalCache.Strength.SOFT;
        cacheBuilderSpec.recordStats = true;
        cacheBuilderSpec.writeExpirationDuration = 2;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.HOURS;
        cacheBuilderSpec.accessExpirationDuration = 1;
        cacheBuilderSpec.accessExpirationTimeUnit = TimeUnit.MINUTES;
        cacheBuilderSpec.refreshDuration = 10;
        cacheBuilderSpec.refreshTimeUnit = TimeUnit.SECONDS;

        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToString() {
        String expected = "CacheBuilderSpec{}";
        String actual = cacheBuilderSpec.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = 42;
        int actual = cacheBuilderSpec.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Object obj = new CacheBuilderSpec("");
        assertTrue(cacheBuilderSpec.equals(obj));
    }

    @Test
    public void testDurationInNanos() {
        Long result = cacheBuilderSpec.durationInNanos(1000, TimeUnit.MILLISECONDS);
        assertNotNull(result);
    }
}