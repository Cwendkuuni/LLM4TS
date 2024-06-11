package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerForUUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() throws IOException {
 Class<String> stringClass = String.class;
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, String.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<Date> dateClass = Date.class;
 JsonDeserializer<Date> deserializer = JdkDeserializers.find(dateClass, TextDeserializer.DATE_VALUE_CLASS_NAME);
 assertNotNull(deserializer);
 assertInstanceOf(TextDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForArrayOfUUID() throws IOException {
 Class<UUID[]> uuidArrayClass = new TypeReference<UUID[]>() {}.getType();
 JsonDeserializer<UUID[]> deserializer = JdkDeserializers.find(uuidArrayClass, UUID[].class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(CollectionDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForMapOfUUIDToInteger() throws IOException {
 Class<Map<UUID, Integer>> mapClass = new TypeReference<Map<UUID, Integer>>() {}.getType();
 JsonDeserializer<Map<UUID, Integer>> deserializer = JdkDeserializers.find(mapClass, Map.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(MapDeserializer.class, deserializer);
 }
}