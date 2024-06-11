import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test case 1
        String cacheBuilderSpecification1 = "initialCapacity=10,maximumSize=100";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        assertEquals(Integer.valueOf(10), spec1.initialCapacity);
        assertEquals(Long.valueOf(100), spec1.maximumSize);

        // Test case 2
        String cacheBuilderSpecification2 = "weakKeys";
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        assertNull(spec2.valueStrength);
        assertEquals(LocalCache.Strength.WEAK, spec2.keyStrength);

        // Test case 3
        String cacheBuilderSpecification3 = "expireAfterWrite=10d,expireAfterAccess=1h";
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification3);
        assertEquals(10, spec3.writeExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec3.writeExpirationTimeUnit);
        assertEquals(1, spec3.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec3.accessExpirationTimeUnit);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(100, builder.maximumSize());
        assertTrue(builder.build().keyStrength().equals(LocalCache.Strength.WEAK));
        assertEquals(1, builder.expireAfterWriteNanos());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        String parsableString = spec.toParsableString();
        assertEquals("maximumSize=100,weakKeys,expireAfterWrite=1h", parsableString);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100,weakKeys,expireAfterWrite=1h");
        assertTrue(spec1.equals(spec2));
    }

}