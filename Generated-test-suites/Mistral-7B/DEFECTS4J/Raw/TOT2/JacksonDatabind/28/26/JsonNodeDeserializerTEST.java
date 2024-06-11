import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonNode node = JsonNodeDeserializer.instance.getDeserializer(NullNode.class).deserialize(mapper.getFactory(), null, null);
 assertEquals(JsonNode.Value.nullValue(), node);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse(jsonString), null, null);
 assertInstanceOf(ObjectNode.class, node);
 ObjectNode objectNode = (ObjectNode) node;
 assertEquals("John", objectNode.get("name").asText());
 assertEquals(30, objectNode.get("age").asInt());
 assertEquals("New York", objectNode.get("city").asText());
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String jsonString = "[1, 2, 3, 4]";
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse(jsonString), null, null);
 assertInstanceOf(ArrayNode.class, node);
 ArrayNode arrayNode = (ArrayNode) node;
 assertEquals(4, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 assertEquals(4, arrayNode.get(3).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 String jsonString = "{\"name\"age\":\"John\"}"; // missing comma between fields
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse(jsonString), null, null);
 fail("Expected JsonMappingException but got: " + node);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidNodeType() throws IOException {
 String jsonString = "[1, {\"name\":\"John\"}}"; // invalid array element
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse(jsonString), null, null);
 fail("Expected JsonMappingException but got: " + node);
 }

 @Test
 public void testDeserializeEmptyJsonObject() throws IOException {
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse("{}"), null, null);
 assertEquals(JsonNode.Type.OBJECT, node.getType());
 assertNull(node.get("name"));
 }

 @Test
 public void testDeserializeEmptyJsonArray() throws IOException {
 JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.parse("[]"), null, null);
 assertEquals(JsonNode.Type.ARRAY, node.getType());
 assertTrue(node.isEmpty());
 }
}