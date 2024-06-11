import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    // Test case for parse method
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");

        assertNotNull(spec);
        assertEquals(10, (int) spec.initialCapacity);
    }
    
    // Test case for disableCaching method
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

        assertNotNull(spec);
        assertEquals(0L, (long) spec.maximumSize);
    }

    // Test case for toCacheBuilder method
    
    @Test
    public void testToCacheBuilder_WithValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=1000, concurrencyLevel=4");

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertNotNull(builder);
        assertEquals(1000L, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
    }
    
    @Test
    public void testToCacheBuilder_WithoutValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertNotNull(builder);
        assertNull(builder.maximumSize());
        assertNull(builder.concurrencyLevel());
    }

    // Test case for toParsableString method
    
    @Test
    public void testToParsableString_WithValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=1000, concurrencyLevel=4");

        String parsableString = spec.toParsableString();

        assertEquals("maximumSize=1000, concurrencyLevel=4", parsableString);
    }
    
    @Test
    public void testToParsableString_WithoutValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");

        String parsableString = spec.toParsableString();

        assertEquals("", parsableString);
    }
}