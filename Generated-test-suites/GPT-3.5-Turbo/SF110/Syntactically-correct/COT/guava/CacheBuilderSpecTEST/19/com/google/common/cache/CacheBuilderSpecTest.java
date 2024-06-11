import org.junit.Test;
import org.junit.Assert;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=100,maximumSize=1000";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        Assert.assertEquals(Integer.valueOf(100), cacheBuilderSpec.initialCapacity);
        Assert.assertEquals(Long.valueOf(1000), cacheBuilderSpec.maximumSize);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        Assert.assertEquals(Long.valueOf(0), cacheBuilderSpec.maximumSize);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        Assert.assertNotNull(cacheBuilder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        String parsableString = cacheBuilderSpec.toParsableString();
        Assert.assertEquals("initialCapacity=100,maximumSize=1000", parsableString);
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec cacheBuilderSpec1 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        Assert.assertEquals(cacheBuilderSpec1, cacheBuilderSpec2);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000");
        int hashCode = cacheBuilderSpec.hashCode();
        Assert.assertNotNull(hashCode);
    }
}