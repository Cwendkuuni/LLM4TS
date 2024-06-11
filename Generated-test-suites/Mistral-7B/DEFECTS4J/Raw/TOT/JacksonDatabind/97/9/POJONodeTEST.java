package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 JsonNode jsonNode = objectMapper.valueToTree(pojoNode);
 String jsonString = jsonNode.asPrettyString();
 String expectedJson = "{\"POJO\":\"Hello World\"}";
 Assert.assertEquals(expectedJson, jsonString);
 }

 @Test
 public void testPOJONodeSerializationWithNullValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(null);
 JsonNode jsonNode = objectMapper.valueToTree(pojoNode);
 String jsonString = jsonNode.asPrettyString();
 String expectedJson = "null";
 Assert.assertEquals(expectedJson, jsonString);
 }

 @Test
 public void testPOJONodeSerializationWithObjectThatImplementsJsonSerializable() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 POJONode pojoNode = new POJONode(myClass);
 JsonNode jsonNode = objectMapper.valueToTree(pojoNode);
 String jsonString = jsonNode.asPrettyString();
 String expectedJson = "{\"POJO\":{\"field1\":\"value1\",\"field2\":1}}"; // replace with actual fields and values
 Assert.assertEquals(expectedJson, jsonString);
 }

 @Test(expected = JsonProcessingException.class)
 public void testPOJONodeSerializationWithNonSerializableObject() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(new NonSerializableClass());
 }

 @Test
 public void testPOJONodeDeserialization() throws JsonProcessingException {
 String jsonString = "{\"POJO\":\"Hello World\"}";
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 POJONode pojoNode = jsonNode.has("POJO") ? objectMapper.treeToValue(jsonNode.get("POJO"), String.class) : null;
 Assert.assertNotNull(pojoNode);
 Assert.assertEquals("Hello World", pojoNode.getPojo());
 }

 @Test(expected = JsonProcessingException.class)
 public void testPOJONodeDeserializationWithInvalidJson() throws JsonProcessingException {
 String jsonString = "{\"POJO\":}"; // missing value
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 POJONode pojoNode = jsonNode.has("POJO") ? objectMapper.treeToValue(jsonNode.get("POJO"), POJONode.class) : null;
 }
}

class MyClass implements JsonSerializable {
 private String field1;
 private int field2;

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeObject("{\"field1\":\"" + field1 + "\",\"field2\":\"" + field2 + "\"}");
 }
}

class NonSerializableClass {
 // this class should not be serializable
}