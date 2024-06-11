package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private Object createPojoNode(Object pojo) {
 return new POJONode(pojo);
 }

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = createPojoNode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = createPojoNode("test");
 assertEquals(JsonToken.VALUE_STRING, pojoNode.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3};
 POJONode pojoNode = createPojoNode(byteArray);
 byte[] result = pojoNode.binaryValue();
 assertArrayEquals(byteArray, result);
 }

 @Test(expected = IOException.class)
 public void testBinaryValueNull() throws IOException {
 POJONode pojoNode = createPojoNode(null);
 pojoNode.binaryValue();
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = createPojoNode("test");
 assertEquals("test", pojoNode.asText());
 }

 @Test
 public void testAsTextDefaultValue() {
 POJONode pojoNode = createPojoNode(null);
 assertEquals("null", pojoNode.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNode = createPojoNode(false);
 assertFalse(pojoNode.asBoolean(true));
 POJONode pojoNode2 = createPojoNode(true);
 assertTrue(pojoNode2.asBoolean(false));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNode = createPojoNode(1);
 assertEquals(1, pojoNode.asInt(0));
 POJONode pojoNode2 = createPojoNode(2.5);
 assertEquals(0, pojoNode2.asInt(1));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNode = createPojoNode(1L);
 assertEquals(1L, pojoNode.asLong(0L));
 POJONode pojoNode2 = createPojoNode(2.5);
 assertEquals(0L, pojoNode2.asLong(1L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNode = createPojoNode(1.5);
 assertEquals(1.5, pojoNode.asDouble(0.0), 0.01);
 POJONode pojoNode2 = createPojoNode(2);
 assertEquals(2.0, pojoNode2.asDouble(1.0), 0.01);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = createPojoNode("test");
 JsonNode jsonNode = JsonNodeFactory.instance.objectNode();
 POJONode jsonPojoNode = (POJONode) JsonNodeFactory.instance.textNode("test").asPOJONode();
 pojoNode.serialize(jsonNode, null);
 assertEquals(jsonPojoNode, pojoNode);
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = createPojoNode(new Object());
 assertEquals(pojoNode._value, pojoNode.getPojo());
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = createPojoNode(new Object());
 POJONode pojoNode2 = createPojoNode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testEqualsNull() {
 POJONode pojoNode = createPojoNode(new Object());
 assertFalse(pojoNode.equals(null));
 }

 @Test
 public void testEqualsDifferentClasses() {
 POJONode pojoNode = createPojoNode(new Object());
 String string = "test";
 assertFalse(pojoNode.equals(string));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = createPojoNode(new Object());
 POJONode pojoNode2 = createPojoNode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode = createPojoNode(new Object());
 assertEquals("(object Node)", pojoNode.toString());
 }
}