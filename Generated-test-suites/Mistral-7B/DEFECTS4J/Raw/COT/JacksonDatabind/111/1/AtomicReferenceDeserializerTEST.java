package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 // Test constructor
 @Test
 public void testAtomicReferenceDeserializerConstructor() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertNotNull(atomicReferenceDeserializer);
 }

 // Test getNullValue method
 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals(new AtomicReference<>(), atomicReferenceDeserializer.getNullValue(null));
 }

 // Test getEmptyValue method
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals(new AtomicReference<>(), atomicReferenceDeserializer.getEmptyValue(null));
 }

 // Test referenceValue method
 @Test
 public void testReferenceValue() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 AtomicReference<Object> expected = new AtomicReference<Object>("expectedValue");
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "expectedValue");
 AtomicReference<Object> actual = atomicReferenceDeserializer.referenceValue(node);
 assertEquals(expected, actual);
 }

 // Test getReferenced method
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("testValue");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertEquals("testValue", atomicReferenceDeserializer.getReferenced(atomicReference));
 }

 // Test updateReference method
 @Test
 public void testUpdateReference() {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("testValue");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 Object newValue = "newValue";
 atomicReferenceDeserializer.updateReference(atomicReference, newValue);
 assertEquals(newValue, atomicReference.get());
 }

 // Test supportsUpdate method
 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
 }

 // Test deserialization with null value
 @Test
 public void testDeserializationWithNullValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.readTree("null");
 AtomicReference<Object> actual = atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 assertNull(actual.get());
 }

 // Test deserialization with empty value
 @Test
 public void testDeserializationWithEmptyValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.readTree("{}");
 AtomicReference<Object> actual = atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 assertNotNull(actual);
 assertEquals(new AtomicReference<>(), actual);
 }

 // Test deserialization with AtomicReference value
 @Test
 public void testDeserializationWithAtomicReferenceValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.createObjectNode();
 node.put("value", "testValue");
 AtomicReference<Object> expected = new AtomicReference<Object>("testValue");
 AtomicReference<Object> actual = (AtomicReference<Object>) atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 assertEquals(expected, actual);
 }

 // Test deserialization with wrong type
 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithWrongType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.createObjectNode();
 node.put("value", new Integer(1));
 atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 }

 // Test deserialization with null TypeDeserializer
 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullTypeDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.createObjectNode();
 node.put("value", new ObjectNode());
 atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 }

 // Test deserialization with TypeDeserializer that doesn't support deserialization
 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithTypeDeserializerThatDoesntSupportDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TypeDeserializer typeDeserializer = new StubTypeDeserializer();
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, typeDeserializer, null);
 JsonNode node = mapper.createObjectNode();
 node.put("value", new ObjectNode());
 atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 }

 // Test deserialization with null JsonDeserializer
 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullJsonDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
 JsonNode node = mapper.createObjectNode();
 node.put("value", new ObjectNode());
 atomicReferenceDeserializer.deserialize(null, null, null, node, null);
 }

}