package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
 POJONode pojoNodeWithByteArray = new POJONode(new byte[] { 1, 2, 3 });
 byte[] binaryValue = pojoNodeWithByteArray.binaryValue();
 assertArrayEquals(new byte[] { 1, 2, 3 }, binaryValue);

 POJONode pojoNodeWithoutByteArray = new POJONode(new Object());
 assertThrows(IOException.class, pojoNodeWithoutByteArray::binaryValue);
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 assertEquals("Hello World", pojoNode.asText());

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals("null", pojoNodeWithNullValue.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 assertEquals("Hello World", pojoNode.asText("Default Value"));

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals("Default Value", pojoNodeWithNullValue.asText("Default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeWithBooleanValue = new POJONode(Boolean.TRUE);
 assertTrue(pojoNodeWithBooleanValue.asBoolean(false));

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertFalse(pojoNodeWithNullValue.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeWithIntegerValue = new POJONode(123);
 assertEquals(123, pojoNodeWithIntegerValue.asInt(0));

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals(0, pojoNodeWithNullValue.asInt(123));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeWithLongValue = new POJONode(456L);
 assertEquals(456L, pojoNodeWithLongValue.asLong(0L));

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals(0L, pojoNodeWithNullValue.asLong(456L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeWithDoubleValue = new POJONode(3.14);
 assertEquals(3.14, pojoNodeWithDoubleValue.asDouble(0.0), 0.001);

 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals(0.0, pojoNodeWithNullValue.asDouble(3.14), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 JsonNode jsonNode = mapper.readTree(new StringReader(pojoNode.toString()));

 assertEquals(JsonNodeType.POJO, jsonNode.getNodeType());
 assertEquals(_value, jsonNode.get("value"));
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(_value, pojoNode.getPojo());
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));

 POJONode pojoNode3 = new POJONode(new Object());
 assertFalse(pojoNode1.equals(pojoNode3));

 POJONode pojoNode4 = new POJONode("Hello World");
 assertFalse(pojoNode1.equals(pojoNode4));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());

 POJONode pojoNode3 = new POJONode(new Object());
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNodeWithByteArray = new POJONode(new byte[] { 1, 2, 3 });
 assertEquals("(binary value of 3 bytes)", pojoNodeWithByteArray.toString());

 POJONode pojoNodeWithRawValue = new POJONode(new RawValue("Hello World"));
 assertEquals("(raw value 'Hello World')", pojoNodeWithRawValue.toString());

 POJONode pojoNodeWithObject = new POJONode(new Object());
 assertEquals("com.fasterxml.jackson.databind.node.POJONode@1b6d358", pojoNodeWithObject.toString());
 }
}