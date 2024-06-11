import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "maximumSize=100,concurrencyLevel=8";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(100, spec.maximumSize.intValue());
        assertEquals(8, spec.concurrencyLevel.intValue());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(Long.valueOf(0), spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10,weakKeys";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10, builder.initialCapacity());
        assertEquals(LocalCache.Strength.WEAK, builder.getKeyStrength());
    }

    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "maximumSize=100,concurrencyLevel=8";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        String parsableString = spec.toParsableString();
        assertEquals(cacheBuilderSpecification, parsableString);
    }

    @Test
    public void testEquals() {
        String cacheBuilderSpecification1 = "maximumSize=100";
        String cacheBuilderSpecification2 = "maximumSize=100";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testHashCode() {
        String cacheBuilderSpecification1 = "maximumSize=100";
        String cacheBuilderSpecification2 = "maximumSize=100";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}