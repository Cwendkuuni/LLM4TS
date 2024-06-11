
import com.google.common.cache.*;
import org.junit.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {
  
  @Test
  public void testParse() {
    String cacheBuilderSpecification = "maximumSize=100, initialCapacity=10";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
    // Assert the values correctly parsed
    Assert.assertEquals(10, spec.initialCapacity.intValue());
    Assert.assertEquals(100L, spec.maximumSize.longValue());
  }
  
  @Test
  public void testDisableCaching() {
    CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
    // Assert that maximumSize was set to 0
    Assert.assertEquals(0L, spec.maximumSize.longValue());
  }
  
  @Test
  public void testToCacheBuilder() {
    String cacheBuilderSpecification = "concurrencyLevel=4, maximumWeight=1000";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
    // Convert to CacheBuilder instance
    CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
    // Assert that the builder is correctly configured
    Assert.assertEquals(4, builder.concurrencyLevel());
    Assert.assertEquals(1000L, builder.maximumWeight());
  }
  
  @Test
  public void testToParsableString() {
    String cacheBuilderSpecification = "initialCapacity=10, weakKeys";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
    // Convert to parsable string
    String parsableString = spec.toParsableString();
    // Assert that the string represents the original specification
    Assert.assertEquals(cacheBuilderSpecification, parsableString);
  }
  
  @Test
  public void testHashCode() {
    String cacheBuilderSpecification = "maximumSize=100, initialCapacity=10";
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification);
    CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification);
    // Assert that objects with the same values have the same hash code
    Assert.assertEquals(spec1.hashCode(), spec2.hashCode());
  }
  
  @Test
  public void testEquals() {
    String cacheBuilderSpecification1 = "maximumSize=100, initialCapacity=10";
    String cacheBuilderSpecification2 = "maximumSize=200, initialCapacity=20";
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
    CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
    CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
    // Assert that objects with the same values are equal
    Assert.assertEquals(spec1, spec2);
    // Assert that objects with different values are not equal
    Assert.assertNotEquals(spec1, spec3);
  }
  
  @Test
  public void testDurationInNanos() {
    CacheBuilderSpec spec = new CacheBuilderSpec("");
    long duration = 10L;
    TimeUnit unit = TimeUnit.SECONDS;
    // Convert duration to nanoseconds
    Long durationInNanos = spec.durationInNanos(duration, unit);
    long expectedDurationInNanos = TimeUnit.SECONDS.toNanos(10L);
    // Assert that the duration is correctly converted to nanoseconds
    Assert.assertEquals(expectedDurationInNanos, durationInNanos.longValue());
  }
  
}
