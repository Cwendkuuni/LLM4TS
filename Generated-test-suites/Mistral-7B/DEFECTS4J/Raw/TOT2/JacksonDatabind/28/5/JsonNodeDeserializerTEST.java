import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 }

 @Test
 public void testDeserializeNullValue() throws Exception {
 JsonNode node = JsonNodeDeserializer.getDeserializer(NullNode.class).deserialize(jsonFactory.createParser("null"), objectMapper.getDeserializationContext());
 assertInstanceOf(NullNode.class, node);
 }

 @Test
 public void testDeserializeObject() throws Exception {
 String json = "{\"name\":\"John\",\"age\":30}";
 JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory.createParser(json), objectMapper.getDeserializationContext());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws Exception {
 String json = "[1,2,3]";
 JsonNode node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonFactory.createParser(json), objectMapper.getDeserializationContext());
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 String json = "Invalid JSON";
 JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(jsonFactory.createParser(json), objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException for invalid JSON");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownType() throws Exception {
 String json = "{\"name\":\"John\",\"age\":30,\"unknown\":\"value\"}";
 JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(jsonFactory.createParser(json), objectMapper.getDeserializationContext());
 ObjectNode objNode = (ObjectNode) node;
 JsonNode unknownNode = objNode.get("unknown");
 assertNotNull(unknownNode);
 fail("Should throw JsonMappingException for unknown type");
 }

 @Test
 public void testDeserializeEmptyObject() throws Exception {
 JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory.createParser("{}"), objectMapper.getDeserializationContext());
 assertTrue(node.isObject());
 assertTrue(node.size() == 0);
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 JsonNode node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonFactory.createParser("[]"), objectMapper.getDeserializationContext());
 assertTrue(node.isArray());
 assertTrue(node.size() == 0);
 }
}