package com.example.junittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testGetNullValue() throws IOException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> result = serializer.getNullValue(null);
 assertNull(result.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = new AtomicReference<Object>();
 assertEquals(result, new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null).getEmptyValue(null));
 }

 @Test
 public void testReferenceValue() throws IOException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object value = new Object();
 AtomicReference<Object> result = serializer.referenceValue(value);
 assertEquals(value, result.get());
 }

 @Test
 public void testGetReferenced() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 assertEquals(reference.get(), serializer.getReferenced(reference));
 }

 @Test
 public void testUpdateReference() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
 assertEquals(newValue, updatedReference.get());
 }

 @Test
 public void testSupportsUpdate() throws IOException {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 TypeDeserializer typeDeserializer = new StdScalarValueDeserializer(new TypeReference<Object>() {});
 JsonDeserializer<Object> valueDeserializer = new StdScalarValueDeserializer(new TypeReference<Object>() {});
 AtomicReferenceDeserializer updatedSerializer = serializer.withResolved(typeDeserializer, valueDeserializer);
 assertEquals(Boolean.TRUE, updatedSerializer.supportsUpdate(null));
 }

 @Test
 public void testDeserializationWithValueDeserializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValves(new JsonDeserializationProblemHandler.Instance());
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"value\":1}";
 JsonNode node = mapper.readTree(json);
 TypeReference<AtomicReference<Number>> typeReference = new TypeReference<AtomicReference<Number>>() {};
 AtomicReference<Number> result = mapper.treeToValue(node, typeReference);
 assertEquals(1, result.get().intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidType() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValves(new JsonDeserializationProblemHandler.Instance());
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"invalidType\":1}";
 TypeReference<AtomicReference<Number>> typeReference = new TypeReference<AtomicReference<Number>>() {};
 AtomicReference<Number> result = mapper.treeToValue(mapper.readTree(json), typeReference);
 }

 @Test
 public void testDeserializationWithNullValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValves(new JsonDeserializationProblemHandler.Instance());
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "null";
 TypeReference<AtomicReference<Number>> typeReference = new TypeReference<AtomicReference<Number>>() {};
 AtomicReference<Number> result = mapper.treeToValue(mapper.readTree(json), typeReference);
 assertNull(result.get());
 }
}