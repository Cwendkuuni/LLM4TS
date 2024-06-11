import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class NumberSerializersTest {

  public NumberSerializersTest() {
  }

  @Test
  public void testAddAll() {
    // Test with empty map
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    NumberSerializers.addAll(allDeserializers);

    assertTrue(allDeserializers.containsKey(Integer.class.getName()));
    assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Long.class.getName()));
    assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Byte.class.getName()));
    assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Short.class.getName()));
    assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Double.class.getName()));
    assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Float.class.getName()));
    assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));

    // Test with existing key-value pairs in the map
    allDeserializers.put("TestKey", new TestSerializer());
    NumberSerializers.addAll(allDeserializers);

    assertTrue(allDeserializers.containsKey(Integer.class.getName()));
    assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Long.class.getName()));
    assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Byte.class.getName()));
    assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Short.class.getName()));
    assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Double.class.getName()));
    assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Float.class.getName()));
    assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    assertTrue(allDeserializers.containsKey("TestKey"));
  }

  // Custom test serializer class
  class TestSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      // Do nothing
    }
  }
}