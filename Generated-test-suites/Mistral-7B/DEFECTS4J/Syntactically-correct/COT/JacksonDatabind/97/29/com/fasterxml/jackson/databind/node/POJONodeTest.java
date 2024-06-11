package com.example.POJONodeTest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

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
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("null", pojoNode.asText());

 String value = new String("Test");
 pojoNode = new POJONode(value);
 assertEquals(value, pojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("DefaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNode = new POJONode(true);
 assertTrue(pojoNode.asBoolean(false));

 pojoNode = new POJONode(false);
 assertFalse(pojoNode.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNode = new POJONode(1);
 assertEquals(1, pojoNode.asInt(0));

 pojoNode = new POJONode(-1);
 assertEquals(-1, pojoNode.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNode = new POJONode(1L);
 assertEquals(1L, pojoNode.asLong(0L));

 pojoNode = new POJONode(-1L);
 assertEquals(-1L, pojoNode.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNode = new POJONode(1.0);
 assertEquals(1.0, pojoNode.asDouble(0.0), 0.001);

 pojoNode = new POJONode(-1.0);
 assertEquals(-1.0, pojoNode.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 String json = mapper.writeValueAsString(pojoNode);
 assertTrue(json.contains("null"));

 String value = "Test";
 pojoNode = new POJONode(value);
 json = mapper.writeValueAsString(pojoNode);
 assertEquals(value, json);
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo().getClass(), pojoNode.getPojo().getClass());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueWithNonByteArray() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.binaryValue();
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));

 POJONode pojoNode3 = new POJONode(new Object());
 assertFalse(pojoNode1.equals(pojoNode3));

 POJONode pojoNode4 = new POJONode("Test".getClass());
 assertFalse(pojoNode1.equals(pojoNode4));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 POJONode pojoNode1 = new POJONode(obj1);
 POJONode pojoNode2 = new POJONode(obj2);
 assertEquals(obj1.hashCode(), pojoNode1.hashCode());
 assertEquals(obj2.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new byte[10]);
 assertEquals("(binary value of 10 bytes)", pojoNode.toString());

 String value = "Test"; pojoNode = new POJONode(value);
 assertEquals("(raw value 'Test')", pojoNode.toString());
 }
}