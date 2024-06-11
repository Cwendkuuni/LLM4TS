import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setUp() {
        cacheBuilderSpec = new CacheBuilderSpec("test spec");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        assertNotNull(parsedSpec);
        assertEquals(Integer.valueOf(100), parsedSpec.initialCapacity);
        assertEquals(Long.valueOf(1000), parsedSpec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledSpec = CacheBuilderSpec.disableCaching();
        assertNotNull(disabledSpec);
        assertEquals(Long.valueOf(0), disabledSpec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        cacheBuilderSpec.initialCapacity = 50;
        cacheBuilderSpec.maximumSize = 500;
        
        assertNotNull(cacheBuilderSpec.toCacheBuilder());
    }

    @Test
    public void testToParsableString() {
        assertEquals("test spec", cacheBuilderSpec.toParsableString());
    }

    @Test
    public void testEqualsAndHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("test spec");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("test spec");

        assertTrue(spec1.equals(spec2));
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDuration() {
        CacheBuilderSpec spec = new CacheBuilderSpec("test spec");
        spec.parse("expireAfterAccess=10x"); // Invalid duration format
    }
}