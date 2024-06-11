import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptyCacheBuilderSpec_returnsEmptySpec() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertNotNull(spec);
        // Add more assertions as needed
    }

    @Test
    public void testDisableCaching_returnsSpecWithMaximumSizeZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.intValue());
    }

    @Test
    public void testToCacheBuilder_returnsCacheBuilderWithSpecifiedValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, writeExpirationDuration=5s");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertNotNull(builder);
        // Add assertions to verify builder configuration
    }

    // Add more test cases for each method in CacheBuilderSpec class

}