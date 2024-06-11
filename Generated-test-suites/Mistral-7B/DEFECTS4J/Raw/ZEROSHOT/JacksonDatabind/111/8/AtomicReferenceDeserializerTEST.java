package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 this.objectMapper = new ObjectMapper();
 this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testAtomicReferenceDeserializer_DefaultConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals(AtomicReferenceDeserializer.class, atomicReferenceDeserializer.getClass());
 assertNull(atomicReferenceDeserializer.getValueInstantiator());
 assertNull(atomicReferenceDeserializer.getTypeDeserializer());
 assertNull(atomicReferenceDeserializer.getValueDeserializer());
 }

 @Test
 public void testAtomicReferenceDeserializer_Constructor() throws JsonMappingException {
 AtomicReference<Integer> fullType = new TypeReference<AtomicReference<Integer>>() {}.getType();
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @SuppressWarnings("unchecked")
 public Object createValueInstance(DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return new AtomicReference<Integer>();
 }
 };
 TypeDeserializer typeDeserializer = new StdTypeDeserializer<AtomicReference<Integer>>(AtomicReference.class);
 JsonDeserializer<Object> valueDeserializer = new StdDeserializer<Object>(Object.class);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
 assertEquals(AtomicReferenceDeserializer.class, atomicReferenceDeserializer.getClass());
 assertEquals(fullType, atomicReferenceDeserializer.getFullType());
 assertEquals(valueInstantiator, atomicReferenceDeserializer.getValueInstantiator());
 assertEquals(typeDeserializer, atomicReferenceDeserializer.getTypeDeserializer());
 assertEquals(valueDeserializer, atomicReferenceDeserializer.getValueDeserializer());
 }

 @Test
 public void testGetNullValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(null);
 assertNotNull(nullValue);
 assertNull(nullValue.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object emptyValue = atomicReferenceDeserializer.getEmptyValue(null);
 assertNotNull(emptyValue);
 assertNull(emptyValue);
 }

 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object contents = new Object();
 AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);
 assertNotNull(referenceValue);
 assertEquals(contents, referenceValue.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object referenced = atomicReferenceDeserializer.getReferenced(reference);
 assertNotNull(referenced);
 assertEquals(reference.get(), referenced);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, newContents);
 assertEquals(reference, updatedReference);
 assertEquals(newContents, reference.get());
 }

 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(AtomicReference.class, new AtomicReferenceDeserializer(null, null, null, null));
 }});
 String jsonString = "{\"value\": 123}";
 AtomicReference<Integer> expectedResult = new AtomicReference<Integer>(123);
 AtomicReference<Integer> result = objectMapper.readValue(jsonString, new TypeReference<AtomicReference<Integer>>() {}.getType());
 assertEquals(expectedResult, result);
 assertEquals(123, result.get());
 }
}