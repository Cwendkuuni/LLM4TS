import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilderSpec;

public class CacheBuilderSpecTest {

  @Test
  public void testParse() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, concurrencyLevel=10");
    assertEquals(100, spec.maximumSize.longValue());
    assertEquals(10, spec.concurrencyLevel.intValue());
  }

  @Test
  public void testDisableCaching() {
    CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
    assertEquals(0, spec.maximumSize.longValue());
  }
  
  @Test
  public void testToCacheBuilder() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=50, maximumSize=100");
    CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
    assertNotNull(builder);
  }
  
  @Test
  public void testToParsableString() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
    String parsableString = spec.toParsableString();
    assertEquals("maximumSize=100", parsableString);
  }

  @Test
  public void testHashCode() {
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
    CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
    assertEquals(spec1.hashCode(), spec2.hashCode());
  }

  @Test
  public void testEquals() {
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse("maximumSize=100");
    CacheBuilderSpec spec2 = CacheBuilderSpec.parse("maximumSize=100");
    assertTrue(spec1.equals(spec2));
  }

}