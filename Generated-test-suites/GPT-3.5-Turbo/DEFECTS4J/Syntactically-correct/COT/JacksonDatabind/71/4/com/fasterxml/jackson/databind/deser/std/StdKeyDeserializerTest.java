import org.junit.Test;

public class StdKeyDeserializerTest {

  @Test
  public void testDeserializeKey() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
    DeserializationContext ctxt = new DeserializationContext(null, null, null);
    
    // Test basic functionality with valid key
    String key = "Jackson";
    Object result = keyDeserializer.deserializeKey(key, ctxt);
    assert(result.equals(key));

    // Test deserializing null key
    key = null;
    result = keyDeserializer.deserializeKey(key, ctxt);
    assert(result == null);
    
    // Test deserializing invalid key
    key = "123";
    boolean exceptionThrown = false;
    try {
      result = keyDeserializer.deserializeKey(key, ctxt);
    } catch (IOException e) {
      exceptionThrown = true;
    }
    assert(exceptionThrown);
  }
}