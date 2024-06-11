package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullValue() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> deserialized = serializer.deserialize(mapper.getFactory(), null, null);
 assertNull(deserialized.get());
 }

 @Test
 public void testEmptyValue() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> deserialized = serializer.deserialize(mapper.getFactory(), null, null);
 assertEquals(AtomicReference.class, deserialized.getClass());
 assertNull(deserialized.get());
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object contents = new Object();
 AtomicReference<Object> deserialized = serializer.referenceValue(contents);
 assertEquals(contents, deserialized.get());
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object deserialized = serializer.getReferenced(reference);
 assertEquals(reference.get(), deserialized);
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
 assertEquals(updatedReference, reference);
 assertEquals(newValue, updatedReference.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 assertTrue(serializer.supportsUpdate(null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws JsonProcessingException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<String>>() {}, null, null, null) {
 @Override
 public AtomicReference<Object> updateReference(AtomicReference<Object> reference, Object contents) {
 throw new UnsupportedOperationException();
 }
 };
 Object reference = new AtomicReference<>("initial");
 Object newValue = "updated";
 AtomicReference<Object> deserialized = serializer.updateReference(reference, newValue);
 fail("updateReference should have thrown UnsupportedOperationException");
 }

 @Test
 public void testDeserialize() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 Object value = mapper.readValue(node, String.class);
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> deserialized = serializer.deserialize(mapper.getFactory(), node, null);
 assertEquals(value, deserialized.get());
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 Object value = mapper.readValue(node, String.class);
 JavaType atomicReferenceType = mapper.getTypeFactory()
 .constructType(AtomicReference.class, Object.class);
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> deserialized = serializer.deserialize(mapper.getFactory(), node, atomicReferenceType);
 assertEquals(value, deserialized.get());
 assertEquals(AtomicReference.class, deserialized.getClass());
 }
}