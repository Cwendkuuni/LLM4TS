import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100";
        CacheBuilderSpec result = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNotNull(result);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec result = CacheBuilderSpec.disableCaching();
        assertNotNull(result);
        assertEquals(result.toParsableString(), "maximumSize=0");
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse("initialCapacity=10");
        CacheBuilder<Object, Object> result = cacheBuilderSpec.toCacheBuilder();
        assertNotNull(result);
        // Add more assertions based on the expected behavior of the CacheBuilder object
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        String result = cacheBuilderSpec.toParsableString();
        assertNotNull(result);
        assertEquals(result, "maximumSize=0");
    }
    
    // Add more test cases based on the identified edge cases and exception handling scenarios
    
}