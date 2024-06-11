import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptySpec_shouldCreateEmptyCacheBuilderSpec() {
        String spec = "";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        assertNotNull(cacheBuilderSpec);
        // Add more assertions here to verify the expected behavior
    }

    @Test
    public void testDisableCaching_shouldCreateCacheBuilderSpecWithMaximumSizeZero() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        assertNotNull(cacheBuilderSpec);
        assertEquals(0L, (long) cacheBuilderSpec.maximumSize);
        // Add more assertions here to verify the expected behavior
    }

    // Add more test cases for other public methods in CacheBuilderSpec class

    @Test
    public void testToParsableString_shouldReturnValidSpecificationString() {
        String spec = "initialCapacity=10,maximumSize=100";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        assertEquals(spec, cacheBuilderSpec.toParsableString());
    }

    // Add test cases for edge cases and exception handling scenarios

}