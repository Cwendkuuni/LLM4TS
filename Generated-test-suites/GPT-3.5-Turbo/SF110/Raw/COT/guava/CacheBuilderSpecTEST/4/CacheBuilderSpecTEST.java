import com.google.common.cache.CacheBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertNotNull(spec);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=1000");
        String parsableString = spec.toParsableString();
        assertNotNull(parsableString);
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
        assertEquals(spec1, spec2);
    }

    //Additional Test Cases for Edge Cases and Exception Handling Scenarios

    @Test
    public void testParseEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidSpec");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullValue() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyValue() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize= ");
    }

    //

    @Test
    public void testCacheBuilderSpecConstructor() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=10");
        assertNotNull(spec);
    }

    @Test
    public void testInitialCapacityParser() {
        CacheBuilderSpec.InitialCapacityParser parser = new CacheBuilderSpec.InitialCapacityParser();
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        parser.parse(spec, "initialCapacity", "10");
        assertNotNull(spec.initialCapacity);
    }

    //... Repeat test methods for each public method in the CacheBuilderSpec class

    //
}