package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONodeBasic() {
 POJONode node = new POJONode(new String("Hello World"));
 JsonNode jsonNode = node;

 assertEquals("Hello World", jsonNode.asText());
 assertEquals("Hello World", node.getPojo().toString());
 assertEquals(JsonNodeType.POJO, jsonNode.getNodeType());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonNode.asToken());
 }

 @Test
 public void testPOJONodeObjectSerializable() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 POJONode node = new POJONode(obj);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(node);
 MyClass deserializedObject = mapper.readValue(json, MyClass.class);
 assertEquals("Test", deserializedObject.getName());
 }

 @Test
 public void testPOJONodeDefaultValues() {
 POJONode node = new POJONode(null);
 assertNull(node.getPojo());
 assertEquals("null", node.asText());
 }

 @Test
 public void testPOJONodeBinaryValue() throws IOException {
 byte[] binaryData = new byte[] { 0x1, 0x2, 0x3 };
 POJONode node = new POJONode(binaryData);
 byte[] result = node.binaryValue();
 assertArrayEquals(binaryData, result);
 }

 @Test
 public void testPOJONodeBoolean() {
 POJONode node = new POJONode(Boolean.TRUE);
 assertTrue(node.asBoolean(false));
}

 @Test
 public void testPOJONodeInt() {
 POJONode node = new POJONode(42);
 assertEquals(42, node.asInt(0));
 }

 @Test
 public void testPOJONodeLong() {
 POJONode node = new POJONode(42L);
 assertEquals(42L, node.asLong(0L));
 }

 @Test
 public void testPOJONodeDouble() {
 POJONode node = new POJONode(3.14);
 assertEquals(3.14, node.asDouble(0), 0.001);
 }

 @Test
 public void testPOJONodeEquals() {
 POJONode node1 = new POJONode(new String("Hello World"));
 POJONode node2 = new POJONode(new String("Hello World"));
 POJONode node3 = new POJONode(new Integer(42));

 assertEquals(node1, node1);
 assertEquals(node1, node2);
 assertNotEquals(node1, node3);
 }

 @Test
 public void testPOJONodeHashCode() {
 POJONode node1 = new POJONode(new String("Hello World"));
 POJONode node2 = new POJONode(new String("Hello World"));
 POJONode node3 = new POJONode(new Integer(42));
 int hashCode1 = node1.hashCode();
 int hashCode2 = node2.hashCode();
 int hashCode3 = node3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testPOJONodeToString() {
 POJONode node = new POJONode(new String("Hello World"));
 String result = node.toString();
 assertTrue(result.startsWith("(value of "));
 assertTrue(result.endsWith(")"));
 }
}

 class MyClass {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}