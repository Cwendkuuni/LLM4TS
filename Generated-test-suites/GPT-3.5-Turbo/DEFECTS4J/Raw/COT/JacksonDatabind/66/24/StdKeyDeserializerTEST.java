
import org.junit.Test;

public class StdKeyDeserializerTest {

  @Test
  public void testDeserializeKey() throws IOException {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, String.class);
    DeserializationContext context = new DeserializationContext(null, null);
    
    // Test basic functionality
    String key = "TestKey";
    Object result = deserializer.deserializeKey(key, context);
    assertEquals(key, result);
    
    // Test null key
    key = null;
    result = deserializer.deserializeKey(key, context);
    assertNull(result);
  }
  
  @Test
  public void testForType() {
    Class<?> raw = String.class;
    StdKeyDeserializer result = StdKeyDeserializer.forType(raw);
    assertNotNull(result);
    
    assertSame(raw, result.getKeyClass());
    
    raw = Object.class;
    result = StdKeyDeserializer.forType(raw);
    assertNotNull(result);
    
    assertSame(raw, result.getKeyClass());
  }
  
  @Test
  public void testDeserializeKeyWithTypeBoolean() throws IOException {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
    DeserializationContext context = new DeserializationContext(null, null);
    
    // Test valid boolean value
    String key = "true";
    Object result = deserializer.deserializeKey(key, context);
    assertEquals(Boolean.TRUE, result);
    
    // Test invalid boolean value
    key = "invalid";
    result = deserializer.deserializeKey(key, context);
    assertTrue(result instanceof JsonMappingException);
  }
  
  // Add more test cases for other methods
  
}

