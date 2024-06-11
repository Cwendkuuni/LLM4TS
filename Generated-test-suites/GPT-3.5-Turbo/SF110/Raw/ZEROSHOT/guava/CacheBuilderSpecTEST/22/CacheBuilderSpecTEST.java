import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("maximumSize=1000,expireAfterAccess=10m");
        assertEquals(1000L, (long) parsedSpec.maximumSize);
        assertEquals(10L, parsedSpec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, parsedSpec.accessExpirationTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledSpec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long) disabledSpec.maximumSize);;
    }

    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.writeExpirationDuration = 1;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.HOURS;

        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();

        assertEquals(100L, (long) builder.maximumSize());
        assertEquals(1L, builder.expireAfterWriteDuration());
        assertEquals(TimeUnit.HOURS, builder.expireAfterWriteTimeUnit());
    }

    @Test
    public void testToParsableString() {
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.refreshDuration = 30;
        cacheBuilderSpec.refreshTimeUnit = TimeUnit.SECONDS;

        String parsableString = cacheBuilderSpec.toParsableString();

        assertTrue(parsableString.contains("maximumSize=100"));
        assertTrue(parsableString.contains("refreshAfterWrite=30s"));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.maximumSize = 100;
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.maximumSize = 100;

        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.maximumSize = 100;
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.maximumSize = 100;

        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testDurationInNanos() {
        Long nanos = CacheBuilderSpec.durationInNanos(5, TimeUnit.MINUTES);
        assertEquals(300000000000L, (long) nanos);
    }
}