package com.example.junit.tests;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<?> valueDeserializer;
 private DeserializationContext ctxt;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 typeDeserializer = Mockito.mock(TypeDeserializer.class);
 valueDeserializer = Mockito.mock(JsonDeserializer.class);
 ctxt = Mockito.mock(DeserializationContext.class);
 deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class), null, typeDeserializer, valueDeserializer);
 }

 // Test constructor
 @Test
 public void testConstructor() {
 assertNotNull(deserializer);
 }

 // Test withResolved method
 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeserializer, valueDeserializer);
 assertNotSame(deserializer, newDeserializer);
 }

 // Test getNullValue method
 @Test
 public void testGetNullValue() {
 AtomicReference<Object> result = deserializer.getNullValue(ctxt);
 assertNotNull(result);
 assertNull(result.get());
 }

 // Test getEmptyValue method
 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(ctxt);
 assertNotNull(result);
 assertNull(result.get());
 }

 // Test referenceValue method
 @Test
 public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertNotNull(result);
 assertSame(contents, result.get());
 }

 // Test getReferenced method
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = deserializer.getReferenced(reference);
 assertNotNull(result);
 assertSame(reference.get(), result);
 }

 // Test updateReference method
 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 assertSame(reference, updatedReference);
 assertSame(newValue, reference.get());
 }

 // Test supportsUpdate method
 @Test
 public void testSupportsUpdate() {
 assertTrue(deserializer.supportsUpdate(Mockito.mock(DeserializationConfig.class)));
 }

}