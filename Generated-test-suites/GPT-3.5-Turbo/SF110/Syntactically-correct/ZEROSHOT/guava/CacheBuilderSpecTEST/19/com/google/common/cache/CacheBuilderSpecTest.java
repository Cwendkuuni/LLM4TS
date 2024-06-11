import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_specificationsAreParsedCorrectly() {
        String cacheBuilderSpecification = "initialCapacity=100,maximumSize=1000,concurrencyLevel=4";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(100, (int)spec.initialCapacity);
        assertEquals(1000L, (long)spec.maximumSize);
        assertEquals(4, (int)spec.concurrencyLevel);
    }

    @Test
    public void testDisableCaching_maximumSizeIsSetToZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0L, (long)spec.maximumSize);
    }

    @Test
    public void testToCacheBuilder_builderIsConfiguredCorrectly() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=10,expireAfterWrite=1h");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(10L, builder.maximumSize());
        assertEquals(1L, builder.expireAfterWrite());
    }

    @Test
    public void testToParsableString_stringIsGeneratedCorrectly() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=1000,concurrencyLevel=4");
        String parsableString = spec.toParsableString();
        assertEquals("maximumSize=1000,concurrencyLevel=4", parsableString);
    }

    @Test
    public void testHashCode_hashCodeIsGeneratedCorrectly() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000,concurrencyLevel=4");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000,concurrencyLevel=4");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals_objectsAreEqual() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        assertEquals(spec1, spec2);
    }
    
    @Test
    public void testEquals_objectsAreNotEqual() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=200,maximumSize=1000");
        assertNotEquals(spec1, spec2);
    }
}