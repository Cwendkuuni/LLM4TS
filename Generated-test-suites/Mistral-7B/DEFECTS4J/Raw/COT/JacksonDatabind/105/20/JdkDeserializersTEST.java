package com.example.junit.tests.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindDeserializerWithValidClassName() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindDeserializerWithValidType() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, null);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindDeserializerWithInvalidClassName() throws IOException {
 Class<Integer> intClass = Integer.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(intClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithNullTypeAndClassName() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithNullType() throws IOException {
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, clsName);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithNullClassName() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, null);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDeserializerWithUnknownType() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = Integer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, clsName);
 fail("Expected an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromStringDeserializerWithNullType() throws IOException {
 JsonDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(null);
 fail("Expected an IllegalArgumentException.");
 }

 @Test
 public void testFromStringDeserializerWithValidType() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(uuidClass);
 assertNotNull(deserializer);
 }

 @Test
 public void testUuidDeserializer() throws IOException {
 Object uuid = objectMapper.readValue("\"123e4567-e89b-12d3-a456-426614174000\"", UUID.class);
 assertEquals("123e4567-e89b-12d3-a456-426614174000", uuid.toString());
 }

 @Test
 public void testStackTraceElementDeserializer() throws IOException {
 Object stackTraceElement = objectMapper.readValue("[{\"className\":\"MyClass\",\"methodName\":\"myMethod\",\"lineNumber\":123,\"fileName\":\"MyFile.java\"}]", StackTraceElement[].class);
 StackTraceElement element = stackTraceElement[0];
 assertEquals("MyClass", element.getClassName());
 assertEquals("myMethod", element.getMethodName());
 assertEquals(123, element.getLineNumber());
 assertEquals("MyFile.java", element.getFileName());
 }

 @Test
 public void testAtomicBooleanDeserializer() throws IOException {
 Object atomicBoolean = objectMapper.readValue("true", AtomicBoolean.class);
 assertTrue((AtomicBoolean) atomicBoolean);
 }

 @Test
 public void testByteBufferDeserializer() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.allocate(10);
 byteBuffer.put("Test".getBytes());
 Object readValue = objectMapper.readValue(new ByteArrayInputStream(byteBuffer.array()), ByteBuffer.class);
 assertEquals(byteBuffer, readValue);
 }
}