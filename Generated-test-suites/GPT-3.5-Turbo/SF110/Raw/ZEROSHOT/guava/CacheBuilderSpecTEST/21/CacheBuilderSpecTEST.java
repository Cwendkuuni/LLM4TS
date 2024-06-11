import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LocalCache;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptyString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
    }

    @Test
    public void testParse_nonEmptyString() {
        String input = "initialCapacity=10, maximumSize=100, concurrencyLevel=5, weakKeys";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(input);
        assertEquals(10, (int) spec.initialCapacity);
        assertEquals(100L, (long) spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertEquals(5, (int) spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long) spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100L, builder.maximumSize());
        assertEquals(LocalCache.Strength.WEAK, builder.getKeyStrength());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        String parsableString = spec.toParsableString();
        assertEquals("initialCapacity=10, maximumSize=100, weakKeys", parsableString);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=100, weakKeys");
        assertEquals(spec1, spec2);
    }

}