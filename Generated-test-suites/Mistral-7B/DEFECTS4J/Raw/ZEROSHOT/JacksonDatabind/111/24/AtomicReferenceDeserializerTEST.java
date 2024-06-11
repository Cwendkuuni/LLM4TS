package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AtomicReferenceDeserializerTest {

 private static ObjectMapper objectMapper;

 static {
 try {
 objectMapper = new ObjectMapper();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDefaultConstruction() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testConstruction() {
 JavaType fullType = null;
 ValueInstantiator valueInstantiator = null;
 TypeDeserializer typeDeser = new EmptyTypeDeserializer();
 JsonDeserializer<?> valueDeser = null;
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, valueDeser);
 assertNotNull(deserializer);
 }

 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(null, null, null, null);
 TypeDeserializer typeDeser = new EmptyTypeDeserializer();
 JsonDeserializer<?> valueDeser = null;
 AtomicReferenceDeserializer deserializer2 = deserializer1.withResolved(typeDeser, valueDeser);
 assertNotSame(deserializer1, deserializer2);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> result = deserializer.getNullValue(null);
 assertNotNull(result);
 assertNull(result.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object result = deserializer.getEmptyValue(null);
 assertNotNull(result);
 assertInstanceOf(AtomicReference.class, result);
 assertNull(((AtomicReference<Object>) result).get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object testValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object result = deserializer.referenceValue(testValue);
 assertNotNull(result);
 assertInstanceOf(AtomicReference.class, result);
 assertEquals(testValue, result.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object result = deserializer.getReferenced(reference);
 assertNotNull(result);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object newValue = new Object();
 AtomicReference<Object> result = deserializer.updateReference(reference, newValue);
 assertSame(reference, result);
 assertEquals(newValue, reference.get());
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(deserializer.supportsUpdate(null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNullValueWithContext() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 DeserializationContext context = null;
 AtomicReference<Object> result = deserializer.getNullValue(context);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetEmptyValueWithContext() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 DeserializationContext context = null;
 Object result = deserializer.getEmptyValue(context);
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testReferenceValueWithContext() throws JsonMappingException {
 Object testValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.referenceValue(testValue, context);
 assertNotNull(result);
 assertInstanceOf(AtomicReference.class, result);
 assertEquals(testValue, result.get());
 }

 @Test
 public void testGetReferencedWithContext() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object testValue = new Object();
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.putContextValue(NameTransformer.ROOT_NAME_TRANSFORM_KEY, new NameTransformer() {
 @Override public String transform(String name, TypeSerializer typeSerializer) {
 return name;
 }
 });
 context.putContextValue(NameTransformer.PREFIX_KEY, "");
 context.putContextValue(NameTransformer.SUFFIX_KEY, "");
 AtomicReference<Object> result = deserializer.getReferenced(reference, context);
 assertNotNull(result);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReferenceWithContext() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object newValue = new Object();
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.putContextValue(NameTransformer.ROOT_NAME_TRANSFORM_KEY, new NameTransformer() {
 @Override public String transform(String name, TypeSerializer typeSerializer) {
 return name;
 }
 });
 context.putContextValue(NameTransformer.PREFIX_KEY, "");
 context.putContextValue(NameTransformer.SUFFIX_KEY, "");
 AtomicReference<Object> result = deserializer.updateReference(reference, newValue, context);
 assertSame(reference, result);
 assertEquals(newValue, reference.get());
 }

 @Test
 public void testToDeserializer() throws JsonMappingException, JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonDeserializer<?> result = deserializer.toDeserializer(null);
 assertNotNull(result);
 assertTrue(result instanceof AtomicReferenceDeserializer);
 }

 @Test
 public void testDeserializeValue() throws JsonMappingException, JsonProcessingException {
 String json = "{\"value\": \"testValue\"}";
 JavaType javaType = objectMapper.getTypeFactory().constructType(AtomicReference.class, String.class);
 JsonDeserializer<AtomicReference<String>> deserializer = objectMapper.findValueDeserializer(javaType);
 Object result = deserializer.deserialize(objectMapper.readTree(json), null);
 AtomicReference<String> expected = new AtomicReference<>("testValue");
 assertEquals(expected, result);
 }
}