package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;

public class AtomicReferenceDeserializerTest {

 private DeserializationContext context;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<Object> valueDeserializer;
 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 typeDeserializer = mock(TypeDeserializer.class);
 valueDeserializer = mock(JsonDeserializer.class);
 deserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), typeDeserializer, valueDeserializer);
 }

 @Test
 public void testEmptyConstructor() {
 AtomicReference<Object> result = deserializer.getEmptyValue(context);
 assertNull(result);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> result = deserializer.getNullValue(context);
 assertNull(result);
 }

 @Test
 public void testReferenceValue() {
 AtomicReference<Object> result = deserializer.referenceValue("test");
 assertEquals("test", result.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 when(context.getActiveContext()).thenReturn(context);
 when(context.findValueDeserializer(deserializer.getTypeDescriptor(AtomicReference.class, Object.class)))
         .thenReturn(deserializer);
 when(valueDeserializer.deserialize(context, null)).thenReturn("test");

 AtomicReference<Object> result = deserializer.getReferenced(reference);
 assertEquals("test", result.get());
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 when(context.getActiveContext()).thenReturn(context);
 when(context.findValueDeserializer(deserializer.getTypeDescriptor(AtomicReference.class, Object.class)))
         .thenReturn(deserializer);
 when(valueDeserializer.deserialize(context, null)).thenReturn("updatedTest");

 AtomicReference<Object> updatedResult = deserializer.updateReference(reference, "updatedTest");
 assertEquals("updatedTest", updatedResult.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testUnsupportedUpdate() throws JsonMappingException {
 when(deserializer.supportsUpdate(context)).thenReturn(Boolean.FALSE);
 assertNull(deserializer.getEmptyValue(context));
 }

}