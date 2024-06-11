import com.google.common.cache.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = CacheBuilderSpec.parse("");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=10s");
        assertEquals(100L, (long)spec.maximumSize);
        assertEquals(10L, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long)spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec.maximumSize = 100L;
        cacheBuilderSpec.writeExpirationDuration = 10L;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.SECONDS;

        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(100L, builder.maximumSize());
        assertEquals(10L, builder.getWriteExpirationDuration());
        assertEquals(TimeUnit.SECONDS, builder.getWriteExpirationTimeUnit());
    }

    @Test
    public void testToParsableString() {
        cacheBuilderSpec.maximumSize = 100L;
        cacheBuilderSpec.writeExpirationDuration = 10L;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.SECONDS;

        String parsableString = cacheBuilderSpec.toParsableString();
        assertEquals("maximumSize=100, expireAfterWrite=10s", parsableString);
    }

    @Test
    public void testHashCode() {
        cacheBuilderSpec.maximumSize = 100L;
        cacheBuilderSpec.writeExpirationDuration = 10L;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.SECONDS;

        int hashCode = cacheBuilderSpec.hashCode();
        assertEquals(2647, hashCode);
    }

    @Test
    public void testEquals() {
        cacheBuilderSpec.maximumSize = 100L;
        cacheBuilderSpec.writeExpirationDuration = 10L;
        cacheBuilderSpec.writeExpirationTimeUnit = TimeUnit.SECONDS;

        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=10s");

        assertTrue(cacheBuilderSpec.equals(spec));
    }
}