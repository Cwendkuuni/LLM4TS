package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 @Test
 public void testBinaryValueNull() throws IOException {
 POJONode pojoNode = new POJONode(null);
 assertNull(pojoNode.binaryValue());
 }

 @Test
 public void testBinaryValueByteArray() throws IOException {
 byte[] byteArray = {1, 2, 3};
 POJONode pojoNode = new POJONode(byteArray);
 assertArrayEquals(byteArray, pojoNode.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueNotByteArray() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.binaryValue();
 }

 @Test
 public void testAsTextNull() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
 }

 @Test
 public void testAsTextString() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 }

 @Test
 public void testAsTextDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("", pojoNode.asText("Default Value"));
 }

 @Test
 public void testAsBooleanDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(false, pojoNode.asBoolean(true));
 }

 @Test
 public void testAsBooleanBoolean() {
 POJONode pojoNode = new POJONode(true);
 assertEquals(true, pojoNode.asBoolean(false));
 }

 @Test
 public void testAsIntDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0, pojoNode.asInt(1));
 }

 @Test
 public void testAsIntNumber() {
 POJONode pojoNode = new POJONode(new Integer(10));
 assertEquals(10, pojoNode.asInt(0));
 }

 @Test
 public void testAsLongDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0L, pojoNode.asLong(1L));
 }

 @Test
 public void testAsLongNumber() {
 POJONode pojoNode = new POJONode(new Long(20));
 assertEquals(20L, pojoNode.asLong(0L));
 }

 @Test
 public void testAsDoubleDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0.0, pojoNode.asDouble(1.0), 0.01);
 }

 @Test
 public void testAsDoubleNumber() {
 POJONode pojoNode = new POJONode(new Double(3.14));
 assertEquals(3.14, pojoNode.asDouble(0.0), 0.01);
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 JsonNode jsonNode = pojoNodeToJsonNode(null);
 String jsonString = mapper.writeValueAsString(jsonNode);
 assertEquals("null", jsonString);
 }

 @Test
 public void testSerializeJsonSerializable() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 POJONode pojoNode = new POJONode(myClass);
 String jsonString = mapper.writeValueAsString(pojoNode);
 // Assuming MyClass has a proper serialization
 // logic defined, this test should pass without errors
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException {
 Object myObject = new Object();
 POJONode pojoNode = new POJONode(myObject);
 String jsonString = mapper.writeValueAsString(pojoNode);
 // Assuming Object has a proper toString() method defined,
 // this test should pass without errors
 }

 @Test
 public void testGetPojoNull() {
 POJONode pojoNode = new POJONode(null);
 assertNull(pojoNode.getPojo());
 }

 @Test
 public void testGetPojoObject() {
 Object myObject = new Object();
 POJONode pojoNode = new POJONode(myObject);
 assertEquals(myObject, pojoNode.getPojo());
 }

 @Test
 public void testEqualsNull() {
 POJONode pojoNode1 = new POJONode(null);
 POJONode pojoNode2 = new POJONode(null);
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testEqualsSameObject() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = pojoNode1;
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertFalse(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testHashCodeSameObjects() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = pojoNode1;
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertNotSame(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToStringNull() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("(null)", pojoNode.toString());
 }

 @Test
 public void testToStringByteArray() {
 byte[] byteArray = {1, 2, 3};
 POJONode pojoNode = new POJONode(byteArray);
 assertEquals("(binary value of 3 bytes)", pojoNode.toString());
 }

 @Test
 public void testToStringRawValue() {
 String rawValue = "Hello World";
 POJONode pojoNode = new POJONode(new RawValue(rawValue));
 assertEquals("(raw value 'Hello World')", pojoNode.toString());
 }

 @Test
 public void testToStringObject() {
 Object myObject = new Object();
 POJONode pojoNode = new POJONode(myObject);
 assertEquals(myObject.toString(), pojoNode.toString());
 }

 private JsonNode pojoNodeToJsonNode(Object pojo) throws JsonProcessingException {
 return mapper.valueToTree(pojo);
 }

}