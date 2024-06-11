package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import java.io.IOException;

public class POJONodeTest {

 private static final Object testPojo = new TestPojo("test");

 @Test
 public void testNodeType() {
 POJONode node = new POJONode(testPojo);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode node = new POJONode(testPojo);
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode node = new POJONode(new byte[] { 1, 2, 3 });
 assertEquals(new byte[] { 1, 2, 3 }, node.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());
 POJONode nodeNull = new POJONode(null);
 assertEquals("null", nodeNull.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());
 POJONode nodeNull = new POJONode(null);
 assertEquals("null", nodeNull.asText());
 POJONode nodeEmpty = new POJONode(new String());
 assertEquals("", nodeEmpty.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode nodeTrue = new POJONode(true);
 assertTrue(nodeTrue.asBoolean(false));
 POJONode nodeFalse = new POJONode(false);
 assertFalse(nodeFalse.asBoolean(true));
 POJONode nodeNull = new POJONode(null);
 assertEquals(false, nodeNull.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode nodeIntOne = new POJONode(1);
 assertEquals(1, nodeIntOne.asInt(0));
 POJONode nodeIntMinusOne = new POJONode(-1);
 assertEquals(-1, nodeIntMinusOne.asInt(0));
 POJONode nodeNull = new POJONode(null);
 assertEquals(0, nodeNull.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode nodeLongOne = new POJONode(1L);
 assertEquals(1L, nodeLongOne.asLong(0L));
 POJONode nodeLongMinusOne = new POJONode(-1L);
 assertEquals(-1L, nodeLongMinusOne.asLong(0L));
 POJONode nodeNull = new POJONode(null);
 assertEquals(0L, nodeNull.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode nodeDoubleOne = new POJONode(1.0);
 assertEquals(1.0, nodeDoubleOne.asDouble(0.0), 0.001);
 POJONode nodeDoubleMinusOne = new POJONode(-1.0);
 assertEquals(-1.0, nodeDoubleMinusOne.asDouble(0.0), 0.001);
 POJONode nodeNull = new POJONode(null);
 assertEquals(0.0, nodeNull.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = new POJONode(testPojo);
 String json = mapper.writeValueAsString(node);
 POJOStyledSerializable pojo = new POJOStyledSerializable();
 String jsonPojo = mapper.writeValueAsString(pojo);
 assertEquals(jsonPojo, json);
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(testPojo);
 assertEquals(testPojo, node.getPojo());
 }

 @Test
 public void testEquals() {
 POJONode node1 = new POJONode(testPojo);
 POJONode node2 = new POJONode(testPojo);
 assertTrue(node1.equals(node2));
 POJONode node3 = new POJONode(new Object());
 assertFalse(node1.equals(node3));
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode(testPojo);
 POJONode node2 = new POJONode(testPojo);
 int hashCode1 = node1.hashCode();
 int hashCode2 = node2.hashCode();
 assertEquals(hashCode1, hashCode2);
 POJONode node3 = new POJONode(new Object());
 assertNotEquals(hashCode1, node3.hashCode());
 }

 @Test
 public void testToString() {
 POJONode nodeByte = new POJONode(new byte[5]);
 assertEquals("(binary value of 5 bytes)", nodeByte.toString());
 POJONode nodeRawValue = new POJONode(new RawValue("test"));
 assertEquals("(raw value 'test')", nodeRawValue.toString());
 POJONode nodeString = new POJONode("test");
 assertEquals("test", nodeString.toString());
 POJONode nodeNull = new POJONode(null);
 assertEquals("null", nodeNull.toString());
 }
}

 class TestPojo {
 public TestPojo(String s) {
 }
 }

 class POJOStyledSerializable implements JsonSerializable {
 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 // Empty implementation for testing purposes
 }
 }