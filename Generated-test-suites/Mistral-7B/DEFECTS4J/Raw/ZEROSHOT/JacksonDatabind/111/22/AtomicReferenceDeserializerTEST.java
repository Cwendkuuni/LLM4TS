package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private DeserializationContext context;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<?> valueDeserializer;

 @Before
 public void setup() throws Exception {
 context = mock(DeserializationContext.class);
 typeDeserializer = new EmptyTypeDeserializer();
 valueDeserializer = mock(JsonDeserializer.class);
 deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {},
 valueDeserializer, typeDeserializer, context);
 }

 @Test
 public void testGetNullValue() throws IOException {
 AtomicReference<Object> result = deserializer.getNullValue(context);
 assertNull(result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(context);
 assertNull(result);
 }

 @Test
 public void testReferenceValue() throws IOException {
 Object contents = "test content";
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>("initial value");
 when(valueDeserializer.deserialize(context, new StringBuffer("test value"), TypeReference.TYPE_ANY_PRIMITIVE))
 .thenReturn("test value");
 AtomicReference<Object> result = deserializer.getReferenced(reference);
 assertEquals("test value", result.get());
 }

 @Test
 public void testUpdateReference() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>("initial value");
 Object updatedValue = "updated value";
 deserializer.updateReference(reference, updatedValue);
 assertEquals(updatedValue, reference.get());
 }

 @Test
 public void testWithResolved() {
 JavaType fullType = mock(JavaType.class);
 ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
 AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeserializer, valueDeserializer);
 assertEquals(deserializer.getClass(), newDeserializer.getClass());
 assertEquals(fullType, newDeserializer.getFullType());
 assertEquals(valueInstantiator, newDeserializer.getValueInstantiator());
 assertEquals(typeDeserializer, newDeserializer.getTypeDeserializer());
 assertEquals(valueDeserializer, newDeserializer.getValueDeserializer());
 }
}