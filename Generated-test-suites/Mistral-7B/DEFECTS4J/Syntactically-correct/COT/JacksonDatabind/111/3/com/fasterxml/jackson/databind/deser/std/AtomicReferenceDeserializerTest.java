package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

 // Test constructor
 @Test
 public void testAtomicReferenceDeserializerConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNotNull(atomicReferenceDeserializer);
 }

 // Test method: withResolved
 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer original = new AtomicReferenceDeserializer(null, null, null, null);
 TypeDeserializer typeDeserializer = null;
 JsonDeserializer<?> valueDeserializer = null;
 AtomicReferenceDeserializer newInstance = original.withResolved(typeDeserializer, valueDeserializer);
 assertNotSame(original, newInstance);
 }

 // Test method: getNullValue
 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 DeserializationContext context = mock(DeserializationContext.class);
 AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(context);
 assertTrue(result == null || result instanceof AtomicReference);
 }

 // Test method: getEmptyValue
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> result = atomicReferenceDeserializer.getEmptyValue(null);
 assertTrue(result instanceof AtomicReference);
 }

 // Test method: referenceValue
 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object contents = new Object();
 AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
 assertSame(result.get(), contents);
 }

 // Test method: getReferenced
 @Test
 public void testGetReferenced() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = atomicReferenceDeserializer.getReferenced(reference);
 assertSame(result, reference.get());
 }

 // Test method: updateReference
 @Test
 public void testUpdateReference() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object contents = new Object();
 AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);
 assertSame(updatedReference, reference);
 assertSame(updatedReference.get(), contents);
 }

 // Test method: supportsUpdate
 @Test(expected = AssertionError.class)
 public void testSupportsUpdateThrowsExceptionWhenFalse() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertFalse(atomicReferenceDeserializer.supportsUpdate(null));
 }

 // Test method: supportsUpdate
 @Test
 public void testSupportsUpdateReturnsTrue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

}