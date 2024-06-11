import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void parse_emptyString_shouldReturnEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertEquals("", spec.toParsableString());
    }

    @Test
    public void parse_validSpec_shouldReturnSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals("initialCapacity=10,maximumSize=100", spec.toParsableString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_invalidSpec_shouldThrowIllegalArgumentException() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidSpec");
    }

    @Test
    public void disableCaching_shouldReturnSpecWithMaximumSizeZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void toCacheBuilder_nullValues_shouldReturnCacheBuilderWithDefaults() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void toParsableString_shouldReturnSpecString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        assertEquals("initialCapacity=10,maximumSize=100", spec.toParsableString());
    }

    @Test
    public void toString_shouldReturnSpecString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        assertEquals("initialCapacity=10,maximumSize=100", spec.toString());
    }

    @Test
    public void hashCode_nullValues_shouldReturnHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void equals_equalSpecs_shouldReturnTrue() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void equals_unequalSpecs_shouldReturnFalse() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertFalse(spec1.equals(spec2));
    }

    @Test
    public void durationInNanos_validValues_shouldReturnDurationInNanos() {
        Long nanos = CacheBuilderSpec.durationInNanos(1, TimeUnit.SECONDS);
        assertEquals(Long.valueOf(1000000000L), nanos);
    }
}