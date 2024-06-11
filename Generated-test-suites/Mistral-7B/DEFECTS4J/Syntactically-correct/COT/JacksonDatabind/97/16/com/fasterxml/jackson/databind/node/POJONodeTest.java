package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * POJONodeTest class to test POJONode methods
 */
public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 POJONode pojoNode = new POJONode("Test POJO");
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testGetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode("Test POJO");
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode pojoNode = new POJONode(new byte[] { 1, 2, 3 });
 byte[] binaryData = pojoNode.binaryValue();
 assertArrayEquals(new byte[] { 1, 2, 3 }, binaryData);
 }

 @Test(expected = IOException.class)
 public void testBinaryValueInvalidType() throws IOException {
 POJONode pojoNode = new POJONode("Test POJO");
 pojoNode.binaryValue();
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("Test POJO");
 assertEquals("Test POJO", pojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeTrue = new POJONode(Boolean.TRUE);
 assertTrue(pojoNodeTrue.asBoolean(false));
 POJONode pojoNodeFalse = new POJONode(Boolean.FALSE);
 assertFalse(pojoNodeFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeInt = new POJONode(Integer.valueOf(42));
 assertEquals(42, pojoNodeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeLong = new POJONode(Long.valueOf(42L));
 assertEquals(42L, pojoNodeLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeDouble = new POJONode(Double.valueOf(3.14));
 assertEquals(3.14, pojoNodeDouble.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 POJONode pojoNode = new POJONode("Test POJO");
 String json = mapper.writeValueAsString(pojoNode);
 assertEquals("{\"value\":\"Test POJO\"}", json);
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode("Test POJO");
 assertNotNull(pojoNode.getPojo());
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode("Test POJO 1");
 POJONode pojoNode2 = new POJONode("Test POJO 2");
 assertFalse(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode("Test POJO 1");
 assertTrue(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode("Test POJO 1");
 POJONode pojoNode2 = new POJONode("Test POJO 2");
 assertNotSame(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode("Test POJO");
 assertEquals("(value of Test POJO)", pojoNode.toString());
 }
}