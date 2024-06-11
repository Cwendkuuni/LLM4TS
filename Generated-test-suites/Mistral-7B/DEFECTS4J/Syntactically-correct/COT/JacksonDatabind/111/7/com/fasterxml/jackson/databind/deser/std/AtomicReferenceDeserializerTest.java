package com.example.junit;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 assertNotNull(atomicReferenceDeserializer);
 }

 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer serializer1 = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReferenceDeserializer serializer2 = serializer1.withResolved(null, null);
 assertNotSame(serializer1, serializer2);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 DeserializationContext context = mock(DeserializationContext.class);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(context);
 assertTrue(result instanceof AtomicReference);
 assertNull(result.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 Object result = atomicReferenceDeserializer.getEmptyValue(null);
 assertTrue(result instanceof AtomicReference);
 assertNull(result.get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = new Object();
 DeserializationContext context = mock(DeserializationContext.class);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
 assertNotNull(result);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 Object result = atomicReferenceDeserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, newValue);
 assertEquals(reference, result);
 assertEquals(newValue, reference.get());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedUpdate() throws JsonMappingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 assertFalse(atomicReferenceDeserializer.supportsUpdate(null));
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 JavaType javaType = new TypeFactory().constructType(AtomicReference.class, String.class);
 String jsonString = "{\"value\": \"test\"}";
 ObjectReader objectReader = objectMapper.readerForType(javaType);
 AtomicReference<String> result = objectReader.readValue(new StringReader(jsonString));
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, String.class), null, null, null);
 AtomicReference<String> expected = new AtomicReference<String>("test");
 assertEquals(expected, result);
 assertEquals("test", expected.get());
 }

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 JavaType javaType = new TypeFactory().constructType(AtomicReference.class, String.class);
 String jsonString = "{}";
 ObjectReader objectReader = objectMapper.readerForType(javaType);
 AtomicReference<String> result = objectReader.readValue(new StringReader(jsonString));
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, String.class), null, null, null);
 AtomicReference<String> expected = new AtomicReference<String>(null);
 assertEquals(expected, result);
 assertNull(expected.get());
 }

 @Test
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JavaType javaType = new TypeFactory().constructType(AtomicReference.class, String.class);
 String jsonString = "{\"invalid\": \"json\"}";
 ObjectReader objectReader = objectMapper.readerForType(javaType);
 AtomicReference<String> result = objectReader.readValue(new StringReader(jsonString));
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, String.class), null, null, null);
 assertNull(result);
 }
}