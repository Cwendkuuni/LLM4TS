package com.example.POJONodeTest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test class for POJONode class.
 */
public class POJONodeTest {

 private ObjectMapper mapper;

 @BeforeClass
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test constructor POJONode(Object v).
 */
 @Test
 public void testPOJONodeConstructor() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 /**
 * Test getNodeType() method.
 */
 @Test
 public void testGetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 /**
 * Test asText() method.
 */
 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 }

 /**
 * Test asText(String defaultValue) method.
 */
 @Test
 public void testAsTextDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("Default Value"));
 }

 /**
 * Test asBoolean(boolean defaultValue) method.
 */
 @Test
 public void testAsBoolean() {
 POJONode pojoNodeTrue = new POJONode(true);
 POJONode pojoNodeFalse = new POJONode(false);
 assertTrue(pojoNodeTrue.asBoolean(false));
 assertFalse(pojoNodeFalse.asBoolean(true));
 }

 /**
 * Test asInt(int defaultValue) method.
 */
 @Test
 public void testAsInt() {
 POJONode pojoNodeIntValue = new POJONode(1);
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(1, pojoNodeIntValue.asInt(0));
 assertEquals(0, pojoNodeNullValue.asInt(10));
 }

 /**
 * Test asLong(long defaultValue) method.
 */
 @Test
 public void testAsLong() {
 POJONode pojoNodeLongValue = new POJONode(1L);
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(1L, pojoNodeLongValue.asLong(0L));
 assertEquals(0L, pojoNodeNullValue.asLong(10L));
 }

 /**
 * Test asDouble(double defaultValue) method.
 */
 @Test
 public void testAsDouble() {
 POJONode pojoNodeDoubleValue = new POJONode(1.5);
 POJONode pojoNodeNullValue = new POJONode(null);
 assertEquals(1.5, pojoNodeDoubleValue.asDouble(0.0), 0.001);
 assertEquals(0.0, pojoNodeNullValue.asDouble(10.5), 0.001);
 }

 /**
 * Test serialize() method.
 */
 @Test
 public void testSerialize() throws JsonProcessingException {
 JsonNode expectedNode = mapper.readTree("{\"value\": \"Hello World\"}");
 POJONode pojoNode = new POJONode("Hello World");
 StringWriter writer = new StringWriter();
 pojoNode.serialize(mapper.getFactory().getGenerator(writer), mapper.getSerializerProvider());
 JsonNode actualNode = mapper.readTree(writer.toString());
 assertEquals(expectedNode, actualNode);
 }

 /**
 * Test getPojo() method.
 */
 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo(), pojoNode._value);
 }

 /**
 * Test equals(Object o) method.
 */
 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 POJONode pojoNode3 = new POJONode("Different Object");
 assertTrue(pojoNode1.equals(pojoNode1));
 assertTrue(pojoNode1.equals(pojoNode2));
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 /**
 * Test hashCode() method.
 */
 @Test
 public void testHashCode() {
 POJNode pojoNode1 = new POJONode(new Object());
 POJNode pojoNode2 = new POJONode(new Object());
 POJNode pojoNode3 = new POJONode("Different Object");
 int hashCode1 = pojoNode1.hashCode();
 int hashCode2 = pojoNode2.hashCode();
 int hashCode3 = pojoNode3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertEquals(hashCode1, pojoNode1.hashCode());
 }

 /**
 * Test toString() method.
 */
 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("POJONode(Object)", pojoNode.toString());
 }

 /**
 * Test binaryValue() method with a byte array value.
 */
 @Test
 public void testBinaryValueByteArray() throws IOException {
 POJONode pojoNode = new POJONode(new byte[] { 0x01, 0x02, 0x03 });
 byte[] binaryData = pojoNode.binaryValue();
 assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, binaryData);
 }

 /**
 * Test binaryValue() method with a non-byte array value.
 */
 @Test(expected = IOException.class)
 public void testBinaryValueNonByteArray() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.binaryValue();
 }
}