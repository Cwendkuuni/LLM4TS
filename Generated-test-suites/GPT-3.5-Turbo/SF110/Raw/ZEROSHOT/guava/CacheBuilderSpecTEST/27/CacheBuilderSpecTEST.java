import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CacheBuilderSpecTest {
    
    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        assertEquals("", spec.toParsableString());
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(builder);
    }
    
    @Test
    public void testToParsableString() {
        String specification = cacheBuilderSpec.toParsableString();
        assertNotNull(specification);
        assertEquals("", specification);
    }
    
    @Test
    public void testToString() {
        String str = cacheBuilderSpec.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = cacheBuilderSpec.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEquals() {
        boolean equals = cacheBuilderSpec.equals(cacheBuilderSpec);
        assertTrue(equals);
    }
    
    @Test
    public void testDurationInNanos() {
        Long duration = CacheBuilderSpec.durationInNanos(100, TimeUnit.MILLISECONDS);
        assertNotNull(duration);
        assertEquals(1000000, duration.longValue());
    }
    
}