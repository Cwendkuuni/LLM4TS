package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() {
 objectMapper = new ObjectMapper();
 }

 // Test for find method with valid arguments
 @Test
 public void testFindValidArguments() {
 Class<?> rawType = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> expectedDeserializer = new UUIDDeserializer();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 Assert.assertSame(expectedDeserializer, deserializer);
 }

 // Test for find method with invalid clsName
 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidClassName() {
 Class<?> rawType = String.class;
 String clsName = "InvalidClassName";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 }

 // Test for find method with null rawType
 @Test(expected = NullPointerException.class)
 public void testFindNullRawType() {
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, clsName);
 }

 // Test for find method with null clsName
 @Test(expected = NullPointerException.class)
 public void testFindNullClassName() {
 Class<?> rawType = UUID.class;
 String clsName = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 }

 // Test for find method with unregistered class
 @Test
 public void testFindUnregisteredClass() {
 Class<?> rawType = StackTraceElementDeserializer.class;
 String clsName = StackTraceElementDeserializer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 Assert.assertNull(deserializer);
 }

 // Test for FromStringDeserializer.findDeserializer method with valid arguments
 @Test
 public void testFindDeserializerValidArguments() {
 Class<?> rawType = UUID.class;
 JsonDeserializer<?> expectedDeserializer = new UUIDDeserializer();
 JsonDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(rawType);
 Assert.assertSame(expectedDeserializer, deserializer);
 }

 // Test for FromStringDeserializer.findDeserializer method with null rawType
 @Test(expected = NullPointerException.class)
 public void testFindDeserializerNullRawType() {
 JsonDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(null);
 }

 // Test for UUIDDeserializer method createInstance
 @Test
 public void testUuidDeserializerCreateInstance() throws JsonProcessingException, IOException {
 String json = "\"some-uuid\"";
 UUID expectedUuid = UUID.fromString("some-uuid");
 Object result = objectMapper.readValue(json, UUID.class);
 UUIDDeserializer deserializer = new UUIDDeserializer();
 UUID deserializerResult = deserializer.createInstance(objectMapper.readTree(json), null);
 Assert.assertSame(expectedUuid, deserializerResult);
 }

 // Test for UUIDDeserializer method deserialize
 @Test
 public void testUuidDeserializerDeserialize() throws JsonProcessingException {
 String json = "\"some-uuid\"";
 Object result = JdkDeserializers.find(UUID.class, UUIDDeserializer.class.getName()).deserialize(objectMapper.readTree(json), null);
 Assert.assertSame(UUID.fromString("some-uuid"), result);
 }

 // Test for AtomicBooleanDeserializer method createInstance
 @Test
 public void testAtomicBooleanDeserializerCreateInstance() throws JsonProcessingException, IOException {
 String json = "true";
 AtomicBoolean expectedAtomicBoolean = new AtomicBoolean(true);
 Object result = objectMapper.readValue(json, AtomicBoolean.class);
 AtomicBooleanDeserializer deserializer = new AtomicBooleanDeserializer();
 AtomicBoolean deserializerResult = deserializer.createInstance(objectMapper.readTree(json), null);
 Assert.assertSame(expectedAtomicBoolean, deserializerResult);
 }

 // Test for AtomicBooleanDeserializer method deserialize
 @Test
 public void testAtomicBooleanDeserializerDeserialize() throws JsonProcessingException {
 String json = "true";
 Object result = JdkDeserializers.find(AtomicBoolean.class, AtomicBooleanDeserializer.class.getName()).deserialize(objectMapper.readTree(json), null);
 Assert.assertTrue((Boolean) result);
 }

 // Test for ByteBufferDeserializer method createInstance
 @Test
 public void testByteBufferDeserializerCreateInstance() throws JsonProcessingException, IOException {
 String json = "\"byte-buffer\"";
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
 Object result = objectMapper.readValue(json, ByteBuffer.class);
 ByteBufferDeserializer deserializer = new ByteBufferDeserializer();
 ByteBuffer deserializerResult = deserializer.createInstance(objectMapper.readTree(json), null);
 Assert.assertSame(expectedByteBuffer, deserializerResult);
 }

 // Test for ByteBufferDeserializer method deserialize
 @Test
 public void testByteBufferDeserializerDeserialize() throws JsonProcessingException {
 String json = "\"byte-buffer\"";
 Object result = JdkDeserializers.find(ByteBuffer.class, ByteBufferDeserializer.class.getName()).deserialize(objectMapper.readTree(json), null);
 Assert.assertSame(ByteBuffer.wrap(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}), result);
 }

 // Test for StackTraceElementDeserializer method deserialize
 @Test
 public void testStackTraceElementDeserializerDeserialize() throws JsonProcessingException {
 String json = "[\"testClassName\",1,\"testMethodName\",\"testFile:123:testLine\",\"testClassFile\",\"testClassName\"]";
 Object result = JdkDeserializers.find(StackTraceElement.class, StackTraceElementDeserializer.class.getName()).deserialize(objectMapper.readTree(json), null);
 StackTraceElement expectedStackTraceElement = new StackTraceElement("testClassName", "testMethodName", "testFile:123:testLine", "(testClassFile):testClassName");
 Assert.assertSame(expectedStackTraceElement, result);
 }
}