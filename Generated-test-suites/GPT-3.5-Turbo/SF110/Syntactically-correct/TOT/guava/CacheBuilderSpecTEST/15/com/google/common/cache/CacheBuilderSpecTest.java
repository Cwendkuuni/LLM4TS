import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Typical use case
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(10, spec1.initialCapacity.intValue());
        assertEquals(100, spec1.maximumSize.intValue());

        // Edge case
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("");
        assertNotNull(spec2);

        // Error scenario
        try {
            CacheBuilderSpec spec3 = CacheBuilderSpec.parse("invalidKey=10");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.intValue());
    }

    @Test
    public void testToCacheBuilder() {
        // Typical use case
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        CacheBuilder<Object, Object> builder1 = spec1.toCacheBuilder();
        assertNotNull(builder1);
        assertEquals(10, builder1.initialCapacity);
        assertEquals(100, builder1.maximumSize);

        // Edge case
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder2 = spec2.toCacheBuilder();
        assertNotNull(builder2);
    }

    @Test
    public void testToParsableString() {
        // Typical use case
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        String string1 = spec1.toParsableString();
        assertEquals("initialCapacity=10,maximumSize=100", string1);

        // Edge case
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        String string2 = spec2.toParsableString();
        assertEquals("", string2);
    }

    @Test
    public void testHashCode() {
        // Typical use case
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        int hashCode1 = spec1.hashCode();
        assertNotNull(hashCode1);

        // Edge case
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        int hashCode2 = spec2.hashCode();
        assertNotNull(hashCode2);
    }

    @Test
    public void testEquals() {
        // Typical use case
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100");
        assertEquals(spec1, spec2);

        // Edge case
        CacheBuilderSpec spec3 = new CacheBuilderSpec("");
        CacheBuilderSpec spec4 = new CacheBuilderSpec("");
        assertEquals(spec3, spec4);
    }

}