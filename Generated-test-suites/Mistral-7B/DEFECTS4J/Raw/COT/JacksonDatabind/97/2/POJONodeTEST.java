package com.example.pojontest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testGetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode pojoNode = new POJONode(new byte[5]);
 byte[] result = pojoNode.binaryValue();
 assertArrayEquals(new byte[5], result);
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 }

 @Test
 public void testAsTextDefaultValue() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 POJONode pojoNodeNull = new POJONode(null);
 assertEquals("null", pojoNodeNull.asText());
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeTrue = new POJONode(true);
 assertTrue(pojoNodeTrue.asBoolean(false));
 POJONode pojoNodeFalse = new POJONode(false);
 assertFalse(pojoNodeFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeInt = new POJONode(1);
 assertEquals(1, pojoNodeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeLong = new POJONode(1L);
 assertEquals(1L, pojoNodeLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeDouble = new POJONode(1.0);
 assertEquals(1.0, pojoNodeDouble.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 POJONode pojoNode = new POJONode(new Object());
 String json = mapper.writeValueAsString(pojoNode);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(JsonNodeType.POJO, jsonNode.getNodeType());
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo().getClass(), new Object().getClass());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueException() throws IOException {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 pojoNode.binaryValue();
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode("Hello World");
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 POJONode pojoNode3 = new POJONode("Hello World");
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 assertTrue(pojoNode.toString().contains("POJONode"));
 }

}