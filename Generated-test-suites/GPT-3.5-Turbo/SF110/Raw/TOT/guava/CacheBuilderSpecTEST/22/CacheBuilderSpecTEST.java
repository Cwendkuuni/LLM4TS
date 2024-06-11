import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test for valid input
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(1000, spec.maximumSize.intValue());

        // Test for blank key-value pair
        try {
            CacheBuilderSpec.parse("initialCapacity=10,,maximumSize=1000");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("blank key-value pair", e.getMessage());
        }

        // Test for key-value pair with more than one equals sign
        try {
            CacheBuilderSpec.parse("initialCapacity=10=20,maximumSize=1000");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key-value pair initialCapacity=10=20 with more than one equals sign", e.getMessage());
        }

        // Test for unknown key
        try {
            CacheBuilderSpec.parse("initialCapacity=10,unknownKey=1000");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("unknown key unknownKey", e.getMessage());
        }

        // Test for valid input with single key-value pair
        spec = CacheBuilderSpec.parse("maximumSize=500");
        assertNull(spec.initialCapacity);
        assertEquals(500, spec.maximumSize.intValue());

        // Test for empty input
        spec = CacheBuilderSpec.parse("");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNull(spec.initialCapacity);
        assertEquals(0, spec.maximumSize.intValue());
    }

    @Test
    public void testToParsableString() {
        // Test for valid input
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        assertEquals("initialCapacity=10, maximumSize=1000", spec.toParsableString());

        // Test for valid input with single key-value pair
        spec = CacheBuilderSpec.parse("maximumSize=500");
        assertEquals("maximumSize=500", spec.toParsableString());

        // Test for empty input
        spec = CacheBuilderSpec.parse("");
        assertEquals("", spec.toParsableString());
    }

    @Test
    public void testToString() {
        // Test for valid input
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        assertEquals("CacheBuilderSpec{initialCapacity=10, maximumSize=1000}", spec.toString());

        // Test for valid input with single key-value pair
        spec = CacheBuilderSpec.parse("maximumSize=500");
        assertEquals("CacheBuilderSpec{maximumSize=500}", spec.toString());

        // Test for empty input
        spec = CacheBuilderSpec.parse("");
        assertEquals("CacheBuilderSpec{}", spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=500");

        assertEquals(spec1.hashCode(), spec2.hashCode());
        assertNotEquals(spec1.hashCode(), spec3.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=500");

        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));

        assertFalse(spec1.equals(null));
        assertFalse(spec1.equals(new Object()));
    }
}