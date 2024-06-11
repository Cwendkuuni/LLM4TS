import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDefaultSerializerSerialization() throws JsonProcessingException {
 StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(Integer.class);
 Integer value = 123;
 String expectedJson = "123";

 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 String key = "name";
 ObjectNode node = objectMapper.createObjectNode();
 node.put(key, value);
 String expectedJsonNode = "{\"" + key + "\":123}";

 // Test serialization with default serializers
 JsonNode actualJsonNode = stringSerializer.serialize(node, new JsonGenerator(objectMapper.getFactory()), objectMapper.getSerializerProvider());
 String actualJson = actualJsonNode.toString();

 assertEquals(expectedJson, actualJson);
 assertEquals(expectedJsonNode, actualJsonNode);

 // Test serialization with int serializer
 JsonNode intJsonNode = intSerializer.serialize(value, new JsonGenerator(objectMapper.getFactory()), objectMapper.getSerializerProvider());
 JsonNode intJson = intJsonNode.get(0);
 String intJsonString = intJson.asText();

 assertEquals(expectedJson, intJsonString);
 }

 @Test
 public void testCustomSerializerSerialization() throws JsonProcessingException {
 // Assuming we have a custom serializer for Integer type named MyIntegerSerializer
 StdSerializer<Integer> customIntSerializer = objectMapper.findValueSerializer(Integer.class, MyIntegerSerializer.class);

 Integer value = 123;
 String expectedJson = "MyCustomIntegerSerialization";

 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 String key = "name";
 ObjectNode node = objectMapper.createObjectNode();
 node.put(key, value);
 String expectedJsonNode = "{\"" + key + "\":123}";

 // Test serialization with custom serializer
 JsonNode actualJsonNode = stringSerializer.serialize(node, new JsonGenerator(objectMapper.getFactory()), objectMapper.getSerializerProvider());
 String actualJson = actualJsonNode.toString();

 assertEquals(expectedJson, actualJson);
 assertEquals(expectedJsonNode, actualJsonNode);

 // Test serialization with custom int serializer
 JsonNode intJsonNode = customIntSerializer.serialize(value, new JsonGenerator(objectMapper.getFactory()), objectMapper.getSerializerProvider());
 JsonNode intJson = intJsonNode.get(0);
 String intJsonString = intJson.asText();

 assertEquals(expectedJson, intJsonString);
 }

 @Test
 public void testSchemaGeneration() throws JsonProcessingException {
 StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(Integer.class);

 ObjectNode schemaNode = intSerializer.getSchema(objectMapper.getSerializerProvider(), null);
 String expectedSchema = "{\"type\":\"integer\"}";

 assertEquals(expectedSchema, schemaNode.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSerialization() throws JsonProcessingException {
 StdSerializer<Object> objectSerializer = objectMapper.findValueSerializer(Object.class);
 Object value = new Object();

 JsonNode actualJsonNode = objectSerializer.serialize(value, new JsonGenerator(objectMapper.getFactory()), objectMapper.getSerializerProvider());

 fail("Expected JsonMappingException, but got: " + actualJsonNode);
 }

 @Test
 public void testWrappingException() throws IOException {
 StdSerializer<Object> objectSerializer = objectMapper.findValueSerializer(Object.class);
 Object value = new Object();
 String fieldName = "name";

 try {
 objectSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new IOException("Test IOException"), value, fieldName);
 fail("Expected JsonMappingException, but got: IOException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}