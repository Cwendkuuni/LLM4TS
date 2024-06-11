package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.Assert.*;

public class POJONodeTest {

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 POJOClass pojo = new POJOClass("Test");
 POJONode node = new POJONode(pojo);

 // Test serialization of POJONode with normal POJO
 JsonNode jsonNode = mapper.valueToTree(node);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);
 assertEquals(expectedJsonNode, jsonNode);

 // Test serialization of POJONode with JsonSerializable POJO
 JsonSerializableJsonSerializable jsonSerializablePojo = new JsonSerializableJsonSerializable();
 POJONode jsonSerializableNode = new POJONode(jsonSerializablePojo);
 JsonNode jsonSerializableJsonNode = mapper.valueToTree(jsonSerializableNode);
 assertEquals(mapper.valueToTree(jsonSerializablePojo), jsonSerializableJsonNode);
 }

 @Test
 public void testPOJONodeBinaryValue() throws IOException {
 byte[] binaryData = new byte[]{1, 2, 3};
 POJONode nodeWithBinaryData = new POJONode(binaryData);
 byte[] actualBinaryData = nodeWithBinaryData.binaryValue();
 Assert.assertArrayEquals(binaryData, actualBinaryData);
 }

 @Test(expected = IOException.class)
 public void testPOJONodeBinaryValueNonBinaryData() {
 POJONode nodeWithNonBinaryData = new POJONode(new String("Test"));
 nodeWithNonBinaryData.binaryValue();
 }

 @Test
 public void testPOJONodeStringRepresentation() {
 POJONode nodeWithString = new POJONode("Test String");
 String actualStringRepresentation = nodeWithString.asText();
 assertEquals("Test String", actualStringRepresentation);
 }

 @Test
 public void testPOJONodeBooleanRepresentation() {
 POJONode nodeWithBoolean = new POJONode(Boolean.TRUE);
 boolean actualBooleanRepresentation = nodeWithBoolean.asBoolean(false);
 assertTrue(actualBooleanRepresentation);
 }

 @Test
 public void testPOJONodeIntRepresentation() {
 POJONode nodeWithInt = new POJONode(42);
 int actualIntRepresentation = nodeWithInt.asInt(0);
 assertEquals(42, actualIntRepresentation);
 }

 @Test
 public void testPOJONodeLongRepresentation() {
 POJONode nodeWithLong = new POJONode(42L);
 long actualLongRepresentation = nodeWithLong.asLong(0);
 assertEquals(42, actualLongRepresentation);
 }

 @Test
 public void testPOJONodeDoubleRepresentation() {
 POJONode nodeWithDouble = new POJONode(3.14);
 double actualDoubleRepresentation = nodeWithDouble.asDouble(0);
 assertEquals(3.14, actualDoubleRepresentation, 0.001);
 }

 @Test
 public void testPOJONodeEquals() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertTrue(node1.equals(node2));
 }

 @Test
 public void testPOJONodeNotEqualsDifferentObjects() {
 POJONode node1 = new POJONode(new Object());
 Object otherObject = new Object();
 assertFalse(node1.equals(otherObject));
 }

 @Test
 public void testPOJONodeNotEqualsDifferentClasses() {
 POJONode node1 = new POJONode(new Object());
 String otherString = new String("Test");
 assertFalse(node1.equals(otherString));
 }

 @Test
 public void testPOJONodeHashCode() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 int actualHashCode1 = node1.hashCode();
 int actualHashCode2 = node2.hashCode();
 assertEquals(actualHashCode1, actualHashCode2);
 }

 @Test
 public void testPOJONodeToString() {
 POJONode node = new POJONode(new Object());
 String actualToString = node.toString();
 assertTrue(actualToString.contains("POJONode"));
 }
}