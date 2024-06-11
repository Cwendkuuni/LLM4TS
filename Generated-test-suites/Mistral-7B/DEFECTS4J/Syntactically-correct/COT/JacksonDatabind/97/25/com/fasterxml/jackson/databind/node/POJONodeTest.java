package com.example.pojontest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test class for POJONode
 */
public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 /**
 * Test method for {@link POJONode#POJONode(Object)}
 */
 @Test
 public void testPOJONodeConstructor() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 /**
 * Test method for {@link POJONode#getNodeType()}
 */
 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 /**
 * Test method for {@link POJONode#asToken()}
 */
 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 /**
 * Test method for {@link POJONode#binaryValue()}
 */
 @Test
 public void testBinaryValue() {
 POJONode pojoNode = new POJONode(new byte[10]);
 byte[] binaryValue = pojoNode.binaryValue();
 assertNotNull(binaryValue);
 assertEquals(10, binaryValue.length);
 }

 /**
 * Test method for {@link POJONode#asText()}
 */
 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 }

 /**
 * Test method for {@link POJONode#asText(String)}
 */
 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("defaultValue"));
 }

 /**
 * Test method for {@link POJONode#asBoolean(boolean)}
 */
 @Test
 public void testAsBoolean() {
 POJONode pojoNodeTrue = new POJONode(true);
 assertTrue(pojoNodeTrue.asBoolean(false));
 POJONode pojoNodeFalse = new POJONode(false);
 assertFalse(pojoNodeFalse.asBoolean(true));
 }

 /**
 * Test method for {@link POJONode#asInt(int)}
 */
 @Test
 public void testAsInt() {
 POJONode pojoNodeIntValue = new POJONode(10);
 assertEquals(10, pojoNodeIntValue.asInt(0));
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(0, pojoNodeNullValue.asInt(10));
 }

 /**
 * Test method for {@link POJONode#asLong(long)}
 */
 @Test
 public void testAsLong() {
 POJONode pojoNodeLongValue = new POJONode(10L);
 assertEquals(10L, pojoNodeLongValue.asLong(0L));
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(0L, pojoNodeNullValue.asLong(10L));
 }

 /**
 * Test method for {@link POJONode#asDouble(double)}
 */
 @Test
 public void testAsDouble() {
 POJONode pojoNodeDoubleValue = new POJONode(10.5);
 assertEquals(10.5, pojoNodeDoubleValue.asDouble(0), 0.01);
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(0, pojoNodeNullValue.asDouble(10), 0.01);
 }

 /**
 * Test method for {@link POJONode#serialize(JsonGenerator, SerializerProvider)}
 */
 @Test
 public void testSerialize() throws JsonProcessingException {
 POJONode pojoNode = new POJONode("Hello World");
 StringWriter stringWriter = new StringWriter();
 JsonGenerator jsonGenerator = mapper.getFactory()
 .createGenerator(stringWriter);
 pojoNode.serialize(jsonGenerator, mapper.getSerializerProvider());
 String jsonString = stringWriter.toString();
 assertEquals("\"Hello World\"", jsonString);
 }

 /**
 * Test method for {@link POJONode#getPojo()}
 */
 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode._value, pojoNode.getPojo());
 }

 /**
 * Test method for {@link POJONode#equals(Object)}
 */
 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode("Hello World");
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 /**
 * Test method for {@link POJONode#hashCode()}
 */
 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 POJONode pojoNode3 = new POJONode("Hello World");
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }

 /**
 * Test method for {@link POJONode#toString()}
 */
 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("POJONode{_value=<[B@3e71d2>", pojoNode.toString());
 POJONode pojoNodeWithPojoValue = new POJONode("Hello World");
 assertEquals("POJONode{_value=Hello World}", pojoNodeWithPojoValue.toString());
 }
}