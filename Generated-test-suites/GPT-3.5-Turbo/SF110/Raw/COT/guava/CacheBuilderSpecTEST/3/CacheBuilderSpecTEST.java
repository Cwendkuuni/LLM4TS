import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    // 

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(10, (int)spec.initialCapacity);
        assertEquals(100L, (long)spec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long)spec.maximumSize);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        String expected = "initialCapacity=10,maximumSize=100";
        assertEquals(expected, spec.toParsableString());
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        String expected = "CacheBuilderSpec{initialCapacity=10,maximumSize=100}";
        assertEquals(expected, spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(spec1, spec2);
    }

    // 
}