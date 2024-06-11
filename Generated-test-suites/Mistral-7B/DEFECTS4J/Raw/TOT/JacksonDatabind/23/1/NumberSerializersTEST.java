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
 JsonNode rootNode = mapper.readTree(new StringReader(jsonString));
 JsonNode intNode = root.get("123");
 JsonNode serializeInt = mapper.valueToTree(intValue);

 // Test equality
 assertEquals(intNode, serializeInt);

 // Test deserialization
 Integer deserializedInt = mapper.treeToValue(intNode, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer intValue = -123;
 String jsonString = "-123";

 // Test serialization
 JsonNode rootNode = mapper.readTree(new StringReader(jsonString));
 JsonNode intNode = root.get("-123");
 JsonNode serializeInt = mapper.valueToTree(intValue);

 // Test equality
 assertEquals(intNode, serializeInt);

 // Test deserialization
 Integer deserializedInt = mapper.treeToValue(intNode, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String jsonString = "abcd";

 // Test serialization
 try {
 mapper.writeValueAsString(new ObjectWriter(mapper).writeValueAsTree(123));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }

 // Test deserialization
 try {
 mapper.treeToValue(mapper.readTree(new StringReader(jsonString)), Integer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 // Similar test cases for other serializers: LongSerializer, ShortSerializer, IntLikeSerializer, FloatSerializer, DoubleSerializer

}