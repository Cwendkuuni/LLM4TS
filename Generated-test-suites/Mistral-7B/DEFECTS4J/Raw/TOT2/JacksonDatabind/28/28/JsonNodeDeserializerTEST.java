package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private static final JsonFactory jsonFactory = new JsonFactory();

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonFactory.createParser("null"), new ObjectMapper().getDeserializationContext());
 assertEquals(JsonNode.Value.NULL, node.getValueAsToken());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createValueInstance("John Doe"));
 expected.put("age", jsonFactory.createNumberNode(30));
 expected.put("address", jsonFactory.createObjectNode().put("city", jsonFactory.createValueInstance("New York")));
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonFactory.createParser("{\"name\":\"John Doe\",\"age\":30,\"address\":{\"city\":\"New York\"}}"), new ObjectMapper().getDeserializationContext());
 assertEquals(expected, node);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode expected = jsonFactory.createArrayNode();
 expected.add(jsonFactory.createNumberNode(1));
 expected.add(jsonFactory.createNumberNode(2));
 expected.add(jsonFactory.createNumberNode(3.5));
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonFactory.createParser(" [1, 2, 3.5]"), new ObjectMapper().getDeserializationContext());
 assertEquals(expected, node);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonNodeDeserializer.getInstance().deserialize(jsonFactory.createParser("{name:1}"), new ObjectMapper().getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingTypeHandler() throws IOException {
 JsonNodeDeserializer.getInstance().deserialize(jsonFactory.createParser("{type: \"X\", data: [1, 2, 3]}"), new ObjectMapper().getDeserializationContext());
 }

 @Test
 public void testDeserializeObjectDeserializerFactory() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode node = (ObjectNode) deserializer.deserialize(jsonFactory.createParser("{\"name\":\"John Doe\",\"age\":30}"), new ObjectMapper().getDeserializationContext());
 assertEquals("John Doe", node.get("name").asText());
 assertEquals(30, node.get("age").asInt());
 }

 @Test
 public void testDeserializeArrayDeserializerFactory() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(jsonFactory.createParser("[1, 2, 3]"), new ObjectMapper().getDeserializationContext());
 assertEquals(3, node.size());
 assertEquals(1, node.get(0).asInt());
 assertEquals(2, node.get(1).asInt());
 assertEquals(3.5, node.get(2).asDouble());
 }

 @Test
 public void testDeserializeOtherTypeDeserializerFactory() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
 String node = (String) deserializer.deserialize(jsonFactory.createParser("\"Hello World\""), new ObjectMapper().getDeserializationContext());
 assertEquals("Hello World", node);
 }
}