package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static AtomicReferenceDeserializer getDeserializer(JavaType type) {
 return new AtomicReferenceDeserializer(type, null, null, null);
 }

 @SuppressWarnings("unchecked")
 private static AtomicReference<Object> readValue(String json) throws IOException {
 JavaType type = new TypeReference<AtomicReference<Object>>() {}.getType();
 TypeDeserializer deserializer = getDeserializer(type);
 ObjectReader reader = mapper.readerForType(type);
 return reader.readValue(new JsonFactory(), deserializer);
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @org.junit.Test
 public void testEmptyAtomicReferenceDeserialization() {
 AtomicReference<Object> atomicReference = null;
 try {
 atomicReference = readValue("{}");
 } catch (JsonProcessingException e) {
 fail("Unexpected JsonProcessingException: " + e.getMessage());
 }
 assertNotNull("AtomicReference should not be null", atomicReference);
 assertTrue("AtomicReference should be empty", atomicReference.isEmpty());
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @org.junit.Test
 public void testAtomicReferenceDeserializationWithValue() {
 Object value = new Object();
 AtomicReference<Object> atomicReference = null;
 try {
 atomicReference = readValue("{\"value\": " + value.getClass().getName() + ":" + value + "}");
 } catch (JsonProcessingException e) {
 fail("Unexpected JsonProcessingException: " + e.getMessage());
 }
 assertNotNull("AtomicReference should not be null", atomicReference);
 assertEquals("AtomicReference value should match the provided value", value, atomicReference.get());
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @org.junit.Test
 public void testAtomicReferenceDeserializationWithNullValue() {
 AtomicReference<Object> atomicReference = null;
 try {
 atomicReference = readValue("{}");
 } catch (JsonProcessingException e) {
 fail("Unexpected JsonProcessingException: " + e.getMessage());
 }
 assertNotNull("AtomicReference should not be null", atomicReference);
 assertNull("AtomicReference value should be null", atomicReference.get());
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @org.junit.Test
 public void testAtomicReferenceDeserializationWithUpdate() throws IOException {
 AtomicReference<Object> atomicReference = readValue("{ \"value\": \"updatedValue\" }");
 Object updatedValue = new Object();
 AtomicReferenceDeserializer deserializer = (AtomicReferenceDeserializer) atomicReference.getDeserializer();
 deserializer.updateReference(atomicReference, updatedValue);
 assertEquals("AtomicReference value should match the updated value", updatedValue, atomicReference.get());
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testAtomicReferenceDeserializationWithInvalidUpdate() {
 AtomicReference<Object> atomicReference = readValue("{ \"value\": 1 }");
 AtomicReferenceDeserializer deserializer = (AtomicReferenceDeserializer) atomicReference.getDeserializer();
 try {
 deserializer.updateReference(atomicReference, new String("invalidType"));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}