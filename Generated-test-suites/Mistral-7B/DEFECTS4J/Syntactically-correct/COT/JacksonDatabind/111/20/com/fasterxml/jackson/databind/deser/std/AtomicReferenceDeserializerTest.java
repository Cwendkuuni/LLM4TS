package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonContent;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 // Test constructor method
 @Test
 public void testAtomicReferenceDeserializerConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertNotNull(atomicReferenceDeserializer);
 }

 // Test method: withResolved
 @Test
 public void testWithResolved() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReferenceDeserializer deserializer2 = deserializer1.withResolved(null, null);
 assertNotSame(deserializer1, deserializer2);
 }

 // Test method: getNullValue
 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> nullReference = atomicReferenceDeserializer.getNullValue(null);
 assertNotNull(nullReference);
 assertEquals(null, nullReference.get());
 }

 // Test method: getEmptyValue
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> emptyReference = atomicReferenceDeserializer.getEmptyValue(null);
 assertNotNull(emptyReference);
 assertEquals(null, emptyReference.get());
 }

 // Test method: referenceValue
 @Test
 public void testReferenceValue() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object content = new Object();
 AtomicReference<Object> atomicReference = atomicReferenceDeserializer.referenceValue(content);
 assertNotNull(atomicReference);
 assertEquals(content, atomicReference.get());
 }

 // Test method: getReferenced
 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object referencedObject = atomicReferenceDeserializer.getReferenced(atomicReference);
 assertNotNull(referencedObject);
 assertEquals(atomicReference.get(), referencedObject);
 }

 // Test method: updateReference
 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object newContent = new Object();
 AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(atomicReference, newContent);
 assertEquals(atomicReference, updatedReference);
 assertEquals(newContent, atomicReference.get());
 }

 // Test method: supportsUpdate
 @Test
 public void testSupportsUpdate() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 // Test edge cases and exceptions
 // Test null input for constructor
 @Test(expected = IllegalArgumentException.class)
 public void testAtomicReferenceDeserializerConstructorWithNull() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 }

 // Test null input for getNullValue
 @Test(expected = JsonMappingException.class)
 public void testGetNullValueWithNull() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertNull(atomicReferenceDeserializer.getNullValue(null));
 }

 // Test null input for getEmptyValue
 @Test
 public void testGetEmptyValueWithNull() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> emptyReference = atomicReferenceDeserializer.getEmptyValue(null);
 assertNotNull(emptyReference);
 assertEquals(null, emptyReference.get());
 }

 // Test null input for referenceValue
 @Test(expected = IllegalArgumentException.class)
 public void testReferenceValueWithNull() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object content = null;
 atomicReferenceDeserializer.referenceValue(content);
 }

 // Test null input for getReferenced
 @Test(expected = IllegalArgumentException.class)
 public void testGetReferencedWithNull() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> atomicReference = null;
 atomicReferenceDeserializer.getReferenced(atomicReference);
 }

 // Test null input for updateReference
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateReferenceWithNull() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object newContent = new Object();
 atomicReferenceDeserializer.updateReference(null, newContent);
 }

 // Test empty input for getNullValue
 @Test
 public void testGetNullValueWithEmpty() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> nullReference = atomicReferenceDeserializer.getNullValue(new Object());
 assertNotNull(nullReference);
 assertEquals(null, nullReference.get());
 }

 // Test empty input for getEmptyValue
 @Test
 public void testGetEmptyValueWithEmpty() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> emptyReference = atomicReferenceDeserializer.getEmptyValue(new Object());
 assertNotNull(emptyReference);
 assertEquals(null, emptyReference.get());
 }

 // Test empty input for referenceValue
 @Test
 public void testReferenceValueWithEmpty() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object content = new Object();
 AtomicReference<Object> atomicReference = new AtomicReference<Object>();
 atomicReferenceDeserializer.referenceValue(content);
 assertEquals(content, atomicReference.get());
 }

 // Test null input for getReferenced
 @Test(expected = JsonMappingException.class)
 public void testGetReferencedWithNullInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("type", "AtomicReference");
 rootNode.put("value", null);
 JsonParser jsonParser = mapper.getFactory().createParser(rootNode.toString());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 JsonDeserializer<Object> deserializer = atomicReferenceDeserializer.createDeserializer(null, jsonParser);
 deserializer.deserialize(jsonParser, atomicReference, null);
 }

 // Test empty input for getReferenced
 @Test(expected = JsonMappingException.class)
 public void testGetReferencedWithEmptyInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("type", "AtomicReference");
 JsonParser jsonParser = mapper.getFactory().createParser(rootNode.toString());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 JsonDeserializer<Object> deserializer = atomicReferenceDeserializer.createDeserializer(null, jsonParser);
 deserializer.deserialize(jsonParser, atomicReference, null);
 assertEquals(null, atomicReference.get());
 }

 // Test null input for updateReference
 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceWithNullInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("type", "AtomicReference");
 rootNode.put("value", new Object());
 JsonParser jsonParser = mapper.getFactory().createParser(rootNode.toString());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 JsonDeserializer<Object> deserializer = atomicReferenceDeserializer.createDeserializer(null, jsonParser);
 Object newContent = new Object();
 deserializer.deserialize(jsonParser, atomicReference, null);
 atomicReferenceDeserializer.updateReference(atomicReference, newContent);
 }

 // Test empty input for updateReference
 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceWithEmptyInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("type", "AtomicReference");
 rootNode.put("value", new Object());
 JsonParser jsonParser = mapper.getFactory().createParser(rootNode.toString());
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 JsonDeserializer<Object> deserializer = atomicReferenceDeserializer.createDeserializer(null, jsonParser);
 Object newContent = new Object();
 deserializer.deserialize(jsonParser, atomicReference, null);
 atomicReferenceDeserializer.updateReference(atomicReference, newContent);
 assertEquals(atomicReference.get(), atomicReference.get());
 }

 // Test NPE for getReferenced with empty AtomicReference
 @Test(expected = NullPointerException.class)
 public void testGetReferencedWithEmptyAtomicReference() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>();
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertNull(atomicReferenceDeserializer.getReferenced(atomicReference));
 }

 // Test NPE for updateReference with null AtomicReference
 @Test(expected = NullPointerException.class)
 public void testUpdateReferenceWithNullAtomicReference() throws JsonProcessingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object newContent = new Object();
 AtomicReference<Object> atomicReference = null;
 atomicReferenceDeserializer.updateReference(atomicReference, newContent);
 }

 // Test NPE for updateReference with empty AtomicReference
 @Test(expected = NullPointerException.class)
 public void testUpdateReferenceWithEmptyAtomicReference() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>();
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object newContent = new Object();
 atomicReferenceDeserializer.updateReference(atomicReference, newContent);
 assertEquals(atomicReference.get(), atomicReference.get());
 }
}