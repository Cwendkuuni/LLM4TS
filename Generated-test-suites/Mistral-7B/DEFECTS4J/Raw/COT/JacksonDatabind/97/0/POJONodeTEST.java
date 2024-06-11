package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONode_Constructor() {
 POJONode pojoNode = new POJONode(new Object());
 assertNotNull(pojoNode);
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
 public void testBinaryValue_WithByteArray() {
 POJONode pojoNode = new POJONode(new byte[5]);
 assertArrayEquals(new byte[5], pojoNode.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testBinaryValue_WithNonByteArray() {
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.binaryValue();
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("test");
 assertEquals("test", pojoNode.asText());
 }

 @Test
 public void testAsText_WithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNode = new POJONode(false);
 assertFalse(pojoNode.asBoolean(true));
 POJONode pojoNodeBoolean = new POJONode(true);
 assertTrue(pojoNodeBoolean.asBoolean(false));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNode = new POJONode(0);
 assertEquals(0, pojoNode.asInt(1));
 POJONode pojoNodeInt = new POJONode(123);
 assertEquals(123, pojoNodeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNode = new POJONode(0L);
 assertEquals(0L, pojoNode.asLong(1L));
 POJONode pojoNodeLong = new POJONode(123L);
 assertEquals(123L, pojoNodeLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNode = new POJONode(0.0);
 assertEquals(0.0, pojoNode.asDouble(1.0), 0.001);
 POJONode pojoNodeDouble = new POJONode(123.123);
 assertEquals(123.123, pojoNodeDouble.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 pojoNode.serialize(gen, provider);
 String expectedJson = "{}";
 assertEquals(expectedJson, gen.getOutputContent().toString());
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo().getClass(), new Object().getClass());
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 POJONode pojoNode3 = new POJONode(new Object());
 assertEquals(pojoNode1, pojoNode1);
 assertNotEquals(pojoNode1, pojoNode2);
 assertTrue(pojoNode1.equals(pojoNode1));
 assertFalse(pojoNode1.equals(pojoNode2));
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode1.hashCode());
 assertNotEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode1 = new POJONode(new Object());
 assertEquals("POJONode(wrapper for Object)", pojoNode1.toString());
 }
}