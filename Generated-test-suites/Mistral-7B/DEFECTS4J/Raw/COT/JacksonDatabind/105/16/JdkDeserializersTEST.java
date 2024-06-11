package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFind() {
 // Test finding deserializer for known classes
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertEquals(UUIDDeserializer.class, deserializer.getClass());

 deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());

 deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());

 deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());

 // Test finding deserializer for unknown class
 deserializer = JdkDeserializers.find(String.class, "java.util.ArrayList".getClass().getName());
 assertNull(deserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullRawType() {
 JdkDeserializers.find(null, UUID.class.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullClassName() {
 JdkDeserializers.find(UUID.class, null);
 }

 @Test
 public void testFindWithUnknownClassName() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.ArrayList".getClass().getName());
 assertNull(deserializer);
 }

 @Test
 public void testFromStringDeserializerIsCheckedFirst() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertSame(FromStringDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testCreateDeserializerForByteBufferWithValidInput() throws JsonProcessingException {
 ByteBuffer byteBuffer = ByteBuffer.wrap("1,2,3,4,5,6,7,8,9".getBytes());
 String jsonString = mapper.writeValueAsString(byteBuffer);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 Object result = mapper.readValue(JsonFactory.instance.createParser(new StringReader(jsonString)), deserializer.getType());
 assertArrayEquals(new byte[10], ((ByteBuffer) result).array());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateDeserializerForByteBufferWithInvalidInput() throws JsonProcessingException {
 String jsonString = mapper.writeValueAsString(new Object());
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 Object result = mapper.readValue(JsonFactory.instance.createParser(new StringReader(jsonString)), deserializer.getType());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateDeserializerForAtomicBoolean() throws JsonProcessingException {
 JsonNode node = mapper.readTree("true");
 ObjectAtomicBoolean result = mapper.treeToValue(node, AtomicBoolean.class);
 AtomicBoolean deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertEquals(result, deserializer.deserialize(mapper, null, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateDeserializerForAtomicBooleanWithInvalidInput() throws JsonProcessingException {
 JsonNode node = mapper.readTree("42");
 ObjectAtomicBoolean result = mapper.treeToValue(node, AtomicBoolean.class);
 AtomicBoolean deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateDeserializerForUUID() throws JsonProcessingException {
 JsonNode node = mapper.readTree("\"5b6d128c-1b6a-4b63-8655-67646e617461\"".getBytes());
 UUID result = mapper.treeToValue(node, UUID.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertEquals(result, deserializer.deserialize(mapper, null, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateDeserializerForUUIDWithInvalidInput() throws JsonProcessingException {
 JsonNode node = mapper.readTree("42");
 UUID result = mapper.treeToValue(node, UUID.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateDeserializerForStackTraceElement() throws JsonProcessingException {
 JsonNode node = mapper.readTree("\"MyClass$MyStaticInner.methodName(int param)\"");
 StackTraceElement result = mapper.treeToValue(node, StackTraceElement.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertEquals(result, deserializer.deserialize(mapper, null, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateDeserializerForStackTraceElementWithInvalidInput() throws JsonProcessingException {
 JsonNode node = mapper.readTree("[1,2,3]");
 StackTraceElement result = mapper.treeToValue(node, StackTraceElement.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 fail("Expected JsonMappingException");
 }
}