package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 JavaType type = JavaType.construct(AtomicReference.class, Object.class);
 TypeDeserializer typeDeser = new None();
 ValueInstantiator valueInst = new ValueInstantiator() {
 @SuppressWarnings("unchecked")
 public Object instantiateValue(DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws JsonMappingException {
 return new AtomicReference<Object>();
 }
 };
 JsonDeserializer<Object> valueDeser = new StubJsonDeserializer<>();
 deserializer = new AtomicReferenceDeserializer(type, valueInst, typeDeser, valueDeser);
 }

 @Test
 public void testConstructor() {
 assertNotNull(deserializer);
 }

 @Test
 public void testWithResolved() {
 TypeDeserializer typeDeser = new None();
 JsonDeserializer<Object> valueDeser = new StubJsonDeserializer<>();
 AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeser, valueDeser);
 assertEquals(deserializer.getClass(), newDeserializer.getClass());
 }

 @Test
 public void testGetNullValue() {
 AtomicReference<Object> result = deserializer.getNullValue(null);
 assertNotNull(result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(null);
 assertNotNull(result);
 }

 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = deserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceNullContext() {
 AtomicReference<Object> reference = new AtomicReference<Object>();
 try {
 deserializer.updateReference(reference, new Object());
 } catch (JsonMappingException e) {
 throw new RuntimeException(e);
 }
 fail("Update should throw UnsupportedOperationException when no context provided");
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNullReference() {
 try {
 deserializer.updateReference(null, new Object());
 } catch (UnsupportedOperationException e) {
 throw new RuntimeException(e);
 }
 fail("Update should throw JsonMappingException when reference is null");
 }

 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> result = deserializer.updateReference(reference, newValue);
 assertEquals(newValue, reference.get());
 assertEquals(newValue, result.get());
 }

 @Test
 public void testSupportsUpdate() {
 assertTrue(deserializer.supportsUpdate(null));
 }

}

 class StubJsonDeserializer<T> implements JsonDeserializer<T> {
 @Override
 public T deserialize(DeserializationContext ctxt, BeanProperty property, Object value) throws JsonMappingException {
 return null;
 }
}