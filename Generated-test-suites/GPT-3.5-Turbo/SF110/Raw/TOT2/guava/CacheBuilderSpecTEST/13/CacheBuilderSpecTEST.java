import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptyCacheBuilderSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        // Add more assertions here
    }

    @Test
    public void testParse_validCacheBuilderSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,initialCapacity=50,recordStats");
        assertNotNull(spec);
        // Add more assertions here
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertNotNull(spec);
        // Add more assertions here
    }

    // Add more test cases for each method of CacheBuilderSpec class

}