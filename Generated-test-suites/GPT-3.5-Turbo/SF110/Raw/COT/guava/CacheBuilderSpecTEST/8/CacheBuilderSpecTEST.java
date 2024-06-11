
import org.junit.Assert;
import org.junit.Test;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        Assert.assertNotNull(spec);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        Assert.assertNotNull(spec);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        Assert.assertNotNull(builder);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        String parsableString = spec.toParsableString();
        Assert.assertNotNull(parsableString);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        Assert.assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        Assert.assertEquals(spec1, spec2);
    }
}
