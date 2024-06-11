package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("hello world");
 assertEquals("hello world", pojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
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

 POJONode pojoNodeNegativeInt = new POJONode(-1);
 assertEquals(-1, pojoNodeNegativeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeLong = new POJONode(1L);
 assertEquals(1L, pojoNodeLong.asLong(0L));

 POJONode pojoNodeNegativeLong = new POJONode(-1L);
 assertEquals(-1L, pojoNodeNegativeLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeDouble = new POJONode(1.0);
 assertEquals(1.0, pojoNodeDouble.asDouble(0.0), 0.001);

 POJONode pojoNodeNegativeDouble = new POJONode(-1.0);
 assertEquals(-1.0, pojoNodeNegativeDouble.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 POJONode pojoNode = new POJONode("test");
 String json = mapper.writeValueAsString(pojoNode);
 assertEquals("\"test\"", json);
 }

 @Test(expected = IOException.class)
 public void testBinaryValue() throws IOException {
 POJONode pojoNode = new POJONode(new byte[0]);
 byte[] binaryValue = pojoNode.binaryValue();
 fail("Expected IOException");
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 Object pojo = pojoNode.getPojo();
 assertSame(pojoNode._value, pojo);
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));

 POJONode pojoNode3 = new POJONode("test");
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());

 POJONode pojoNode3 = new POJONode("test");
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }
}