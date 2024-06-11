package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JdkDeserializersTest {

 private static Set<String> _classNames;

 @BeforeClass
 public static void initClass() {
 _classNames = new HashSet<String>();
 Class<?>[] types = new Class<?>[] {
 UUID.class,
 AtomicBoolean.class,
 StackTraceElement.class,
 ByteBuffer.class
 };
 for (Class<?> cls : types) { _classNames.add(cls.getName()); }
 for (Class<?> cls : FromStringDeserializer.types()) { _classNames.add(cls.getName()); }
 }

 @Test
 public void testFind_UUID() throws JsonMappingException {
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 assertEquals(UUIDDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_UUID_String() throws JsonMappingException {
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(String.class, UUID.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_AtomicBoolean() throws JsonMappingException {
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_AtomicBoolean_String() throws JsonMappingException {
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(String.class, AtomicBoolean.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_StackTraceElement() throws JsonMappingException {
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_StackTraceElement_String() throws JsonMappingException {
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(String.class, StackTraceElement.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_ByteBuffer() throws JsonMappingException {
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_ByteBuffer_String() throws JsonMappingException {
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(String.class, ByteBuffer.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_UnknownClass() throws JsonMappingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Integer.class, UUID.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() throws JsonMappingException {
 Class<?> testType = String[].class;
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(testType, String.class.getName());
 assertNotNull(deserializer);
 assertEquals(FromStringDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_FromStringDeserializer_String() throws JsonMappingException {
 Class<?> testType = String[].class;
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(String.class, String.class.getName());
 assertNull(deserializer);
 }
}