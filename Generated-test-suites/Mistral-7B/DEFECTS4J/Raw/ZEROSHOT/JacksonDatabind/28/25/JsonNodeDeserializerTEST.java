import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullValue() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonNode node = deserializer.deserialize(parser, new DeserializationContext(), null);
 assertEquals(NullNode.getInstance(), node);
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John Doe\"}");
 ObjectNode objectNode = deserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 assertEquals("John Doe", objectNode.get("name").asText());
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 ArrayNode arrayNode = deserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testDeserializeAny() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John Doe\", \"age\": 30 }");
 JsonNode node = deserializer.deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 assertNotNull(node);
 JsonNode nameNode = node.get("name");
 assertEquals("John Doe", nameNode.asText());
 JsonNode ageNode = node.get("age");
 assertEquals(30, ageNode.asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("{ invalid: 1}");
 deserializer.deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArrayWithObject() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("[{\"name\": \"John Doe\"}]");
 deserializer.deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeObjectWithArray() throws JsonProcessingException {
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John Doe\", \"ages\": [1, 2, 3]}");
 ObjectNode objectNode = (ObjectNode) deserializer.deserialize(parser, new DeserializationContext(), mapper.getNodeFactory());
 assertEquals("John Doe", objectNode.get("name").asText());
 ArrayNode agesNode = (ArrayNode) objectNode.get("ages");
 assertEquals(3, agesNode.size());
 assertEquals(1, agesNode.get(0).asInt());
 assertEquals(2, agesNode.get(1).asInt());
 assertEquals(3, agesNode.get(2).asInt());
 }
}