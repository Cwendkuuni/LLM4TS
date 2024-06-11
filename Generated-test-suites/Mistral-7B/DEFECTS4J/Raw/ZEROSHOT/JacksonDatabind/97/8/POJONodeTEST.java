package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @BeforeClass
 public void setUp() {
 objectMapper = new ObjectMapper();
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
 POJONode pojoNode = new POJONode(new byte[] { 0x01, 0x02, 0x03 });
 assertEquals(new byte[] { 0x01, 0x02, 0x03 }, pojoNode.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode1 = new POJONode("Test String");
 assertEquals("Test String", pojoNode1.asText());
 POJONode pojoNode2 = new POJONode(null);
 assertEquals("null", pojoNode2.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("Default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNode1 = new POJONode(Boolean.TRUE);
 assertTrue(pojoNode1.asBoolean(false));
 POJONode pojoNode2 = new POJONode(Boolean.FALSE);
 assertFalse(pojoNode2.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNode1 = new POJONode(Integer.valueOf(123));
 assertEquals(123, pojoNode1.asInt(0));
 POJONode pojoNode2 = new POJONode(Integer.valueOf(-123));
 assertEquals(-123, pojoNode2.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNode1 = new POJONode(Long.valueOf(456L));
 assertEquals(456L, pojoNode1.asLong(0L));
 POJONode pojoNode2 = new POJONode(Long.valueOf(-456L));
 assertEquals(-456L, pojoNode2.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNode1 = new POJONode(Double.valueOf(789.123));
 assertEquals(789.123, pojoNode1.asDouble(0), 0.001);
 POJONode pojoNode2 = new POJONode(Double.valueOf(-789.123));
 assertEquals(-789.123, pojoNode2.asDouble(0), 0.001);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.serialize(jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String expectedJson = "{}"; // Replace with the expected JSON string for a POJO object
 assertEquals(expectedJson, jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo(), pojoNode._value);
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode(new Object());
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 int hashCode1 = pojoNode1.hashCode();
 int hashCode2 = pojoNode2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("{" + pojoNode.getPojo() + "}", pojoNode.toString());
 }
}