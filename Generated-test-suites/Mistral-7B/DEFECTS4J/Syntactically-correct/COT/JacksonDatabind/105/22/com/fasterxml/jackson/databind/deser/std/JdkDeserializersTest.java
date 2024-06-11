package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public class JdkDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @org.junit.Test
 public void testFindWithValidClassNameAndType() {
 Class<UUID> uuidClass = UUID.class;
 String uuidClassName = uuidClass.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, uuidClassName);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @org.junit.Test
 public void testFindWithValidTypeButWrongClassName() {
 Class<UUID> uuidClass = UUID.class;
 String stringClassName = String.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, stringClassName);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithValidClassNameButWrongType() {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String uuidClassName = UUID.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, uuidClassName);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithNullTypeAndClassName() {
 JsonDeserializer<None> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithValidTypeButNullClassName() {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, null);
 assertTrue(deserializer instanceof FromStringDeserializer);
 }

 @org.junit.Test
 public void testFindWithUnknownType() {
 Class<Object> objectClass = Object.class;
 String unknownTypeClassName = "com.unknown.UnknownType";
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, unknownTypeClassName);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithEmptyListOfTypes() {
 Class<?> emptyClassArray[] = {};
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, null);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithEmptyMapOfClassNames() {
 HashSet<String> emptyClassNames = new HashSet<>();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, null);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithNullMapOfClassNames() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, null);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindWithInvalidClassNameInMap() {
 HashSet<String> classNames = new HashSet<>();
 classNames.add("com.invalid.InvalidType");
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, null);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testUuidDeserializer() throws JsonProcessingException, IOException {
 String json = "\"rhdgfjhghj-hjgj-gjhj-hjgj-hjgjhjgjhj\"";
 UUID uuid = mapper.readValue(new TextNode(json), UUID.class);
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 UUID deserializedUuid = mapper.readValue(new TextNode(json), deserializer.getClass());
 assertEquals(uuid, deserializedUuid);
 }

 @org.junit.Test
 public void testStackTraceElementDeserializer() throws JsonProcessingException, IOException {
 String json = "{\"className\":\"JdkDeserializersTest\", \"file\":\"JdkDeserializersTest.java\", \"lineNumber\":15, \"methodName\":\"testFindWithValidClassNameAndType\", \"nativeMethod\":false}";
 StackTraceElement stackTraceElement = mapper.readValue(new TextNode(json), StackTraceElement.class);
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 StackTraceElement deserializedStackTraceElement = mapper.readValue(new TextNode(json), deserializer.getClass());
 assertEquals(stackTraceElement, deserializedStackTraceElement);
 }

 @org.junit.Test
 public void testAtomicBooleanDeserializer() throws JsonProcessingException, IOException {
 String jsonTrue = "true";
 AtomicBoolean atomicBoolean = new AtomicBoolean(true);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonDeserializer<AtomicBoolean> deserializerTrue = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 AtomicBoolean deserializedAtomicBooleanTrue = mapper.readValue(new TextNode(jsonTrue), deserializerTrue.getClass());
 assertTrue(deserializedAtomicBooleanTrue.get());
 
 String jsonFalse = "false";
 AtomicBoolean atomicBoolean = new AtomicBoolean(false);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonDeserializer<AtomicBoolean> deserializerFalse = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 AtomicBoolean deserializedAtomicBooleanFalse = mapper.readValue(new TextNode(jsonFalse), deserializerFalse.getClass());
 assertFalse(deserializedAtomicBooleanFalse.get());
 }

 @org.junit.Test
 public void testByteBufferDeserializer() throws JsonProcessingException, IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4, 5 });
 String json = mapper.writeValueAsString(byteBuffer);
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 ByteBuffer deserializedByteBuffer = mapper.readValue(new TextNode(json), deserializer.getClass());
 assertEquals(byteBuffer, deserializedByteBuffer);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testFindWithUnknownTypeThrowsJsonMappingException() {
 Class<Object> objectClass = Object.class;
 String unknownTypeClassName = "com.unknown.UnknownType";
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, unknownTypeClassName);
 fail("Expected JsonMappingException not thrown");
 }

 @org.junit.Test(expected = JsonMappingException.ClassMismatch.class)
 public void testFindWithInvalidTypeThrowsClassMismatch() {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<Integer> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
 fail("Expected ClassMismatch not thrown");
 }

 @org.junit.Test(expected = JsonProcessingException.class)
 public void testReadValueWithNullJsonNode() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, null);
 fail("Expected JsonProcessingException not thrown");
 }

 @org.junit.Test(expected = JsonProcessingException.class)
 public void testReadValueWithInvalidJsonString() {
 String json = "{\"className\":\"JdkDeserializersTest\", \"file\":\"JdkDeserializersTest.java\", \"lineNumber\":15, \"methodName\":\"testFindWithValidClassNameAndType\", \"nativeMethod\":\"not a boolean\"}";
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 fail("Expected JsonProcessingException not thrown");
 }
}