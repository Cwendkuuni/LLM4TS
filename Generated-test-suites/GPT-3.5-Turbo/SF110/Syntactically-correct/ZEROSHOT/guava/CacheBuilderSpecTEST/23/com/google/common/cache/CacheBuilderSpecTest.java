import com.google.common.cache.CacheBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;

    @Before
    public void setup() {
        cacheBuilderSpec = new CacheBuilderSpec("");
    }

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize.longValue());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilder<Object, Object> builder = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        String parsableString = cacheBuilderSpec.toParsableString();
        assertNotNull(parsableString);
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
}