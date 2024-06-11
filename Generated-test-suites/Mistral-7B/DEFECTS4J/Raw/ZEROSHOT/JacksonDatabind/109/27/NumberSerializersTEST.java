import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integer = 123;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(integer);
 assertNotNull(jsonNode);
 assertEquals(123.0, jsonNode.get(0).numberValue().doubleValue());

 // Test deserialization
 Integer deserialized = objectMapper.readValue(jsonNode.toString(), Integer.class);
 assertEquals(123, deserialized.intValue());
 }

 @Test
 public void testIntegerSerializerWithJsonFormat() throws JsonProcessingException {
 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 Integer integer = 123;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(integer);
 assertNotNull(jsonNode);
 assertEquals(123.0, jsonNode.get(0).numberValue().doubleValue());

 // Test deserialization
 Integer deserialized = objectMapper.readValue(jsonNode.toString(), Integer.class);
 assertEquals(123, deserialized.intValue());
 }

 @Test
 public void testIntegerSerializerWithCustomSchemaName() throws JsonProcessingException {
 Integer integer = 123;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsTree(integer);
 assertNotNull(jsonNode);
 assertEquals("123", jsonNode.get(0).asText());

 // Test deserialization
 Integer deserialized = objectMapper.readTree(jsonNode.toString()).getInt(0);
 assertEquals(123, deserialized.intValue());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortValue = 127;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(shortValue);
 assertNotNull(jsonNode);
 assertEquals(127.0, jsonNode.get(0).numberValue().doubleValue());

 // Test deserialization
 Short deserialized = objectMapper.readValue(jsonNode.toString(), Short.class);
 assertEquals((short) 127, deserialized.shortValue());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 123456789L;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(longValue);
 assertNotNull(jsonNode);
 assertEquals(123456789.0, jsonNode.get(0).numberValue().doubleValue());

 // Test deserialization
 Long deserialized = objectMapper.readValue(jsonNode.toString(), Long.class);
 assertEquals(123456789, deserialized.longValue());
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 123.45f;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(floatValue);
 assertNotNull(jsonNode);
 assertEquals(123.45, jsonNode.get(0).numberValue().doubleValue(), 0.001);

 // Test deserialization
 Float deserialized = objectMapper.readValue(jsonNode.toString(), Float.class);
 assertEquals(123.45f, deserialized, 0.001);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleValue = 123.456789;
 ObjectMapper objectMapper = new ObjectMapper();

 // Test serialization
 JsonNode jsonNode = objectMapper.writeValueAsTree(doubleValue);
 assertNotNull(jsonNode);
 assertEquals(123.456789, jsonNode.get(0).numberValue().doubleValue(), 0.00001);

 // Test deserialization
 Double deserialized = objectMapper.readValue(jsonNode.toString(), Double.class);
 assertEquals(123.456789, deserialized, 0.00001);
 }
}