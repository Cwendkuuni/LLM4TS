package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer serializer;
 private AtomicReference<String> atomicReference;

 @Before
 public void setUp() {
 serializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class, JavaType.constructUnboundType(Object.class)),
 null, new EmptyTypeDeserializer(), null);
 atomicReference = new AtomicReference<>("Initial Value");
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> nullValue = serializer.getNullValue(null);
 assertNull(nullValue.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> emptyValue = serializer.getEmptyValue(null);
 assertEquals("Initial Value", emptyValue.get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = new Object();
 AtomicReference<Object> referencedValue = serializer.referenceValue(contents);
 assertEquals(contents, referencedValue.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 assertEquals("Initial Value", serializer.getReferenced(atomicReference));
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 Object newContent = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(atomicReference, newContent);
 assertEquals(newContent, updatedReference.get());
 assertEquals(newContent, atomicReference.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 DeserializationConfig config = null;
 assertTrue(serializer.supportsUpdate(config));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws JsonMappingException, JsonProcessingException {
 Object newContent = new Object();
 DeserializationContext context = new DeserializationContext(null, new StandardDeserializationContext());
 AtomicReference<Object> referencedValue = serializer.referenceValue(newContent);
 try {
 serializer.updateReference(referencedValue, newContent);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 serializer.updateReference(referencedValue, atomicReference.get());
}

 @Test
 public void testDeserialize() throws JsonMappingException, JsonProcessingException {
 String json = "{\"value\": \"Test Value\"}";
 JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
 DeserializationContext context = new DeserializationContext(introspector, new StandardDeserializationContext());
 JsonParser parser = context.parserBuilder().build(json);
 AtomicReference<Object> deserializedValue = serializer.deserialize(parser, context);
 assertEquals("Test Value", deserializedValue.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonMappingException, JsonProcessingException {
 String json = "{";
 JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
 DeserializationContext context = new DeserializationContext(introspector, new StandardDeserializationContext());
 JsonParser parser = context.parserBuilder().build(json);
 serializer.deserialize(parser, context);
 fail("Expected JsonMappingException not thrown");
 }
}