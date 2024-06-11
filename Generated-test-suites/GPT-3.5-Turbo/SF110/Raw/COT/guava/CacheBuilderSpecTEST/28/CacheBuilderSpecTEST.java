
import com.google.common.cache.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(10, (int) spec.initialCapacity);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long) spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=1000");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(1000L, (long) builder.maximumSize());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=1000");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=10,maximumSize=1000", parsableString);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=1000");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=1000");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=1000");
        assertEquals(spec1, spec2);
    }
}
