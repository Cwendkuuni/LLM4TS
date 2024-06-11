package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFind_NullRawType() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertNull(jdkDeserializers.find(null, "AnyClassName"));
 }

 @Test
 public void testFind_NullClsName() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertNull(jdkDeserializers.find(UUID.class, null));
 }

 @Test
 public void testFind_NonMatchingClsName() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertNull(jdkDeserializers.find(Integer.class, "com.fasterxml.jackson.databind.deser.std.JdkDeserializers"));
 }

 @Test
 public void testFind_FromStringDeserializer() throws IOException {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertNotNull(jdkDeserializers.find(String.class, FromStringDeserializer.class.getName()));
 }

 @Test
 public void testFind_UUID() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 JsonDeserializer<?> deserializer = jdkDeserializers.find(UUID.class, UUID.class.getName());
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFind_AtomicBoolean() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 JsonDeserializer<?> deserializer = jdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFind_StackTraceElement() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 JsonDeserializer<?> deserializer = jdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFind_ByteBuffer() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 JsonDeserializer<?> deserializer = jdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testFind_UnknownClass() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 jdkDeserializers.find(Long.class, "com.unknown.UnknownClass");
 }

 @Test(expected = NullPointerException.class)
 public void testFind_NullClass() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 jdkDeserializers.find(null, "UUID");
 }

 @Test
 public void testFind_DifferentClassesWithSameName() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertNotSame(new UUIDDeserializer(), new AtomicBooleanDeserializer());
 JsonDeserializer<?> uuidDeserializer = jdkDeserializers.find(UUID.class, UUID.class.getName());
 JsonDeserializer<?> atomicBooleanDeserializer = jdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotSame(uuidDeserializer, atomicBooleanDeserializer);
 }

 @Test
 public void testContains_EmptyClassNameSet() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertFalse(jdkDeserializers._classNames.contains("String"));
 }

 @Test
 public void testContains_NonEmptyClassNameSet() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 assertTrue(jdkDeserializers._classNames.contains(UUID.class.getName()));
 }

 @Test
 public void testFromStringDeserializer_EmptyTypes() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 List<Class<?>> types = Collections.emptyList();
 for (Class<?> cls : jdkDeserializers.FromStringDeserializer.types()) {
 types.add(cls);
 }
 assertTrue(types.isEmpty());
 }

 @Test
 public void testFromStringDeserializer_NonEmptyTypes() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 List<Class<?>> types = new ArrayList<>();
 for (Class<?> cls : jdkDeserializers.FromStringDeserializer.types()) {
 types.add(cls);
 }
 assertFalse(types.isEmpty());
 }

 @Test
 public void testFromStringDeserializer_ContainsUUID() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 List<Class<?>> types = jdkDeserializers.FromStringDeserializer.types();
 assertTrue(types.contains(UUID.class));
 }

 @Test
 public void testFromStringDeserializer_ContainsAtomicBoolean() {
 JdkDeserializers jdkDeserializers = new JdkDeserializers();
 List<Class<?>> types = jdkDeserializers.FromStringDeserializer.types();
 assertTrue(types.contains(AtomicBoolean.class));
 }
}