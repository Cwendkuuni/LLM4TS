package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private static ObjectMapper mapper;

 static {
 try {
 mapper = new ObjectMapper();
 } catch (JsonFactory.FeatureNotSupportedException e) {
 e.printStackTrace();
 }
}

 @Test
 public void testAtomicReferenceDeserializer_Constructor() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 assertNull(serializer.getNullValue(null));
 assertSame(AtomicReference.class, serializer.getType());
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 assertNull(serializer.getNullValue(null));
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> emptyValue = serializer.getEmptyValue(null);
 assertNotNull(emptyValue);
 assertSame(AtomicReference.class, emptyValue.getClass());
 assertNull(emptyValue.get());
 }

 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 Object referenceValue = new Object();
 AtomicReference<Object> atomicReference = serializer.referenceValue(referenceValue);
 assertNotNull(atomicReference);
 assertSame(AtomicReference.class, atomicReference.getClass());
 assertSame(referenceValue, atomicReference.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 Object referencedObject = serializer.getReferenced(atomicReference);
 assertNotNull(referencedObject);
 assertSame(atomicReference.get(), referencedObject);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(atomicReference, newValue);
 assertSame(atomicReference, updatedReference);
 assertSame(newValue, atomicReference.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testSupportsUpdate_NonSupporting() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 assertFalse(serializer.supportsUpdate(null));
 }

 @Test
 public void testSupportsUpdate_Supporting() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 assertTrue(serializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, JsonProcessingException {
 String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestAtomicReference\",\"value\":\"testValue\"}";
 TypeDeserializer typeDeserializer = new AtomicReferenceTypeDeserializer();
 JsonDeserializer<Object> valueDeserializer = new SimpleValueDeserializer();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, typeDeserializer, valueDeserializer);
 AtomicReference<Object> reference = mapper.readValue(json, AtomicReference.class);
 assertNotNull(reference);
 assertSame(TestAtomicReference.class, reference.getType());
 assertEquals("testValue", reference.get());
 }

 @Test
 public void testDeserialize_Null() throws JsonMappingException, JsonProcessingException {
 String json = "null";
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> reference = mapper.readValue(json, AtomicReference.class);
 assertNull(reference);
 }
}

 class TestAtomicReference {}