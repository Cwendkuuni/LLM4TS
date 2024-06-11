import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test typical case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(100L, spec.maximumSize.longValue());

        // Test edge case: empty input
        CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
        assertNull(emptySpec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledSpec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, disabledSpec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        // Test typical case
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=50");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=200");
        String parsableString = spec.toParsableString();
        assertEquals("maximumWeight=200", parsableString);
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=150");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=150");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("concurrencyLevel=2");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("concurrencyLevel=2");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testIntegerParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.IntegerParser parser = new CacheBuilderSpec.InitialCapacityParser();
        parser.parse(spec, "initialCapacity", "50");
        assertEquals(50, spec.initialCapacity.intValue());
    }

    @Test
    public void testLongParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.LongParser parser = new CacheBuilderSpec.MaximumSizeParser();
        parser.parse(spec, "maximumSize", "200");
        assertEquals(200L, spec.maximumSize.longValue());
    }

    @Test
    public void testKeyStrengthParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.KeyStrengthParser parser = new CacheBuilderSpec.KeyStrengthParser(LocalCache.Strength.WEAK);
        parser.parse(spec, "weakKeys", null);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }

    @Test
    public void testValueStrengthParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.ValueStrengthParser parser = new CacheBuilderSpec.ValueStrengthParser(LocalCache.Strength.SOFT);
        parser.parse(spec, "softValues", null);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }

    @Test
    public void testRecordStatsParser() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.RecordStatsParser parser = new CacheBuilderSpec.RecordStatsParser();
        parser.parse(spec, "recordStats", null);
        assertTrue(spec.recordStats);
    }

    @Test
    public void testDurationParsers() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilderSpec.DurationParser parser = new CacheBuilderSpec.AccessDurationParser();
        parser.parse(spec, "expireAfterAccess", "5h");
        assertEquals(5, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
    }
}