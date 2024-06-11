package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.NoneTypeDeserializer;

public class AtomicReferenceDeserializerTest {

 private DeserializationContext context;
 private ObjectMapper mapper;
 private AtomicReferenceDeserializer serializer;

 @Before
 public void setup() throws Exception {
 context = new TestContext();
 mapper = new ObjectMapper();
 serializer = new AtomicReferenceDeserializer(
 new TypeFactory().constructType(AtomicReference.class, Object.class),
 mapper.getValueInstantiator(), new EmptyTypeDeserializer(), null);
 }

 // Test constructor method
 @Test
 public void testConstructor() {
 // Check arguments are not null
 assertEquals("Null fullType", null != serializer._fullType);
 assertEquals("Null inst", null != serializer._valueInstantiator);
 assertEquals("Null typeDeser", null != serializer._typeDeserializer);
 assertEquals("Null deser", null != serializer._valueDeserializer);
 }

 // Test getNullValue method
 @Test
 public void testGetNullValue() {
 AtomicReference<Object> result = serializer.getNullValue(context);
 assertNull("GetNullValue should return null", result.get());
 }

 // Test getEmptyValue method
 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = serializer.getEmptyValue(context);
 assertSame("GetEmptyValue should return empty AtomicReference", result, new AtomicReference<Object>());
 }

 // Test referenceValue method
 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> result = serializer.referenceValue(contents);
 assertSame("referenceValue should return new AtomicReference with given contents", result, new AtomicReference<Object>(contents));
 }

 // Test getReferenced method
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = serializer.getReferenced(reference);
 assertSame("getReferenced should return the value contained in the AtomicReference", result, reference.get());
 }

 // Test updateReference method
 @Test
 public void testUpdateReference() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
 assertSame("updateReference should return the same AtomicReference", serializer, serializer);
 assertSame("updatedReference should contain new value", updatedReference.get(), newValue);
 }

 // Test supportsUpdate method
 @Test
 public void testSupportsUpdate() {
 assertTrue("supportsUpdate should return true", serializer.supportsUpdate(context.getConfig()));
 }

 // Test edge cases and exceptions
 @Test(expected = IllegalStateException.class)
 public void testNullContext() {
 serializer.getNullValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullType() {
 serializer = new AtomicReferenceDeserializer(null, _valueInstantiator, _typeDeserializer, _valueDeserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueInst() {
 serializer = new AtomicReferenceDeserializer(_fullType, null, _typeDeserializer, _valueDeserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTypeDeserializer() {
 serializer = new AtomicReferenceDeserializer(_fullType, _valueInst, null, _valueDeserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueDeserializer() {
 serializer = new AtomicReferenceDeserializer(_fullType, _valueInst, _typeDeserializer, null);
 }

 @Test
 public void testDeserialize() throws Exception {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":42}";
 Object deserializedObject = mapper.readValue(json, serializer);
 AtomicReference<Object> result = (AtomicReference<Object>) deserializedObject;
 assertEquals("Deserialized value should be 42", 42, result.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"invalidJson\":42}";
 mapper.readValue(json, serializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNullContext() throws Exception {
 mapper.readValue(null, serializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNullContextDeserializer() throws Exception {
 JsonDeserializer<Object> nullContextDeserializer = null;
 Object deserializedObject = mapper.readValue(json, serializer, context, nullContextDeserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidJsonType() throws Exception {
 String json = "{\"@class\":\"java.util.concurrent.atomic.AtomicInteger\",\"value\":42}";
 Object deserializedObject = mapper.readValue(json, serializer);
 }
}