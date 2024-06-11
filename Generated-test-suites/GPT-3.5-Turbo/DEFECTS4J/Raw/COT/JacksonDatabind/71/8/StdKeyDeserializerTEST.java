import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

  @Test
  public void testDeserializeKey() throws Exception {
    String key = "test";
    DeserializationContext ctxt = new DeserializationContext(null, null);
    StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, String.class);
    
    Object result = deserializer.deserializeKey(key, ctxt);
    
    assertEquals(key, result);
  }

  @Test
  public void test_forType() throws Exception {
    Class<?> raw = String.class;
    StdKeyDeserializer result = StdKeyDeserializer.forType(raw);
    
    assertNotNull(result);
    assertEquals(String.class, result.getKeyClass());
  }

  @Test
  public void test_parse() throws Exception {
    String key = "test";
    DeserializationContext ctxt = new DeserializationContext(null, null);
    StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
    
    Object result = deserializer._parse(key, ctxt);
    
    assertEquals(key, result);
  }
  
  // Additional Test cases for potential edge cases and exceptions
  
  @Test
  public void test_deserializeKey_withNullKey() throws Exception {
    String key = null;
    DeserializationContext ctxt = new DeserializationContext(null, null);
    StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
    
    Object result = deserializer.deserializeKey(key, ctxt);
    
    assertNull(result);
  }
  
  @Test(expected = Exception.class)
  public void test_deserializeKey_withInvalidKey() throws Exception {
    String key = "123";
    DeserializationContext ctxt = new DeserializationContext(null, null);
    StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
    
    Object result = deserializer.deserializeKey(key, ctxt);
  }
  
  @Test(expected = Exception.class)
  public void test_forType_withInvalidType() throws Exception {
    Class<?> raw = Object.class;
    
    StdKeyDeserializer result = StdKeyDeserializer.forType(raw);
  }

}