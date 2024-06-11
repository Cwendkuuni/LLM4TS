package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class AtomicReferenceDeserializerTest {

 private static ObjectMapper objectMapper;

 static {
 try {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAtomicReferenceDeserializer() throws JsonMappingException {
 AtomicReference<String> atomicReference = new AtomicReference<>("Initial Value");

 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}.getType(), null, null, null);

 DeserializationContext ctxt = new TestDeserializationContext();
 AtomicReference<Object> deserializedAtomicReference = atomicReferenceDeserializer.deserialize(atomicReference.toString(), ctxt);

 assertEquals("Initial Value", deserializedAtomicReference.get());

 AtomicReference<Integer> deserializedAtomicReferenceInt = atomicReferenceDeserializer.deserialize(Integer.toString(1), ctxt);
 assertNotNull(deserializedAtomicReferenceInt);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<String> atomicReference = new AtomicReference<>("Initial Value");

 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}.getType(), null, null, null);

 DeserializationContext ctxt = new TestDeserializationContext();

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String jsonString = "{\"value\": \"New Value\"}";
 AtomicReference<String> updatedAtomicReference = new ObjectMapper().readValue(jsonString, new TypeReference<AtomicReference<String>>(){}.getType());

 AtomicReference<String> deserializedAtomicReference = atomicReferenceDeserializer.updateReference(atomicReference, updatedAtomicReference.get());

 assertEquals("New Value", deserializedAtomicReference.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceWithNullReference() throws JsonMappingException {
 AtomicReference<String> atomicReference = null;

 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}.getType(), null, null, null);

 DeserializationContext ctxt = new TestDeserializationContext();

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String jsonString = "{\"value\": \"New Value\"}";
 AtomicReference<String> updatedAtomicReference = new ObjectMapper().readValue(jsonString, new TypeReference<AtomicReference<String>>(){}.getType());

 atomicReferenceDeserializer.updateReference(atomicReference, updatedAtomicReference.get());
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}.getType(), null, null, null);

 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 private static class TestDeserializationContext extends DeserializationContext {
 @SuppressWarnings("unchecked")
 public <T> T constructValue(Class<T> valueType) throws JsonMappingException {
 return (T) new AtomicReference<Object>();
 }

 @SuppressWarnings("unchecked")
 public <T> T constructValue(Class<?> valueType, TypeSerializer valueTypeSerializer) throws JsonMappingException {
 return (T) new AtomicReference<Object>();
 }

 @SuppressWarnings("unchecked")
 public <T> T constructValue(TypeDescriptor valueType) throws JsonMappingException {
 return (T) new AtomicReference<Object>();
 }
 }
}