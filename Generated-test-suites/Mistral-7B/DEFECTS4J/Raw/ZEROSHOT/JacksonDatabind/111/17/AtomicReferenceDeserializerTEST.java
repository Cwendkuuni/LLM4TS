package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;
import com.fasterxml.jackson.databind.JsonDeserializer.None.NoneInstance;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testEmptyConstructor() throws JsonMappingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNull(deserializer.getNullValue(null));
 assertNotNull(deserializer.getEmptyValue(null));
 AtomicReference<Object> emptyRef = deserializer.getEmptyValue(null);
 assertEquals(emptyRef.get(), null);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException, IOException {
 Object value = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> ref = deserializer.referenceValue(value);
 assertNotNull(ref);
 assertEquals(ref.get(), value);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException, IOException {
 AtomicReference<Object> ref = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals(deserializer.getReferenced(ref), ref.get());
 }

 @Test
 public void testUpdateReference() throws JsonMappingException, IOException {
 AtomicReference<Object> ref = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> updatedRef = deserializer.updateReference(ref, newValue);
 assertSame(ref, updatedRef);
 assertEquals(updatedRef.get(), newValue);
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(deserializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = new NoneInstance();
 String json = "{\"value\":\"test\"}";
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 AtomicReference<Object> result = deserializer.deserialize(mapper.readTree(json), ctxt);
 Object deserializedValue = deserializer.getReferenced(result);
 assertEquals(deserializedValue, "test");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNull() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = new NoneInstance();
 String json = null;
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 AtomicReference<Object> result = deserializer.deserialize(mapper.readTree(json), ctxt);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeEmpty() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = new NoneInstance();
 String json = "{}";
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 AtomicReference<Object> result = deserializer.deserialize(mapper.readTree(json), ctxt);
 assertNull(deserializer.getReferenced(result));
 }

 @Test
 public void testDeserializeInvalidJson() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = new NoneInstance();
 String json = "{\"value\":}";
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 AtomicReference<Object> result = deserializer.deserialize(mapper.readTree(json), ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeValueType() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = mapper.deserializerForClass(String.class);
 String json = "{\"value\":\"test\"}";
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 AtomicReference<String> result = (AtomicReference<String>) deserializer.deserialize(mapper.readTree(json), ctxt);
 assertEquals("test", deserializer.getReferenced(result));
 }

 @Test
 public void testDeserializeTypeId() throws JsonMappingException, IOException {
 Object target = new Object();
 TypeDeserializer typeDeserializer = new None();
 JsonDeserializer<Object> valueDeserializer = mapper.deserializerForClass(String.class);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"value\":\"test\"}";
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class),
         mapper.getFactory().valueInstance(Object.class), typeDeserializer, valueDeserializer);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.handleUnknownProperty("@type", typeSerializer.writeValueAsBytes(mapper.getTypeFactory().constructType(AtomicReference.class.getName())));
 AtomicReference<String> result = (AtomicReference<String>) deserializer.deserialize(mapper.readTree(json), ctxt);
 assertEquals("test", deserializer.getReferenced(result));
 }
}