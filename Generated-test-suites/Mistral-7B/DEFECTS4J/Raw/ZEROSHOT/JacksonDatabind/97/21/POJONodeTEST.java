package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.util.TestBase;

public class POJONodeTest extends TestBase {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testNodeType() throws Exception {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() throws Exception {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() throws Exception {
 POJONode node = new POJONode(new byte[0]);
 assertArrayEquals(new byte[0], node.binaryValue());

 node = new POJONode("test");
 assertNull(node.binaryValue());
 }

 @Test
 public void testAsText() throws Exception {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());

 node = new POJONode(new Object());
 assertEquals("null", node.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() throws Exception {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText("defaultValue"));

 node = new POJONode(new Object());
 assertEquals("defaultValue", node.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() throws Exception {
 POJONode node = new POJONode(false);
 assertFalse(node.asBoolean(true));

 node = new POJONode(true);
 assertTrue(node.asBoolean(false));

 node = new POJONode(null);
 assertTrue(node.asBoolean(false));
 }

 @Test
 public void testAsInt() throws Exception {
 POJONode node = new POJONode(1);
 assertEquals(1, node.asInt(0));

 node = new POJONode(Integer.MAX_VALUE);
 assertEquals(Integer.MAX_VALUE, node.asInt(0));

 node = new POJONode(null);
 assertEquals(0, node.asInt(0));
 }

 @Test
 public void testAsLong() throws Exception {
 POJONode node = new POJONode(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, node.asLong(0));

 node = new POJONode(null);
 assertEquals(0, node.asLong(0));
 }

 @Test
 public void testAsDouble() throws Exception {
 POJONode node = new POJONode(3.14);
 assertEquals(3.14, node.asDouble(0), 0.001);

 node = new POJONode(null);
 assertEquals(0, node.asDouble(0), 0.001);
 }

 @Test
 public void testSerialize() throws Exception {
 JsonNode expected = objectMapper.readTree("{\"value\":\"test\"}");
 POJONode node = new POJONode("test");
 StringWriter writer = new StringWriter();
 node.serialize(objectMapper.getFactory().createGenerator(writer), objectMapper.getSerializerProvider());
 JsonNode actual = objectMapper.readTree(writer.toString());
 assertEquals(expected, actual);

 node = new POJONode(new Object());
 writer = new StringWriter();
 node.serialize(objectMapper.getFactory().createGenerator(writer), objectMapper.getSerializerProvider());
 expected = objectMapper.readTree("{\"value\":null}");
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPojo() throws Exception {
 Object obj = new Object();
 POJONode node = new POJONode(obj);
 assertEquals(obj, node.getPojo());
 }

 @Test
 public void testEquals() throws Exception {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 POJONode node3 = new POJONode("test");
 assertFalse(node1.equals(node2));
 assertFalse(node1.equals(node3));
 assertFalse(node2.equals(node3));
 assertFalse(node1.equals(null));
 assertTrue(node1.equals(node1));
 }

 @Test
 public void testHashCode() throws Exception {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 POJONode node3 = new POJONode("test");
 assertEquals(node1.hashCode(), node1.hashCode());
 assertNotEquals(node1.hashCode(), node2.hashCode());
 assertNotEquals(node1.hashCode(), node3.hashCode());
 assertNotEquals(node1.hashCode(), null.hashCode());
 }
}