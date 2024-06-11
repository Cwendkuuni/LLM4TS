import static org.junit.Assert.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=1000, maximumWeight=2000");
        assertEquals(10, (int) spec.initialCapacity);
        assertEquals(1000L, (long) spec.maximumSize);
        assertEquals(2000L, (long) spec.maximumWeight);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 10;
        spec.maximumSize = 1000L;
        spec.maximumWeight = 2000L;

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(1000L, builder.maximumSize());
        assertEquals(2000L, builder.maximumWeight());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 10;
        spec.maximumSize = 1000L;
        spec.maximumWeight = 2000L;

        String expected = "initialCapacity=10, maximumSize=1000, maximumWeight=2000";
        assertEquals(expected, spec.toParsableString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.initialCapacity = 10;
        spec1.maximumSize = 1000L;
        spec1.maximumWeight = 2000L;

        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.initialCapacity = 10;
        spec2.maximumSize = 1000L;
        spec2.maximumWeight = 2000L;

        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.initialCapacity = 10;
        spec1.maximumSize = 1000L;
        spec1.maximumWeight = 2000L;

        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.initialCapacity = 10;
        spec2.maximumSize = 1000L;
        spec2.maximumWeight = 2000L;

        assertEquals(spec1, spec2);
    }
}