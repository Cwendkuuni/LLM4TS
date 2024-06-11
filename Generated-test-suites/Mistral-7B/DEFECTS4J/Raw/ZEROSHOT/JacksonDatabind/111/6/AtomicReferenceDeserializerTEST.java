package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 objectMapper = new ObjectMapper();
 objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 AtomicReference<Object> actual = deserializer.getNullValue(new DeserializationContext(objectMapper, null));
 assertNull(actual.get());
 }

 @Test
 public void testEmptyValue() {
 AtomicReference<Object> actual = deserializer.getEmptyValue(new DeserializationContext(objectMapper, null));
 assertNotNull(actual);
 assertNull(actual.get());
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 Object contents = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(expected, actual);
 assertEquals(contents, actual.get());
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReference<Object> actual = deserializer.updateReference(reference, newContents);
 assertEquals(reference, actual);
 assertEquals(newContents, reference.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonProcessingException {
 assertTrue(deserializer.supportsUpdate(objectMapper.getConfig()));
 }

 @Test
 public void testDeserializeWithTypeId() throws JsonProcessingException {
 ObjectMapper mapper = objectMapper.copy();
 mapper.setTypeFactory(new StdTypeFactory());
 TypeDeserializerProvider typeDeserializers = mapper.getTypeDeserializers();
 TypeDeserializer typeDeserializer = typeDeserializers.findValueDeserializer(AtomicReference.class, null);
 AtomicReference<Object> expected = new AtomicReference<Object>(new Object());
 JsonNode source = objectMapper.readTree("{'@type':'com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer','value':{'@class':'java.lang.String','value':'Hello'}}");
 AtomicReference<Object> actual = mapper.readValue(source, typeDeserializer.getType());
 assertEquals(expected, actual);
 assertEquals("Hello", actual.get());
 }

 @Test
 public void testDeserializeWithoutTypeId() throws JsonProcessingException {
 ObjectNode source = objectMapper.createObjectNode();
 source.put("value", "Hello");
 AtomicReference<Object> actual = objectMapper.readValue(source, AtomicReference.class);
 AtomicReference<Object> expected = new AtomicReference<Object>("Hello");
 assertEquals(expected, actual);
 assertEquals("Hello", actual.get());
 }
}