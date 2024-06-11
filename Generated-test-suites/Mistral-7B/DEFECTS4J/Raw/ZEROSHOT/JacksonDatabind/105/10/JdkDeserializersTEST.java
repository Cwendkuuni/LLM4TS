package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.ByteBufferDeserializer;

public class JdkDeserializersTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindNull() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindUnknownClass() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "UnknownClass");
 assertNull(deserializer);
 }

 @Test
 public void testFindUUID() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 assertNotNull(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindAtomicBoolean() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertNotNull(deserializer instanceof AtomicBooleanDeserializer);
 }

 @Test
 public void testFindStackTraceElement() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertNotNull(deserializer instanceof StackTraceElementDeserializer);
 }

 @Test
 public void testFindByteBuffer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertNotNull(deserializer instanceof ByteBufferDeserializer);
 }

 @Test
 public void testFindFromStringDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, FromStringDeserializer.class.getName());
 assertNotNull(deserializer);
 assertNotNull(deserializer instanceof None);
 }

 @Test
 public void testFindDeserializerForList() throws IOException {
 JsonDeserializer<List> listDeserializer = JdkDeserializers.find(List.class, "java.util.ArrayList");
 assertNotNull(listDeserializer);
 assertNull(listDeserializer instanceof JdkDeserializers.FromStringDeserializer);
 }

 @Test
 public void testFindDeserializerForMap() throws IOException {
 JsonDeserializer<Map> mapDeserializer = JdkDeserializers.find(Map.class, "java.util.HashMap");
 assertNotNull(mapDeserializer);
 assertNull(mapDeserializer instanceof JdkDeserializers.FromStringDeserializer);
 }

 @Test
 public void testFindDeserializerForCustomClass() throws IOException {
 ObjectMapper mapper = objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonDeserializer<CustomClass> customDeserializer = JdkDeserializers.find(CustomClass.class, CustomClass.class.getName());
 assertNotNull(customDeserializer);
 assertNull(customDeserializer instanceof JdkDeserializers.FromStringDeserializer);

 mapper.writeValueAsString(new CustomClass());
 }

 @JsonDeserialize(using = UUIDDeserializer.class)
 static class CustomClass {
 private UUID id;
 }
}