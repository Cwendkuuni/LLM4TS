package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonText;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setup() {
 JavaType fullType = StdSerializer.constructType(AtomicReference.class, Object.class);
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException {
 return new AtomicReference<Object>();
 }
 };
 TypeDeserializer typeDeserializer = null;
 JsonDeserializer<?> valueDeserializer = null;
 deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
 }

 // Test methods for constructor AtomicReferenceDeserializer(JavaType fullType, ValueInstantiator inst, TypeDeserializer typeDeser, JsonDeserializer<?> deser)
 @Test
 public void testAtomicReferenceDeserializerConstruction() {
 // Basic test
 assertNotNull(deserializer);
 }

 // Test methods for method getNullValue(DeserializationContext ctxt)
 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> result = deserializer.getNullValue(null);
 assertNull(result.get());
 }

 // Test methods for method getEmptyValue()
 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(null);
 assertNull(result.get());
 }

 // Test methods for method referenceValue(Object contents)
 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 // Test methods for method getReferenced(AtomicReference<Object> reference)
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = deserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 // Test methods for method updateReference(AtomicReference<Object> reference, Object contents)
 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object contents = new Object();
 deserializer.updateReference(reference, contents);
 assertEquals(contents, reference.get());
 }

 // Test methods for method supportsUpdate(DeserializationConfig config)
 @Test
 public void testSupportsUpdate() {
 assertTrue(deserializer.supportsUpdate(null));
 }

 // Edge cases and exception handling tests

 @Test(expected = NullPointerException.class)
 public void testGetNullValueNullContext() throws JsonMappingException {
 deserializer.getNullValue(null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetNullValueInvalidContext() throws JsonMappingException {
 DeserializationContext context = new DeserializationContext(new JsonFactory(), null, null);
 deserializer.getNullValue(context);
 }

 @Test(expected = JsonMappingException.class)
 public void testReferenceValueNullContents() throws JsonMappingException {
 deserializer.referenceValue(null);
 }

 @Test(expected = InstantiationException.class)
 public void testGetEmptyValueInvalidContext() {
 deserializer.getEmptyValue(null);
 }

 @Test(expected = InstantiationException.class)
 public void testReferenceValueNullReference() throws JsonMappingException {
 AtomicReference<Object> reference = null;
 deserializer.referenceValue(reference);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetReferencedNullReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(null);
 deserializer.getReferenced(reference);
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNullReference() {
 AtomicReference<Object> reference = null;
 Object contents = new Object();
 deserializer.updateReference(reference, contents);
 }
}