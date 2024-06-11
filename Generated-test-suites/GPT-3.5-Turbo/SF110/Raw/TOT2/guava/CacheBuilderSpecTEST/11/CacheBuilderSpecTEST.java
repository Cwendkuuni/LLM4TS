import com.google.common.cache.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(10, spec.initialCapacity);
        assertEquals(100, spec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 10;
        spec.maximumSize = 100L;
        spec.concurrencyLevel = 5;
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        String str = spec.toString();
        assertNotNull(str);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testDurationInNanos() {
        Long nanos = CacheBuilderSpec.durationInNanos(10, java.util.concurrent.TimeUnit.SECONDS);
        assertNotNull(nanos);
    }
}