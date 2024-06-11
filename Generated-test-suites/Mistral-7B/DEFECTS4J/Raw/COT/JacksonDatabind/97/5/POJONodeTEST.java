package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONodeConstructor() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testGetNodeType() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode node = new POJONode(new byte[5]);
 assertArrayEquals(new byte[5], node.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueWithNonByteArray() {
 POJONode node = new POJONode(new Object());
 node.binaryValue();
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode(new String("Hello World"));
 assertEquals("Hello World", node.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals("null", node.asText("Default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode node = new POJONode(true);
 assertTrue(node.asBoolean(false));
 }

 @Test
 public void testAsBooleanWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertFalse(node.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode node = new POJONode(1);
 assertEquals(1, node.asInt(0));
 }

 @Test
 public void testAsIntWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0, node.asInt(1));
 }

 @Test
 public void testAsLong() {
 POJONode node = new POJONode(1L);
 assertEquals(1L, node.asLong(0L));
 }

 @Test
 public void testAsLongWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0L, node.asLong(1L));
 }

 @Test
 public void testAsDouble() {
 POJONode node = new POJONode(1.5);
 assertEquals(1.5, node.asDouble(0.0), 0.001);
 }

 @Test
 public void testAsDoubleWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0.0, node.asDouble(1.5), 0.001);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 POJONode node = new POJONode(new Object());
 String json = mapper.writeValueAsString(node);
 assertNotNull(json);
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(new Object());
 assertEquals(node.getPojo().getClass(), node.getPojo().getClass());
 }

 @Test
 public void testEquals() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertTrue(node1.equals(node2));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new String("Hello World"));
 assertFalse(node1.equals(node2));
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertEquals(node1.hashCode(), node2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode node = new POJONode(new Object());
 assertTrue(node.toString().contains("POJONode"));
 }
}