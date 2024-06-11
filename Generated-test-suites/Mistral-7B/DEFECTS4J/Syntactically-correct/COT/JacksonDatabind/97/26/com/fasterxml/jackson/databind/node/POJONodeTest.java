package com.example; // replace with your test package name

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("test");
 assertEquals("test", pojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode("test");
 assertEquals("test", pojoNode.asText());
 assertEquals("null", pojoNode.asText((String) null));
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
 POJONode pojoNodeOne = new POJONode(1);
 assertEquals(1, pojoNodeOne.asInt(0));
 POJONode pojoNodeMinusOne = new POJONode(-1);
 assertEquals(-1, pojoNodeMinusOne.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeOne = new POJONode(1L);
 assertEquals(1, pojoNodeOne.asLong(0L));
 POJONode pojoNodeMinusOne = new POJONode(-1L);
 assertEquals(-1, pojoNodeMinusOne.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeOnePointFive = new POJONode(1.5);
 assertEquals(1.5, pojoNodeOnePointFive.asDouble(0), 0.001);
 POJONode pojoNodeMinusOnePointFive = new POJONode(-1.5);
 assertEquals(-1.5, pojoNodeMinusOnePointFive.asDouble(0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode("test");
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
 pojoNode.serialize(gen, new SerializerProvider().builder().build());
 assertEquals("test", gen.toString());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueThrowsIOExceptionWhenNotByteArray() {
 POJONode pojoNode = new POJONode("test");
 pojoNode.binaryValue();
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo(), new Object());
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode("test1");
 POJONode pojoNode2 = new POJONode("test1");
 assertTrue(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode("test2");
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode("test1");
 POJONode pojoNode2 = new POJONode("test1");
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 POJONode pojoNode3 = new POJONode("test2");
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }
}