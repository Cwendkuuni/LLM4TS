import org.junit.Assert;
import org.junit.Test;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse_EmptyCacheBuilderSpecification_ReturnsEmptyCacheBuilderSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        Assert.assertEquals("", spec.toParsableString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidKeyValuePair_ThrowsIllegalArgumentException() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=invalidValue");
    }
    
    @Test
    public void testDisableCaching_MaximumSizeSetToZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        Assert.assertEquals("maximumSize=0", spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder_AllParametersSet_CacheBuilderCreated() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000,maximumWeight=10000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterAccess=10m,expireAfterWrite=1h,refreshAfterWrite=5h");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        // Assert the parameters of the cache builder
    }
}