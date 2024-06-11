import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, concurrencyLevel=4");
        assertEquals(100L, spec.maximumSize.longValue());
        assertEquals(4, spec.concurrencyLevel.intValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(100L, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        assertEquals("maximumSize=100, concurrencyLevel=4", spec.toParsableString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        assertEquals(spec1, spec2);
    }
}