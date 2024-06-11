// Import necessary packages
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    // Test case for parse() method - typical use case
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=5,recordStats";
        CacheBuilderSpec result = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, (int)result.initialCapacity);
        assertEquals(100, (long)result.maximumSize);
        assertEquals(5, (int)result.concurrencyLevel);
        assertTrue(result.recordStats);
    }
    
    // Test case for parse() method - edge case
    @Test
    public void testParse_EmptySpecification() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec result = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertNull(result.initialCapacity);
        assertNull(result.maximumSize);
        assertNull(result.concurrencyLevel);
        assertNull(result.recordStats);
    }
    
    // Test case for toCacheBuilder() method - typical use case
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(5, builder.concurrencyLevel());
    }
    
    // Test case for toParsableString() method - typical use case
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        String result = spec.toParsableString();

        assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=5", result);
    }
    
    // Test case for hashCode() method - typical use case
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        int hashCode1 = spec1.hashCode();
        int hashCode2 = spec2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
    
    // Test case for equals() method - typical use case
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        boolean result = spec1.equals(spec2);

        assertTrue(result);
    }
    
    // Test case for equals() method - edge case
    @Test
    public void testEquals_SameObject() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        boolean result = spec.equals(spec);

        assertTrue(result);
    }
    
    // Test case for equals() method - edge case
    @Test
    public void testEquals_NullObject() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=5");

        boolean result = spec.equals(null);

        assertFalse(result);
    }
    
    // Test case for durationInNanos() method - typical use case
    @Test
    public void testDurationInNanos() {
        long duration = 1000;
        TimeUnit unit = TimeUnit.SECONDS;

        Long result = CacheBuilderSpec.durationInNanos(duration, unit);

        assertEquals(1000000000000L, (long)result);
    }
    
    // Test case for durationInNanos() method - edge case
    @Test
    public void testDurationInNanos_NullUnit() {
        long duration = 1000;
        TimeUnit unit = null;

        Long result = CacheBuilderSpec.durationInNanos(duration, unit);

        assertNull(result);
    }
}