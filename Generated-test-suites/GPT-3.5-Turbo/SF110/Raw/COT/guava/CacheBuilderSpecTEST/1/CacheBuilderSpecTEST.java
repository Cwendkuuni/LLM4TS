import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        CacheBuilderSpec expectedSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(10, expectedSpec.initialCapacity.intValue());
        assertEquals(100, expectedSpec.maximumSize.longValue());
        assertEquals(5, expectedSpec.concurrencyLevel.intValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec expectedSpec = CacheBuilderSpec.disableCaching();
        assertEquals(0, expectedSpec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 10;
        spec.maximumSize = 100;
        spec.concurrencyLevel = 5;
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(5, builder.concurrencyLevel());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 10;
        spec.maximumSize = 100;
        spec.concurrencyLevel = 5;
        String expectedString = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        assertEquals(expectedString, spec.toParsableString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.initialCapacity = 10;
        spec1.maximumSize = 100;
        spec1.concurrencyLevel = 5;

        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.initialCapacity = 10;
        spec2.maximumSize = 100;
        spec2.concurrencyLevel = 5;

        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.initialCapacity = 10;
        spec1.maximumSize = 100;
        spec1.concurrencyLevel = 5;

        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.initialCapacity = 10;
        spec2.maximumSize = 100;
        spec2.concurrencyLevel = 5;

        assertTrue(spec1.equals(spec2));
    }

}