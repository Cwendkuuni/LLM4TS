import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
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
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testDurationInNanos() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        Long nanos = spec.durationInNanos(1, TimeUnit.SECONDS);
        assertEquals(Long.valueOf(1000000000), nanos);
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptySpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidKeyValuePair() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidValue() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidDurationFormat() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=10x");
    }
}