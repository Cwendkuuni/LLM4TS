package com.example.pojontest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 POJONode node = new POJONode("Test POJO");
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals("Test POJO", node.asText());
 }

 @Test
 public void testGetNodeType() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode node = new POJONode("Test POJO");
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode node = new POJONode(new byte[] { 1, 2, 3 });
 assertArrayEquals(new byte[] { 1, 2, 3 }, node.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueNullValue() throws IOException {
 POJONode node = new POJONode(null);
 node.binaryValue();
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode("Test POJO");
 assertEquals("Test POJO", node.asText());
 }

 @Test
 public void testAsTextDefaultValue() {
 POJONode node = new POJONode("Test POJO");
 assertEquals("Test POJO", node.asText("Default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode nodeTrue = new POJONode(true);
 assertTrue(nodeTrue.asBoolean(false));
 POJONode nodeFalse = new POJONode(false);
 assertFalse(nodeFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode nodeOne = new POJONode(1);
 assertEquals(1, nodeOne.asInt(0));
 POJONode nodeMinusOne = new POJONode(-1);
 assertEquals(-1, nodeMinusOne.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode nodeLong = new POJONode(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, nodeLong.asLong(0L));
 POJONode nodeMinusLong = new POJONode(Long.MIN_VALUE);
 assertEquals(Long.MIN_VALUE, nodeMinusLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode nodeDouble = new POJONode(Double.MAX_VALUE);
 assertEquals(Double.MAX_VALUE, nodeDouble.asDouble(0D), 0.001);
 POJONode nodeMinusDouble = new POJONode(Double.MIN_VALUE);
 assertEquals(Double.MIN_VALUE, nodeMinusDouble.asDouble(0D), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 String json = "{\"name\":\"Test POJO\",\"age\":30}";
 POJONode node = mapper.readTree(json);
 StringWriter writer = new StringWriter();
 node.serialize(mapper.getFactory().createGenerator(writer), mapper.getSerializationConfig());
 assertEquals(json, writer.toString());
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(new Object());
 assertEquals(node.getPojo().getClass(), node._value.getClass());
 }

 @Test
 public void testEquals() {
 POJONode node1 = new POJONode("Test POJO 1");
 POJONode node2 = new POJONode("Test POJO 1");
 POJONode node3 = new POJONode("Test POJO 2");
 assertTrue(node1.equals(node1));
 assertTrue(node1.equals(node2));
 assertFalse(node1.equals(node3));
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode("Test POJO 1");
 POJONode node2 = new POJONode("Test POJO 1");
 POJONode node3 = new POJONode("Test POJO 2");
 int hashCode1 = node1.hashCode();
 int hashCode2 = node2.hashCode();
 int hashCode3 = node3.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }

 @Test
 public void testToString() {
 POJONode node = new POJONode(new Object());
 assertEquals("POJONode [_value=<[B@7f23c2cd>]", node.toString());
 }

}