package com.example.pojontest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testgetNodeType() throws Exception {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() throws Exception {
 POJONode node = new POJONode(new byte[0]);
 assertArrayEquals(new byte[0], node.binaryValue());

 node = new POJONode(new String("test").getBytes());
 assertArrayEquals(new byte[]{"t", "e", "s", "t"}, node.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());

 node = new POJONode(null);
 assertEquals("null", node.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());

 node = new POJONode(null);
 assertEquals("null", node.asText());

 node = new POJONode(new Object());
 assertEquals("", node.asText("default"));
 }

 @Test
 public void testAsBoolean() {
 POJONode node = new POJONode(true);
 assertTrue(node.asBoolean(false));

 node = new POJONode(false);
 assertFalse(node.asBoolean(true));

 node = new POJONode(new Boolean(true));
 assertTrue(node.asBoolean(false));

 node = new POJONode(new Boolean(false));
 assertFalse(node.asBoolean(true));

 node = new POJONode(null);
 assertFalse(node.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode node = new POJONode(1);
 assertEquals(1, node.asInt(0));

 node = new POJONode(new Integer(1));
 assertEquals(1, node.asInt(0));

 node = new POJONode(new Object());
 assertEquals(0, node.asInt(1));
 }

 @Test
 public void testAsLong() {
 POJONode node = new POJONode(1L);
 assertEquals(1, node.asLong(0));

 node = new POJONode(new Long(1));
 assertEquals(1, node.asLong(0));

 node = new POJONode(new Object());
 assertEquals(0, node.asLong(1));
 }

 @Test
 public void testAsDouble() {
 POJONode node = new POJONode(1.0);
 assertEquals(1.0, node.asDouble(0.0), 0.01);

 node = new POJONode(new Double(1.0));
 assertEquals(1.0, node.asDouble(0.0), 0.01);

 node = new POJONode(new Object());
 assertEquals(0.0, node.asDouble(1.0), 0.01);
 }

 @Test(expected = IOException.class)
 public void testSerialize() throws Exception {
 POJONode node = new POJONode(new Object());
 ObjectWriter writer = mapper.writer();
 node.serialize(writer, mapper.getSerializers());
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

 node1 = new POJONode(new Object());
 node2 = new POJONode(new String("test"));
 assertFalse(node1.equals(node2));
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertEquals(node1.hashCode(), node2.hashCode());

 node1 = new POJONode(new Object());
 node2 = new POJONode("test");
 assertNotSame(node1.hashCode(), node2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode node = new POJONode(new Object());
 assertEquals("(POJONode)", node.toString());

 node = new POJONode(new byte[0]);
 assertEquals("(binary value of 0 bytes)", node.toString());

 node = new POJONode(new String("test"));
 assertEquals("(raw value 'test')", node.toString());
 }

 @Test
 public void testSerializeWithJsonSerializable() throws JsonProcessingException {
 Object obj = new MyClass();
 POJONode node = new POJONode(obj);
 ObjectWriter writer = mapper.writer();
 String json = writer.writeValueAsString(node);
 assertEquals("{\"class\":\"com.example.MyClass\",\"value\":{\"field1\":\"value1\"," +
               "\"field2\":\"value2\"}}", json);
 }
}

class MyClass implements JsonSerializable {
 private String field1;
 private String field2;

 @Override
 public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
 generator.writeObject(this);
 }
}