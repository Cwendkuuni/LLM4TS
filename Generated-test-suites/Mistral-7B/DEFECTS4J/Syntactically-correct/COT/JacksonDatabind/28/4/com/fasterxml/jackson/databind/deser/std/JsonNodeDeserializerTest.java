package com.example.jsonnodedeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonNodeDeserializer jsonNodeDeserializer;
 private JsonNodeFactory nodeFactory;
 private DeserializationContext context;
 private ObjectMapper mapper;

 @BeforeClass
 public void setup() throws Exception {
 jsonNodeDeserializer = JsonNodeDeserializer.instance;
 nodeFactory = mapper.getNodeFactory();
 context = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerFinder(), null);
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 JsonNode result = jsonNodeDeserializer.deserialize(null, context);
 assertEquals(JsonNode.Value.nullValue(), result);
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 JsonNode result = jsonNodeDeserializer.deserialize(parser, context);
 ObjectNode obj = (ObjectNode) result;
 assertEquals("John", obj.get("name").asText());
 assertEquals(30, obj.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[\"apple\", \"banana\", 1, 2.5]");
 ArrayNode arr = (ArrayNode) jsonNodeDeserializer.deserialize(parser, context);
 assertEquals(4, arr.size());
 assertEquals("apple", arr.get(0).asText());
 assertEquals("banana", arr.get(1).asText());
 assertEquals(1, arr.get(2).asInt());
 assertEquals(2.5, arr.get(3).asDouble(), 0.001);
 }

 @Test
 public void testDeserializeComplexJson() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30,\"address\": {\"street\": \"123 Main St\", \"city\": \"Anytown\", \"state\": \"CA\", \"zip\": 12345}}");
 ObjectNode obj = (ObjectNode) jsonNodeDeserializer.deserializeObject(parser, context, nodeFactory);
 assertEquals("John", obj.get("name").asText());
 assertEquals(30, obj.get("age").asInt());
 ObjectNode address = (ObjectNode) obj.get("address");
 assertEquals("123 Main St", address.get("street").asText());
 assertEquals("Anytown", address.get("city").asText());
 assertEquals("CA", address.get("state").asText());
 assertEquals(12345, address.get("zip").asInt());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30,\"address\": [1, 2, 3]}");
 jsonNodeDeserializer.deserializeObject(parser, context, nodeFactory);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidToken() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 parser.nextToken(); // consume start object token
 parser.nextToken(); // consume field name token
 parser.nextToken(); // consume invalid token (e.g. 'X')
 jsonNodeDeserializer.deserializeAny(parser, context, nodeFactory);
 }
}