import com.google.common.cache.CacheBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,recordStats");
        Assert.assertEquals(10, (int)spec.initialCapacity);
        Assert.assertEquals(100L, (long)spec.maximumSize);
        Assert.assertNull(spec.maximumWeight);
        Assert.assertNull(spec.concurrencyLevel);
        Assert.assertNull(spec.keyStrength);
        Assert.assertNull(spec.valueStrength);
        Assert.assertTrue(spec.recordStats);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        Assert.assertEquals(0L, (long)spec.maximumSize);
        Assert.assertNull(spec.maximumWeight);
        Assert.assertNull(spec.concurrencyLevel);
        Assert.assertNull(spec.keyStrength);
        Assert.assertNull(spec.valueStrength);
        Assert.assertNull(spec.recordStats);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        Assert.assertEquals(100L, builder.getMaximumSize());
        Assert.assertEquals(30L, builder.getExpireAfterWriteTimeout());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        String parsableString = spec.toParsableString();
        Assert.assertEquals("maximumSize=100,expireAfterWrite=30s", parsableString);
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        String string = spec.toString();
        Assert.assertEquals("CacheBuilderSpec{maximumSize=100,expireAfterWrite=30s}", string);
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        Assert.assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100,expireAfterWrite=30s");
        Assert.assertEquals(spec1, spec2);
    }
}