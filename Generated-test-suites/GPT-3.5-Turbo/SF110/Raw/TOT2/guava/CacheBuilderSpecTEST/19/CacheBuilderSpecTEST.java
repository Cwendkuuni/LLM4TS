import com.google.common.cache.CacheBuilder;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptyCacheBuilderSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        assertEquals("blank key-value pair", spec.toParsableString());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        assertEquals("maximumSize=0", spec.toParsableString());
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals("maximumSize=100", spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }
  
    @Test
    public void testExceptionHandling() {
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=100");
            fail("Should have thrown IllegalArgumentException");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

}