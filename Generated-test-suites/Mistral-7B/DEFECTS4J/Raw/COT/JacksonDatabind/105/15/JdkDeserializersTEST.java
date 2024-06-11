package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testFind_NullClass_ReturnsNull() {
 assertNull(JdkDeserializers.find(null, null));
 }

 @Test
 public void testFind_UnknownClass_ReturnsNull() {
 assertNull(JdkDeserializers.find(String.class, "UnknownClass"));
 }

 @Test
 public void testFind_UUID() {
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertEquals(new UUIDDeserializer(), deserializer);
 }

 @Test
 public void testFind_AtomicBoolean() {
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertEquals(new AtomicBooleanDeserializer(), deserializer);
 }

 @Test
 public void testFind_StackTraceElement() {
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertEquals(new StackTraceElementDeserializer(), deserializer);
 }

 @Test
 public void testFind_ByteBuffer() {
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertEquals(new ByteBufferDeserializer(), deserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() {
 Class<?> rawType = UUID.class;
 String clsName = Object.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 None expected = null;
 assertEquals(expected, deserializer);
 }

 @Test
 public void testFind_StaticClass_ReturnsNull() {
 Class<?> rawType = UUID.class;
 String clsName = "java.util.UUID".replace(".", "_").toUpperCase();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFind_Collection_ReturnsNull() {
 HashSet<String> hashSet = new HashSet<String>();
 JsonDeserializer<HashSet<String>> deserializer = JdkDeserializers.find(hashSet.getClass(), hashSet.getClass().getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_List_ReturnsNull() {
 List<String> list = Arrays.asList("one", "two");
 JsonDeserializer<List<String>> deserializer = JdkDeserializers.find(list.getClass(), list.getClass().getName());
 assertNull(deserializer);
 }
}