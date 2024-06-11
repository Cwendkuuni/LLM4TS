package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 assertNotNull(serializer);
 }

 @Test
 public void testWithResolved() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 AtomicReferenceDeserializer newSerializer = serializer.withResolved(null, null);
 assertNotSame(serializer, newSerializer);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 AtomicReference<Object> nullValue = serializer.getNullValue(null);
 assertNotNull(nullValue);
 assertEquals(null, nullValue.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 Object emptyValue = serializer.getEmptyValue(null);
 assertNotNull(emptyValue);
 assertEquals(null, emptyValue);
 }

 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 AtomicReference<Object> referenceValue = serializer.referenceValue(contents);
 assertNotNull(referenceValue);
 assertEquals(contents, referenceValue.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 Object contents = new Object();
 AtomicReference<Object> reference = new AtomicReference<Object>(contents);
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 Object referenced = serializer.getReferenced(reference);
 assertEquals(contents, referenced);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 Object newContents = new Object();
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newContents);
 assertEquals(updatedReference, reference);
 assertEquals(newContents, updatedReference.get());
 }

 @Test (expected = JsonMappingException.class)
 public void testSupportsUpdateWithInvalidConfig() throws JsonMappingException {
 ObjectMapper invalidConfigMapper = new ObjectMapper();
 DeserializationConfig invalidConfig = mapper.getConfiguration();
 invalidConfig.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 assertFalse(serializer.supportsUpdate(invalidConfig));
 }

 @Test
 public void testSupportsUpdateWithValidConfig() {
 ObjectMapper validConfigMapper = new ObjectMapper();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){}, null, null, null);
 assertTrue(serializer.supportsUpdate(validConfigMapper.getConfiguration()));
 }

 @Test
 public void testDeserializeValue() throws JsonMappingException, JsonProcessingException {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":1}";
 ObjectReader reader = mapper.readerForValueType(AtomicReference.class);
 Object deserializedValue = reader.readValue(new StringReader(json));
 AtomicReference<Object> deserializedAtomicReference = (AtomicReference<Object>) deserializedValue;
 Object contents = deserializedAtomicReference.get();
 assertEquals(1, contents);
 }

 @Test
 public void testDeserializeWithToStringSerializer() throws JsonMappingException, JsonProcessingException {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":{\"@class\":\"com.fasterxml.jackson.databind.ser.std.ToStringSerializer\",\"value\":\"Hello World\"}}";
 ObjectReader reader = mapper.readerForValueType(AtomicReference.class);
 Object deserializedValue = reader.readValue(new StringReader(json));
 AtomicReference<Object> deserializedAtomicReference = (AtomicReference<Object>) deserializedValue;
 ObjectToStringSerializer stringSerializer = new ObjectToStringSerializer(String.class, null);
 mapper.addSerializer(String.class, stringSerializer);
 String deserializedString = (String) deserializedAtomicReference.get();
 assertEquals("Hello World", deserializedString);
 }
}