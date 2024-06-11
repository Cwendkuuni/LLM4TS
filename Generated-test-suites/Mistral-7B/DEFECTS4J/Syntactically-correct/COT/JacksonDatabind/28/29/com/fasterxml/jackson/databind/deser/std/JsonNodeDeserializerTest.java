package com.example.jsonnodedeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetDeserializer() throws Exception {
 JsonNode expectedObjectNode = mapper.readValue("{\"name\": \"John\"}", ObjectNode.class);
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode actualObjectNode = deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()));
 assertEquals(expectedObjectNode, actualObjectNode);

 JsonNode expectedArrayNode = mapper.readValue("[1, 2, 3]", ArrayNode.class);
 JsonDeserializer<JsonNode> deserializerArray = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 JsonNode actualArrayNode = deserializerArray.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()));
 assertEquals(expectedArrayNode, actualArrayNode);
 }

 @Test
 public void testGetNullValue() throws Exception {
 JsonNode deserializerNullValue = JsonNodeDeserializer.getInstance().getNullValue(new DeserializationContext(mapper.getConfig()));
 assertEquals(NullNode.getInstance(), deserializerNullValue);
 }

 @Test
 public void testDeserializeObject() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT);
 ObjectNode expectedObjectNode = mapper.readValue("{\"name\": \"John\", \"age\": 30}", ObjectNode.class);
 ObjectDeserializer objectDeserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 ObjectNode actualObjectNode = (ObjectNode) objectDeserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedObjectNode, actualObjectNode);

 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_NULL); // consume null value
 ObjectNode actualObjectNodeNullValue = (ObjectNode) objectDeserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertTrue(actualObjectNodeNullValue.isEmpty());

 parser.nextTokenId(JsonTokenId.ID_START_ARRAY); // consume start array
 JsonNode expectedArrayNode = mapper.readValue("[1, 2, 3]", ArrayNode.class);
 ArrayNode actualArrayNode = (ArrayNode) JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedArrayNode, actualArrayNode);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT);
 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextToken(); // consume value string
 parser.nextTokenId(JsonTokenId.ID_START_ARRAY); // consume start array
 parser.nextToken(); // consume value null
 parser.nextTokenId(JsonTokenId.ID_STRING); // consume value string
 parser.nextTokenId(JsonTokenId.ID_END_ARRAY); // consume end array
 parser.nextTokenId(JsonTokenId.ID_END_OBJECT); // consume end object
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 fail("Expected JsonMappingException but it didn't occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithDuplicateField() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT);
 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextToken(); // consume value string
 parser.nextToken(); // consume null token
 parser.nextToken(); // consume new field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextToken(); // consume value string
 parser.nextTokenId(JsonTokenId.ID_END_OBJECT); // consume end object
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 fail("Expected JsonMappingException but it didn't occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithDuplicateValue() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT);
 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextToken(); // consume value string
 parser.nextToken(); // consume null token
 parser.nextToken(); // consume new field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT); // consume start object
 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_STRING);
 parser.nextToken(); // consume value string
 parser.nextTokenId(JsonTokenId.ID_END_OBJECT); // consume end object
 parser.nextTokenId(JsonTokenId.ID_END_OBJECT); // consume end object
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 fail("Expected JsonMappingException but it didn't occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithInvalidToken() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_START_OBJECT);
 parser.nextToken(); // consume field name
 parser.nextTokenId(JsonTokenId.ID_NUMBER_INT); // consume invalid integer value
 parser.nextTokenId(JsonTokenId.ID_END_OBJECT); // consume end object
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 fail("Expected JsonMappingException but it didn't occur.");
 }

 @Test
 public void testDeserializeEmptyJsonObject() throws Exception {
 ObjectNode actualObjectNode = (ObjectNode) JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertTrue(actualObjectNode.isEmpty());
 }

 @Test
 public void testDeserializeEmptyJsonArray() throws Exception {
 ArrayNode actualArrayNode = (ArrayNode) JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertTrue(actualArrayNode.isEmpty());
 }

 @Test
 public void testDeserializeJsonString() throws Exception {
 TextNode expectedTextNode = mapper.readValue("\"Hello World\"", TextNode.class);
 TextNode actualTextNode = (TextNode) JsonNodeDeserializer.getInstance().deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedTextNode, actualTextNode);
 }

 @Test
 public void testDeserializeJsonNumberInt() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_NUMBER_INT);
 JsonNode expectedIntNode = mapper.readValue("1", JsonNode.class);
 JsonNode actualIntNode = JsonNodeDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedIntNode, actualIntNode);
 }

 @Test
 public void testDeserializeJsonNumberFloat() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_NUMBER_FLOAT);
 JsonNode expectedFloatNode = mapper.readValue("3.14", JsonNode.class);
 JsonNode actualFloatNode = JsonNodeDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedFloatNode, actualFloatNode);
 }

 @Test
 public void testDeserializeJsonTrue() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_TRUE);
 JsonNode expectedBooleanNode = mapper.readValue("true", JsonNode.class);
 JsonNode actualBooleanNode = JsonNodeDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedBooleanNode, actualBooleanNode);
 }

 @Test
 public void testDeserializeJsonFalse() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_FALSE);
 JsonNode expectedBooleanNode = mapper.readValue("false", JsonNode.class);
 JsonNode actualBooleanNode = JsonNodeDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedBooleanNode, actualBooleanNode);
 }

 @Test
 public void testDeserializeJsonNull() throws Exception {
 JsonParser parser = mapper.parser();
 parser.nextTokenId(JsonTokenId.ID_NULL);
 JsonNode expectedNullNode = mapper.readValue("null", JsonNode.class);
 JsonNode actualNullNode = JsonNodeDeserializer.getInstance().deserialize(parser, new DeserializationContext(mapper.getConfig()), mapper.getNodeFactory());
 assertEquals(expectedNullNode, actualNullNode);
 }
}