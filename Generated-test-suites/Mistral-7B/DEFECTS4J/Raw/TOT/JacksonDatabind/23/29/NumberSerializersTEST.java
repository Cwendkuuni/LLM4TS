import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer intValue = 123;
 String jsonString = "123";

 // Test serialization
 JsonNode result = mapper.writeValueAsTree(intValue);
 assertEquals(jsonString, result.asText());

 // Test deserialization
 Integer deserializedInt = mapper.readValue(jsonString, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer intValue = -123;
 String jsonString = "-123";

 // Test serialization
 JsonNode result = mapper.writeValueAsTree(intValue);
 assertEquals(jsonString, result.asText());

 // Test deserialization
 Integer deserializedInt = mapper.readValue(jsonString, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithInvalidInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String invalidInput = "abcd";

 // Test serialization with validation
 try {
 mapper.writeValueAsTree(invalidInput);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }

 // Test deserialization with validation
 try {
 mapper.readValue(invalidInput, Integer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Short shortValue = 127;
 String jsonString = "127";

 // Test serialization
 JsonNode result = mapper.writeValueAsTree(shortValue);
 assertEquals(jsonString, result.asText());

 // Test deserialization
 Short deserializedShort = mapper.readValue(jsonString, Short.class);
 assertEquals(shortValue, deserializedShort);
 }

 // ... Add tests for LongSerializer, FloatSerializer, and DoubleSerializer here ...
}