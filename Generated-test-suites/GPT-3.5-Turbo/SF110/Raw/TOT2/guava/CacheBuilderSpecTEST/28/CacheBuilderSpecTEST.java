import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CacheBuilderSpecTest {

    // Test for parse method when cacheBuilderSpecification is empty
    @Test
    public void testParseEmptySpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        assertEquals("", spec.toParsableString());
    }

    // Test for parse method with valid cacheBuilderSpecification
    @Test
    public void testParseValidSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, initialCapacity=50");
        assertNotNull(spec);
        assertEquals("maximumSize=100, initialCapacity=50", spec.toParsableString());
    }

    // Test for disableCaching method
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    // Test for toCacheBuilder method
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    // Test for toString method
    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=50");
        String toString = spec.toString();
        assertNotNull(toString);
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    // Test for equals method with same object
    @Test
    public void testEqualsSameObject() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertTrue(spec.equals(spec));
    }

    // Test for equals method with different object
    @Test
    public void testEqualsDifferentObject() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=200");
        assertFalse(spec1.equals(spec2));
    }

}