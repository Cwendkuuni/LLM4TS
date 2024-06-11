
import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(10, spec.initialCapacity.intValue());
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.intValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(100, builder.maximumSize());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=500");
        String parsableString = spec.toParsableString();
        assertEquals("maximumWeight=500", parsableString);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10");
        assertEquals(spec1, spec2);
    }

    @Test
    public void testDurationInNanos() {
        long duration = 10;
        TimeUnit unit = TimeUnit.DAYS;
        Long nanos = CacheBuilderSpec.durationInNanos(duration, unit);
        assertEquals(864000000000L, nanos.longValue());
    }
}
