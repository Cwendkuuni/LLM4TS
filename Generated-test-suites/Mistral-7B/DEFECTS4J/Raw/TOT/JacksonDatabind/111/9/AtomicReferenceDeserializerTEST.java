package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetNullValue() throws JsonMappingException, JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = deserializer.getNullValue(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> actual = deserializer.getEmptyValue(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> expected = new AtomicReference<Object>("test");
 JsonNode jsonNode = mapper.readTree(node.toString());
 AtomicReference<Object> actual = deserializer.referenceValue(jsonNode.get(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException, JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException, JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("test");
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "newTest");
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 JsonNode jsonNode = mapper.readTree(node.toString());
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, jsonNode.get(0));
 assertEquals(updatedReference.get(), "newTest");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws JsonMappingException, JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 MapperFactory.standard().constructType(AtomicReference.class), null, null, null);
 deserializer.updateReference(null, null);
 }
}