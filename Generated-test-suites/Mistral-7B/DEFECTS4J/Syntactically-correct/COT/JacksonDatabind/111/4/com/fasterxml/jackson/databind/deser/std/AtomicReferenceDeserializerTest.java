package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializers;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class),
 null, null, null);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReference<Object> actual = deserializer.getNullValue(null);
 assertNull("getNullValue should return null", actual.get());
 assertSame(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReference<Object> actual = deserializer.getEmptyValue(null);
 assertSame(expected, actual);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = "test";
 AtomicReference<Object> expected = new AtomicReference<>(contents);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<>("test");
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<>("test");
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 assertEquals(updatedReference, reference);
 assertEquals(newValue, updatedReference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceUnsupported() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<>("test");
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializer.updateReference(null, newValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceNullValue() throws JsonMappingException {
 AtomicReference<Object> reference = null;
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceInvalidDeserializer() throws JsonMappingException {
 JsonDeserializer<?> deserializer = StdSerializers.nullDeserializer();
 AtomicReferenceDeserializer deserializerWrapper = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, deserializer);
 AtomicReference<Object> reference = new AtomicReference<>("test");
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializerWrapper.updateReference(reference, newValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNullReference() throws JsonMappingException {
 AtomicReference<Object> reference = null;
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNonAtomicReferenceType() throws JsonMappingException {
 JsonDeserializer<String> deserializer = StdDeserializers.stringDeserializer();
 AtomicReferenceDeserializer deserializerWrapper = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(String.class), null, null, deserializer);
 AtomicReference<Object> reference = new AtomicReference<>("test");
 Object newValue = "newTest";
 AtomicReference<Object> updatedReference = deserializerWrapper.updateReference(reference, newValue);
 }
}