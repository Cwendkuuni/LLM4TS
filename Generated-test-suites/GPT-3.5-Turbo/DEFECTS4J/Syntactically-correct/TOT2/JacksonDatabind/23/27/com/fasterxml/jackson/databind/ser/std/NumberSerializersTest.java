import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {
  
  @Test
  public void testAddAll() {
    // Initialize map for deserializers
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    
    // Add all deserializers to the map
    NumberSerializers.addAll(allDeserializers);
    
    // Test that deserializers are added correctly
    assertNotNull(allDeserializers.get(Integer.class.getName()));
    assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
    assertNotNull(allDeserializers.get(Long.class.getName()));
    assertNotNull(allDeserializers.get(Long.TYPE.getName()));
    assertNotNull(allDeserializers.get(Byte.class.getName()));
    assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
    assertNotNull(allDeserializers.get(Short.class.getName()));
    assertNotNull(allDeserializers.get(Short.TYPE.getName()));
    assertNotNull(allDeserializers.get(Float.class.getName()));
    assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    assertNotNull(allDeserializers.get(Double.class.getName()));
    assertNotNull(allDeserializers.get(Double.TYPE.getName()));
  }
  
  @Test
  public void testShortSerializer() {
    // Create ShortSerializer instance
    NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
    
    // Test serialization of Short value
    assertEquals("5", shortSerializer.serialize((short) 5, new JsonGenerator(), new ObjectMapper()));
  }
  
  // Additional test cases for other serializers can be added here
  
}