package com.example.pojonode;

import static org.junit. Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

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
 byte[] byteArray = new byte[]{1, 2, 3};
 POJONode pojoNode1 = new POJONode(byteArray);
 byte[] actual = pojoNode1.binaryValue();
 assertArrayEquals(byteArray, actual);

 POJONode pojoNode2 = new POJONode(new Object());
 assertNull(pojoNode2.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());

 POJONode pojoNodeNull = new POJONode(null);
 assertEquals("null", pojoNodeNull.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText("Default Value"));

 POJONode pojoNodeNull = new POJONode(null);
 assertEquals("Default Value", pojoNodeNull.asText());
 }

 @Test(expected = ClassCastException.class)
 public void testAsBooleanWithDefaultValueFalse() {
 POJONode pojoNode = new POJONode(1);
 assertFalse(pojoNode.asBoolean(false));
 }

 @Test(expected = ClassCastException.class)
 public void testAsBooleanWithDefaultValueTrue() {
 POJONode pojoNode = new POJONode(true);
 assertTrue(pojoNode.asBoolean(true));
 }

 @Test(expected = NumberFormatException.class)
 public void testAsIntWithDefaultValue() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals(0, pojoNode.asInt(0));
 }

 @Test
 public void testAsIntWithDefaultValuePositiveNumber() {
 POJONode pojoNode = new POJONode(1);
 assertEquals(1, pojoNode.asInt(0));
 }

 @Test
 public void testAsLongWithDefaultValue() {
 POJONode pojoNode = new POJONode(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, pojoNode.asLong(0L));
 }

 @Test
 public void testAsDoubleWithDefaultValue() {
 POJONode pojoNode = new POJONode(Double.MAX_VALUE);
 assertEquals(Double.MAX_VALUE, pojoNode.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 POJONode pojoNode = new POJONode(new Object());
 pojoNode.serialize(jsonGenerator, new SerializerProvider());
 String expected = "{\"@class\":\"POJONode\",\"_value\":{}}";
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());

 jsonGenerator.close();
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 Object actual = pojoNode.getPojo();
 assertNotNull(actual);
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));

 POJONode pojoNode3 = new POJONode("Hello World");
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 int expectedHashCode = pojoNode1.hashCode();
 int actualHashCode = pojoNode2.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 String actual = pojoNode.toString();
 assertTrue(actual.startsWith("(object)"));
 }

 @Test
 public void testToStringWithBinaryValue() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3};
 POJONode pojoNode = new POJONode(byteArray);
 String actual = pojoNode.toString();
 assertTrue(actual.startsWith("(binary value of "));
 assertEquals(3, actual.length() - actual.lastIndexOf(")") - 1);
 }

 @Test
 public void testToStringWithRawValue() {
 POJONode pojoNode = new POJONode(new RawValue("Hello World"));
 String actual = pojoNode.toString();
 assertEquals("(raw value 'Hello World')", actual);
 }
}