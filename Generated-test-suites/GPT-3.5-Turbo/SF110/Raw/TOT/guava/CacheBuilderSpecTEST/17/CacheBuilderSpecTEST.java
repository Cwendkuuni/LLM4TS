import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;

public class CacheBuilderSpecTest {

    // Test the parse method with a valid cache builder specification
    @Test
    public void testParse_ValidSpecification() {
        String cacheBuilderSpecification = "initialCapacity=5, maximumSize=100, concurrencyLevel=4, expireAfterWrite=10m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        Assert.assertEquals(5, spec.initialCapacity.intValue());
        Assert.assertEquals(100, spec.maximumSize.longValue());
        Assert.assertEquals(4, spec.concurrencyLevel.intValue());
        Assert.assertEquals(10, spec.writeExpirationDuration);
        Assert.assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
    }

    // Test the parse method with an invalid cache builder specification (missing values)
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSpecification_MissingValue() {
        String cacheBuilderSpecification = "initialCapacity=, maximumSize=100, concurrencyLevel=4, expireAfterWrite=10m";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
    }

    // Test the disableCaching method
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

        Assert.assertEquals(0, spec.maximumSize.longValue());
    }

    // Test the toCacheBuilder method with all parameters
    @Test
    public void testToCacheBuilder_AllParameters() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 5;
        spec.maximumSize = 100;
        spec.concurrencyLevel = 4;
        spec.writeExpirationDuration = 10;
        spec.writeExpirationTimeUnit = TimeUnit.MINUTES;

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        Assert.assertEquals(5, builder.initialCapacity());
        Assert.assertEquals(100, builder.maximumSize());
        Assert.assertEquals(4, builder.concurrencyLevel());
        Assert.assertEquals(10, builder.expireAfterWriteDuration());
        Assert.assertEquals(TimeUnit.MINUTES, builder.expireAfterWriteTimeUnit());
    }

    // Test the toParsableString method
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        spec.initialCapacity = 5;
        spec.maximumSize = 100;
        spec.writeExpirationDuration = 10;
        spec.writeExpirationTimeUnit = TimeUnit.MINUTES;

        String parsableString = spec.toParsableString();

        Assert.assertEquals("initialCapacity=5, maximumSize=100, expireAfterWrite=10m", parsableString);
    }

}