import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,recordStats");
        assertNotNull(spec);
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
        assertTrue(spec.recordStats);
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
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
        assertEquals(100, builder.maximumSize());
    }

    // Add more test cases for other public methods

    @Test
    public void testDurationParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterWrite=10s");
        spec.writeDurationParser(spec, "expireAfterWrite", "10s");
        assertEquals(10, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
    }

    // Add test cases for potential edge cases and exceptions

}