package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONode_Null() {
 POJONode node = new POJONode(null);
 assertNull(node.asText());
 assertFalse(node.asBoolean(false));
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals(JsonToken.VALUE_NULL, node.asToken());
 }

 @Test
 public void testPOJONode_Boolean() throws JsonProcessingException {
 POJONode node = new POJONode(true);
 assertTrue(node.asBoolean(false));
 assertEquals("true", node.asText());
 }

 @Test
 public void testPOJONode_Number() throws JsonProcessingException {
 POJONode node = new POJONode(1L);
 assertEquals(1, node.asInt(0));
 assertEquals("1", node.asText());
 }

 @Test
 public void testPOJONode_String() throws JsonProcessingException {
 POJONode node = new POJONode("Test String");
 assertEquals("Test String", node.asText());
 }

 @Test
 public void testPOJONode_Object() throws JsonProcessingException {
 TestClass testClass = new TestClass();
 testClass.setName("Test Object");
 POJONode node = new POJONode(testClass);
 assertEquals(testClass.getName(), node.asText());
 }

 @Test
 public void testPOJONode_Serialize() throws JsonProcessingException {
 POJONode node = new POJONode(new TestClass());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 node.serialize(gen, new SerializerProvider() {
 @Override
 public JsonSerializer<Object> findValueSerializer(Class<?> valueType) {
 return new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 try {
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeValue(value.toString());
 gen.writeEndObject();
 } catch (IOException e) {
 throw e;
 }
 }
 };
 }
 });
 assertEquals("{\"name\":\"[POJONode@592e8fcc]\"}", mapper.writeValueAsString(node));
 }

 @Test
 public void testPOJONode_Equals() {
 POJONode node1 = new POJONode("Test String");
 POJONode node2 = new POJONode("Test String");
 assertTrue(node1.equals(node2));
 }

 @Test
 public void testPOJONode_HashCode() {
 POJONode node1 = new POJONode("Test String");
 POJONode node2 = new POJONode("Test String");
 assertEquals(node1.hashCode(), node2.hashCode());
 }

 @Test
 public void testPOJONode_GetPojo() {
 POJONode node = new POJONode(new TestClass());
 assertSame(node.getPojo(), node._value);
 }

 @Test
 public void testPOJONode_BinaryValue() throws IOException {
 byte[] byteArray = new byte[] { 1, 2, 3 };
 POJONode node = new POJONode(byteArray);
 assertArrayEquals(byteArray, node.binaryValue());
 }
}

 class TestClass implements JsonSerializable {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(name);
 }
}