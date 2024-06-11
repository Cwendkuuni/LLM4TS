package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 // ------------------- Base class overrides -------------------

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
 public void testBinaryValue_Null() {
 POJONode pojoNode = new POJONode(null);
 assertNull(pojoNode.binaryValue());
 }

 @Test
 public void testBinaryValue_ByteArray() {
 POJONode pojoNode = new POJONode(new byte[] { 1, 2, 3 });
 assertArrayEquals(new byte[] { 1, 2, 3 }, pojoNode.binaryValue());
 }

 // ------------------- General type coercions ------------------

 @Test
 public void testAsText_Null() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
 }

 @Test
 public void testAsText_String() {
 POJONode pojoNode = new POJONode("Hello World");
 assertEquals("Hello World", pojoNode.asText());
 }

 @Test
 public void testAsText_DefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("Default Value"));
 }

 @Test
 public void testAsBoolean_DefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertFalse(pojoNode.asBoolean(true));
 }

 @Test
 public void testAsBoolean_Boolean() {
 POJONode pojoNode = new POJONode(true);
 assertTrue(pojoNode.asBoolean(false));
 }

 @Test
 public void testAsInt_DefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0, pojoNode.asInt(1));
 }

 @Test
 public void testAsInt_Number() {
 POJONode pojoNode = new POJONode(123);
 assertEquals(123, pojoNode.asInt(0));
 }

 @Test
 public void testAsLong_DefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0L, pojoNode.asLong(1L));
 }

 @Test
 public void testAsLong_Number() {
 POJONode pojoNode = new POJONode(123L);
 assertEquals(123L, pojoNode.asLong(0L));
 }

 @Test
 public void testAsDouble_DefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0.0, pojoNode.asDouble(1.0), 0.001);
 }

 @Test
 public void testAsDouble_Number() {
 POJONode pojoNode = new POJONode(123.45);
 assertEquals(123.45, pojoNode.asDouble(0.0), 0.001);
 }

 // ------------------- Public API, serialization ------------------

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode(new Object());
 String json = objectMapper.writeValueAsString(pojoNode);
 assertTrue(json.startsWith("{\"class\":\""));
 }

 @Test
 public void testSerializeJsonSerializable() throws IOException {
 MyClass myClass = new MyClass();
 POJONode pojoNode = new POJONode(myClass);
 String json = objectMapper.writeValueAsString(pojoNode);
 assertEquals(objectMapper.writeValueAsString(myClass), json);
 }

 @Test
 public void testSerialize_NonJsonSerializable() throws IOException {
 String json = objectMapper.writeValueAsString(new POJONode(new Object()));
 assertFalse(json.startsWith("{\"class\":\""));
 }

 // ------------------- Extended API ------------------

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo(), new Object());
 }

 // ------------------- Equals and hashCode ------------------

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
 POJONode pojoNode = new POJONode(new Object());
 Object pojo = pojoNode.getPojo();
 int hashCode1 = pojoNode.hashCode();
 Object pojo2 = new Object();
 int hashCode2 = new POJONode(pojo2).hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 // ------------------- toString ------------------

 @Test
 public void testToString_Null() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("(null)", pojoNode.toString());
 }

 @Test
 public void testToString_ByteArray() {
 POJONode pojoNode = new POJONode(new byte[] { 1, 2, 3 });
 assertEquals("(binary value of 3 bytes)", pojoNode.toString());
 }

 @Test
 public void testToString_RawValue() {
 POJONode pojoNode = new POJONode(new RawValue("Hello World"));
 assertEquals("(raw value 'Hello World')", pojoNode.toString());
 }

 @Test
 public void testToString_Object() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("Object{}", pojoNode.toString());
 }
}

class MyClass {
 // Add any necessary fields and methods here
}