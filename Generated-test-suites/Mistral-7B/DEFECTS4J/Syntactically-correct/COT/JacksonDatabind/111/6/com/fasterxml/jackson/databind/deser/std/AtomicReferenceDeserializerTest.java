package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(AtomicReference.class, new AtomicReferenceDeserializer(null, null, null, null));
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(module);
 }

 // Testing constructor method
 @Test
 public void testAtomicReferenceDeserializerConstruction() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNull(atomicReferenceDeserializer.getFullType());
 assertNull(atomicReferenceDeserializer.getValueInstantiator());
 assertNull(atomicReferenceDeserializer.getTypeDeserializer());
 assertNull(atomicReferenceDeserializer.getValueDeserializer());
 }

 // Testing withResolved method
 @Test
 public void testWithResolved() throws JsonMappingException {
 AtomicReferenceDeserializer original = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReferenceDeserializer updated = original.withResolved(null, null);
 assertSame(original.getClass(), updated.getClass());
 assertNotSame(original, updated);
 }

 // Testing getNullValue method
 @Test
 public void testGetNullValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNull(atomicReferenceDeserializer.getNullValue(null));
 }

 // Testing getEmptyValue method
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> emptyReference = atomicReferenceDeserializer.getEmptyValue(null);
 assertEquals(emptyReference.getClass(), AtomicReference.class);
 }

 // Testing referenceValue method
 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object contents = new Object();
 AtomicReference<Object> reference = atomicReferenceDeserializer.referenceValue(contents);
 assertEquals(reference.getClass(), AtomicReference.class);
 assertEquals(reference.get(), contents);
 }

 // Testing getReferenced method
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals(atomicReferenceDeserializer.getReferenced(reference), reference.get());
 }

 // Testing updateReference method
 @Test
 public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object newContents = new Object();
 assertEquals(atomicReferenceDeserializer.updateReference(reference, newContents), reference);
 assertEquals(reference.get(), newContents);
 }

 // Testing supportsUpdate method
 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 // Testing deserialization with null value
 @Test
 public void testDeserializeNullValue() throws Exception {
 ObjectNode node = objectMapper.createObjectNode();
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = objectMapper.readValue(node, AtomicReference.class);
 assertEquals(expected, actual);
 }

 // Testing deserialization with empty value
 @Test
 public void testDeserializeEmptyValue() throws Exception {
 ObjectNode node = objectMapper.createObjectNode();
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = objectMapper.readValue(node, AtomicReference.class);
 assertEquals(expected, actual);
 }

 // Testing deserialization with existing value
 @Test
 public void testDeserializeWithValue() throws Exception {
 ObjectNode node = objectMapper.createObjectNode();
 Object contents = new Object();
 node.put("value", contents);
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = objectMapper.readValue(node, AtomicReference.class);
 assertEquals(expected, actual);
 assertEquals(expected.get(), contents);
 }

 // Testing deserialization with incorrect type
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithIncorrectType() throws Exception {
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "notAnAtomicReference");
 objectMapper.readValue(node, AtomicReference.class);
 }
}