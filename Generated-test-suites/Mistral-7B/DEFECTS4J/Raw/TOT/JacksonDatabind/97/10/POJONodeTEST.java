import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testPOJONodeWithNullValue() throws JsonGenerationException, JsonMappingException {
 POJONode pojoNode = new POJONode(null);
 JsonNode jsonNode = pojoNode.serialize();
 assertNull(jsonNode.asText());
 }

 @Test
 public void testPOJONodeWithBooleanValue() throws JsonGenerationException, JsonMappingException {
 POJONode pojoNode = new POJONode(true);
 JsonNode jsonNode = pojoNode.serialize();
 assertTrue(jsonNode.asBoolean(false));
 }

 @Test
 public void testPOJONodeWithNumberValue() throws JsonGenerationException, JsonMappingException {
 POJONode pojoNode = new POJONode(123);
 JsonNode jsonNode = pojoNode.serialize();
 assertEquals(123, jsonNode.asInt(0));
 }

 @Test
 public void testPOJONodeWithStringValue() throws JsonGenerationException, JsonMappingException {
 POJONode pojoNode = new POJONode("Hello World");
 JsonNode jsonNode = pojoNode.serialize();
 assertEquals("Hello World", jsonNode.asText());
 }

 @Test
 public void testPOJONodeWithJsonSerializableValue() throws JsonGenerationException, JsonMappingException {
 MyClass myClass = new MyClass("Test");
 POJONode pojoNode = new POJONode(myClass);
 JsonNode jsonNode = pojoNode.serialize();
 assertEquals("{\"name\":\"Test\"}", jsonNode.asText());
 }

 @Test(expected = JsonGenerationException.class)
 public void testPOJONodeWithNonSerializedObject() throws JsonGenerationException, JsonMappingException {
 POJONode pojoNode = new POJONode(new NonSerializableClass());
 }

 @Test
 public void testPOJONodeWithByteArrayValue() throws JsonGenerationException, JsonMappingException, IOException {
 byte[] byteArray = {1, 2, 3};
 POJONode pojoNode = new POJONode(byteArray);
 byte[] binaryData = pojoNode.binaryValue();
 assertArrayEquals(byteArray, binaryData);
 }
}

class MyClass implements JsonSerializable {
 private String name;

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("name");
 gen.writeString(name);
 }
}

class NonSerializableClass {}