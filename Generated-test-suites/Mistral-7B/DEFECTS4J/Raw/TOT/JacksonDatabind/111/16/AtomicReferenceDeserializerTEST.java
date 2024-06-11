package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

 private static ObjectMapper mapper;
 private static ObjectMapper mapperWithModule;

 static {
 try {
 mapper = new ObjectMapper();
 mapperWithModule = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 mapperWithModule.registerModule(module);
 mapperWithModule.setDefaultTyping(DefaultTyping.NON_FINAL);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
}

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 StdTypeDeserializers.NULL, null, null, null);
 AtomicReference<Object> result = serializer.deserialize(mapper, null, null);
 assertTrue(result == null);
 }

 @Test
 public void testGetEmptyValue() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 StdTypeDeserializers.emptyValue(Object.class), null, null, null);
 AtomicReference<Object> result = serializer.deserialize(mapper, null, null);
 assertNotNull(result);
 assertTrue(result.get() == null);
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 Object contents = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 null, null, null, null);
 AtomicReference<Object> result = serializer.referenceValue(contents);
 assertNotNull(result);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 null, null, null, null);
 Object result = serializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 null, null, null, null);
 serializer.updateReference(reference, newContents);
 assertEquals(newContents, reference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 null, null, null, null);
 serializer.updateReference(reference, new Object());
 fail("Update should not be supported");
 }

 @Test
 public void testSupportsUpdate() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 null, null, null, null);
 assertTrue(serializer.supportsUpdate(mapper.getConfig()));
 }
}