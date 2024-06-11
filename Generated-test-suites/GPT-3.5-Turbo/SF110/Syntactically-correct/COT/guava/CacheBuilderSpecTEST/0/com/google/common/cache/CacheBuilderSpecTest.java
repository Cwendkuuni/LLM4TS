import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(cacheBuilderSpec);
        assertEquals(Integer.valueOf(10), cacheBuilderSpec.initialCapacity);
        assertEquals(Long.valueOf(100), cacheBuilderSpec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        assertNotNull(cacheBuilderSpec);
        assertEquals(Long.valueOf(0), cacheBuilderSpec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("");
        cacheBuilderSpec.initialCapacity = 10;
        cacheBuilderSpec.maximumSize = 100;
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(cacheBuilder);
    }

    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(cacheBuilderSpec);
        String parsableString = cacheBuilderSpec.toParsableString();
        assertEquals(cacheBuilderSpecification, parsableString);
    }

    @Test
    public void testToString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(cacheBuilderSpec);
        String toString = cacheBuilderSpec.toString();
        assertEquals(cacheBuilderSpecification, toString);
    }

    @Test
    public void testHashCode() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec1 = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilderSpec cacheBuilderSpec2 = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(cacheBuilderSpec1.hashCode(), cacheBuilderSpec2.hashCode());
    }

    @Test
    public void testEquals() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec1 = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilderSpec cacheBuilderSpec2 = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertTrue(cacheBuilderSpec1.equals(cacheBuilderSpec2));
    }
}