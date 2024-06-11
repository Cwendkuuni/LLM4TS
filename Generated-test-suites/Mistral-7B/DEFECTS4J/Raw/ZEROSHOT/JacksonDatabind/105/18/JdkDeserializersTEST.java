package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = mock(ObjectMapper.class);
 }

 @Test
 public void testFind_UUID_Class() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = uuidClass.getName();
 JsonDeserializer<?> expectedDeserializer = new UUIDDeserializer();

 when(objectMapper.findValueDeserializer(uuidClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(uuidClass, clsName);

 assertInstanceOf(UUIDDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_UUID_String() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = "UUID";

 when(objectMapper.findValueDeserializer(uuidClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(uuidClass, clsName);

 assertInstanceOf(UUIDDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_AtomicBoolean_Class() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = atomicBooleanClass.getName();
 JsonDeserializer<?> expectedDeserializer = new AtomicBooleanDeserializer();

 when(objectMapper.findValueDeserializer(atomicBooleanClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(atomicBooleanClass, clsName);

 assertInstanceOf(AtomicBooleanDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_AtomicBoolean_String() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = "AtomicBoolean";

 when(objectMapper.findValueDeserializer(atomicBooleanClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(atomicBooleanClass, clsName);

 assertInstanceOf(AtomicBooleanDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_StackTraceElement_Class() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = stackTraceElementClass.getName();
 JsonDeserializer<?> expectedDeserializer = new StackTraceElementDeserializer();

 when(objectMapper.findValueDeserializer(stackTraceElementClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stackTraceElementClass, clsName);

 assertInstanceOf(StackTraceElementDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_StackTraceElement_String() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = "StackTraceElement";

 when(objectMapper.findValueDeserializer(stackTraceElementClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stackTraceElementClass, clsName);

 assertInstanceOf(StackTraceElementDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_ByteBuffer_Class() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = byteBufferClass.getName();
 JsonDeserializer<?> expectedDeserializer = new ByteBufferDeserializer();

 when(objectMapper.findValueDeserializer(byteBufferClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(byteBufferClass, clsName);

 assertInstanceOf(ByteBufferDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_ByteBuffer_String() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = "ByteBuffer";

 when(objectMapper.findValueDeserializer(byteBufferClass))
 .thenReturn(null);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(byteBufferClass, clsName);

 assertInstanceOf(ByteBufferDeserializer.class, actualDeserializer);
 }

 @Test
 public void testFind_UnknownClass() throws IOException {
 Class<Object> unknownClass = Object.class;
 String clsName = "UnknownClass";

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(unknownClass, clsName);

 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = stringClass.getName();
 JsonDeserializer<?> expectedDeserializer = Mockito.mock(JsonDeserializer.class);

 when(objectMapper.findValueDeserializer(stringClass))
 .thenReturn(expectedDeserializer);

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stringClass, clsName);

 assertSame(expectedDeserializer, actualDeserializer);
 }

 @Test
 public void testFind_NullObjectMapper() {
 Class<UUID> uuidClass = UUID.class;
 String clsName = uuidClass.getName();

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(uuidClass, clsName);

 assertNull(actualDeserializer);
 }
}