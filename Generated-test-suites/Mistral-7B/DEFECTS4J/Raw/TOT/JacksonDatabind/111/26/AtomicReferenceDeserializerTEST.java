package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class AtomicReferenceDeserializerTest {

 private static final JavaType ATOMIC_REFERENCE_TYPE = ClassUtil.constructType(AtomicReference.class, Object.class);

 @Test
 public void testDeserialization() throws Exception {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE, new ValueInstantiator() {
 @Override
 public Object createValueInstance(DeserializationContext ctxt, BeanProperty property) throws IOException {
 return new AtomicReference<Object>();
 }
 }, mock(TypeDeserializer.class), mock(JsonDeserializer.class));
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();

 // Test serialization and deserialization of null value
 AtomicReference<Object> nullReference = deserializer.getNullValue(ctxt);
 assertEquals(null, nullReference.get());

 // Test serialization and deserialization of empty value
 AtomicReference<Object> emptyReference = deserializer.getEmptyValue(ctxt);
 assertNull(emptyReference.get());

 // Test serialization and deserialization of referenced value
 Object referencedValue = new Object();
 AtomicReference<Object> reference = deserializer.referenceValue(referencedValue);
 assertEquals(referencedValue, deserializer.getReferenced(reference));

 // Test update reference
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, new Object());
 assertEquals(updatedReference.get(), referencedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithJsonMappingException() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(ATOMIC_REFERENCE_TYPE,
 new ValueInstantiator() {
 @Override
 public Object createValueInstance(DeserializationContext ctxt, BeanProperty property) throws IOException {
 return new AtomicReference<Object>();
 }
 }, mock(TypeDeserializer.class), mock(JsonDeserializer.class));
 String json = "{\"@class\":\"com.example.AtomicReference\",\"value\":\"error\"}";
 Converter.StandardConverter standardConverter = new Converter.StandardConverter(JsonLocation.ROOT, null,
 new EmptyTypeDeserializer(), null);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 Object readValue = mapper.readerForValueType(ATOMIC_REFERENCE_TYPE)
 .withContext(ctxt).readValue(json, ATOMIC_REFERENCE_TYPE, standardConverter);
 AtomicReference<Object> reference = (AtomicReference<Object>) readValue;
 when(deserializer.getReferenced(reference)).thenThrow(new JsonMappingException("Test error"));
 deserializer.getReferenced(reference);
 }
}