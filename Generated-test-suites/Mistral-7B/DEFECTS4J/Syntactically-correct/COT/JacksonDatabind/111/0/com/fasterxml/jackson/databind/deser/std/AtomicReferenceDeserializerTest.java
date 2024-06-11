package com.yourpackage.tests;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private AtomicReference<Object> reference;
 private DeserializationContext ctxt;
 private TypeDeserializer typeDeser;
 private JsonDeserializer<?> valueDeser;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 typeDeser = Mockito.mock(TypeDeserializer.class);
 valueDeser = Mockito.mock(JsonDeserializer.class);
 ctxt = Mockito.mock(DeserializationContext.class);
 deserializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class, null), Mockito.mock(ValueInstantiator.class), typeDeser, valueDeser);
 reference = new AtomicReference<>();
 }

 @Test
 public void testConstructor() {
 // Basic test for constructor
 assertNotNull(deserializer);
 }

 @Test
 public void testWithResolved() {
 // Test for withResolved method
 AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeser, valueDeser);
 assertNotSame(deserializer, newDeserializer);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 // Test for getNullValue method
 AtomicReference<Object> returnedNullValue = deserializer.getNullValue(ctxt);
 assertNotNull(returnedNullValue);
 }

 @Test
 public void testGetEmptyValue() {
 // Test for getEmptyValue method
 AtomicReference<Object> returnedEmptyValue = deserializer.getEmptyValue(ctxt);
 assertNotNull(returnedEmptyValue);
 }

 @Test
 public void testReferenceValue() {
 // Test for referenceValue method
 Object contents = new Object();
 AtomicReference<Object> returnedValue = deserializer.referenceValue(contents);
 assertEquals(contents, returnedValue.get());
 }

 @Test
 public void testGetReferenced() {
 // Test for getReferenced method
 Object initialValue = new Object();
 reference.set(initialValue);
 AtomicReference<Object> deserializedValue = deserializer.getReferenced(reference);
 assertEquals(initialValue, deserializedValue);
 }

 @Test
 public void testUpdateReference() {
 // Test for updateReference method
 Object newValue = new Object();
 deserializer.updateReference(reference, newValue);
 assertEquals(newValue, reference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSupportsUpdateWithDisableValueDeserializer() {
 // Test for supportsUpdate method with value deserializer disabled
 deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class, null), null, typeDeser, valueDeser);
 assertFalse(deserializer.supportsUpdate(null));
 }

 @Test
 public void testSupportsUpdateWithEnabledValueDeserializer() {
 // Test for supportsUpdate method with value deserializer enabled
 deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class, null), Mockito.mock(ValueInstantiator.class), typeDeser, valueDeser);
 assertTrue(deserializer.supportsUpdate(null));
 }
}