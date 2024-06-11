
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
  
  // Test parse method for valid inputs
  @Test
  public void testParse_validInput() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000");
    assertNotNull(spec);
    assertEquals(Integer.valueOf(100), spec.initialCapacity);
    assertEquals(Long.valueOf(1000), spec.maximumSize);
  }
  
  // Test parse method for empty input
  @Test
  public void testParse_emptyInput() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("");
    assertNotNull(spec);
    assertNull(spec.initialCapacity);
    assertNull(spec.maximumSize);
  }
  
  // Test parse method for invalid key-value pair
  @Test(expected = IllegalArgumentException.class)
  public void testParse_invalidKeyValuePair() {
    CacheBuilderSpec.parse("invalidKey=100");
  }
  
  // Test toCacheBuilder method
  @Test
  public void testToCacheBuilder() {
    CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000,recordStats");
    CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
    assertNotNull(builder);
    assertEquals(100, builder.initialCapacity());
    assertEquals(1000, builder.maximumSize());
    assertTrue(builder.recordStats());
  }
  
  // Test toParsableString method
  @Test
  public void testToParsableString() {
    CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000,recordStats");
    String parsableString = spec.toParsableString();
    assertEquals("initialCapacity=100,maximumSize=1000,recordStats", parsableString);
  }
  
  // Test toString method
  @Test
  public void testToString() {
    CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100,maximumSize=1000,recordStats");
    String toString = spec.toString();
    assertEquals("CacheBuilderSpec{initialCapacity=100,maximumSize=1000,recordStats}", toString);
  }
}
