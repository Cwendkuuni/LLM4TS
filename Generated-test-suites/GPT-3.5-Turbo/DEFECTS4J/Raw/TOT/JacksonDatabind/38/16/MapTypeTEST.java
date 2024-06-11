import static org.junit.Assert.*;
import org.junit.Test;

public class MapTypeTest {
  
  @Test
  public void testConstruct() {
    // Typical use case
    MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
    assertNotNull(mapType);
    
    // Edge case: rawType is null
    try {
      mapType = MapType.construct(null, new TypeBindings(), null, null, String.class, Integer.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("rawType cannot be null", e.getMessage());
    }
    
    // Edge case: keyType is null
    try {
      mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, null, Integer.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("keyType cannot be null", e.getMessage());
    }
    
    // Edge case: valueType is null
    try {
      mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("valueType cannot be null", e.getMessage());
    }
  }
  
  @Test
  public void testConstructDeprecated() {
    // Typical use case
    MapType mapType = MapType.construct(HashMap.class, String.class, Integer.class);
    assertNotNull(mapType);
    
    // Edge case: rawType is null
    try {
      mapType = MapType.construct(null, String.class, Integer.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("rawType cannot be null", e.getMessage());
    }
    
    // Edge case: keyType is null
    try {
      mapType = MapType.construct(HashMap.class, null, Integer.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("keyType cannot be null", e.getMessage());
    }
    
    // Edge case: valueType is null
    try {
      mapType = MapType.construct(HashMap.class, String.class, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("valueType cannot be null", e.getMessage());
    }
  }
  
  @Test
  public void test_narrow() {
    // Typical use case
    MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class, null, null, false);
    MapType narrowedMapType = mapType._narrow(LinkedHashMap.class);
    assertNotNull(narrowedMapType);
    assertEquals(LinkedHashMap.class, narrowedMapType.getRawClass());
    
    // Edge case: subclass is null
    try {
      narrowedMapType = mapType._narrow(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("subclass cannot be null", e.getMessage());
    }
  }
  
  // ... Other test methods for remaining methods in the class
  
}