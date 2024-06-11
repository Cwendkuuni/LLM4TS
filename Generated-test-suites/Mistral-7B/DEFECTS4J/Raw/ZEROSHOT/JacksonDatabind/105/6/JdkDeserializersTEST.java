package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.core.base.ClassIdResolverBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Test class for JdkDeserializers.
 */
public class JdkDeserializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerForUuid() throws IOException {
 Class<?> rawType = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 Class<?> rawType = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof AtomicBooleanDeserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 Class<?> rawType = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof StackTraceElementDeserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 Class<?> rawType = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof ByteBufferDeserializer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() throws IOException {
 Class<?> rawType = String.class;
 String clsName = Object.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<?> rawType = Date.class;
 String clsName = Date.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);

 // check that FromStringDeserializer is picked up
 Object emptyMap = Collections.emptyMap();
 ClassIdResolver idResolver = new ClassIdResolverBase() {
 @Override
 public int idFromClassName(String className) {
 return -1;
 }
 };
 ObjectReader reader = objectMapper.readerForMap(new TypeReference<Map<String, Object>>() {}
 .withResolvedType(new TypeReference<Map<String, Object>>() {}
 .withTypeIdResolver(idResolver)));
 JsonDeserializer<?> deserializerFromString = reader.getValueFactory().getDeserializerProvider()
 .findValueDeserializer(JsonType. constructorValueType(Date.class));
 assertTrue(deserializerFromString instanceof FromStringDeserializer);
 deserializer = JdkDeserializers.find(rawType, Date.class.getName());
 assertSame(deserializerFromString, deserializer);
 }
}