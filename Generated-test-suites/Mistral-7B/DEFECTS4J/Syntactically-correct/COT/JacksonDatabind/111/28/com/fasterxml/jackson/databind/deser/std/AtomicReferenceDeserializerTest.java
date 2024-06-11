package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private static final JavaType ATOMIC_REFERENCE_TYPE = ObjectTypeFactory.defaultInstance()
         .constructType(AtomicReference.class, Object.class);

 // Basic test cases for all methods

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 assertNotNull(serializer);
 }

 @Test
 public void testWithResolved() {
 TypeDeserializer typeDeser = null;
 JsonDeserializer<?> valueDeser = null;
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 AtomicReferenceDeserializer newSerializer = serializer.withResolved(typeDeser, valueDeser);
 assertEquals(serializer, newSerializer);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 AtomicReference<Object> result = serializer.getNullValue(null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 AtomicReference<Object> result = serializer.getEmptyValue(null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 AtomicReference<Object> result = serializer.referenceValue(contents);
 assertTrue(result.get() == contents);
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 Object result = serializer.getReferenced(reference);
 assertTrue(result == reference.get());
 }

 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
 assertTrue(updatedReference == reference);
 assertTrue(updatedReference.get() == newValue);
 }

 // Testing edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
 }

 @Test
 public void testNullDeserializedValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdSerializer<Object> nullValueSerializer = new StdSerializer<Object>(Object.class);
 mapper.registerSerializer(Object.class, nullValueSerializer);
 String json = "null";
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 JsonParser parser = mapper.getFactory().createParser(json);
 AtomicReference<Object> result;
 try {
 result = serializer.deserialize(parser, null, null);
 } catch (JsonParseException e) {
 result = null;
 }
 assertNull(result);
 }

 @Test
 public void testDeserializationOfNonAtomicReferenceType() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 JsonParser parser = null; // or any other non-AtomicReference type
 AtomicReference<Object> result = serializer.deserialize(parser, null, null);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullDeserializer() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, null, null, null);
 JsonDeserializer<?> deser = null;
 AtomicReference<Object> result = serializer.withResolved(null, deser);
 }

}