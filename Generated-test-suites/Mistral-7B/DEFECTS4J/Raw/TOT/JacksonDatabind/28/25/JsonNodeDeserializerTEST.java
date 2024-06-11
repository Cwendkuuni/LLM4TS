import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 JsonNode deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory(), null, null);
 assertEquals(JsonNode.Value.nullValue, deserializer);
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("name", "John Doe");
 JsonNode deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper, new JsonParser(jsonObject.getJsonStream()), null);
 assertEquals(jsonObject, deserializer);
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 ArrayNode jsonArray = mapper.createArrayNode();
 jsonArray.add(JsonNode.Value.numberValue(1));
 jsonArray.add(JsonNode.Value.numberValue(2));
 JsonNode deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper, new JsonParser(jsonArray.getJsonStream()), null);
 assertEquals(jsonArray, deserializer);
 }

 @Test
 public void testDeserializeAny() throws JsonProcessingException {
 String jsonString = "{\"name\":\"John Doe\", \"age\":30}";
 JsonNode deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper, new JsonParser(new StringReader(jsonString)), null);
 assertNotNull(deserializer);
 assertEquals("name", deserializer.findValue("name").asText());
 assertEquals(30, deserializer.findValue("age").asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 String jsonString = "5{name:John}";
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode node = deserializer.deserialize(new JsonParser(new StringReader(jsonString)), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Unexpected character ('{' (code 123)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')", e.getMessage());
 }
 }
}