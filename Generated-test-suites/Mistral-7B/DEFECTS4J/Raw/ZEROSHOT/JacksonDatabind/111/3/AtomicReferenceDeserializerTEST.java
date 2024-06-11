package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new JsonMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testAtomicReferenceDeserializer_NullValue() throws JsonProcessingException {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReference<Object> actual = AtomicReferenceDeserializer.instance.getNullValue(objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testAtomicReferenceDeserializer_EmptyValue() {
 AtomicReference<Object> expected = new AtomicReference<>();
 AtomicReference<Object> actual = AtomicReferenceDeserializer.instance.getEmptyValue(objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testAtomicReferenceDeserializer_ReferenceValue() throws JsonProcessingException {
 Object contents = "Test Contents";
 AtomicReference<Object> expected = new AtomicReference<>(contents);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("c", "Test Contents");
 AtomicReference<Object> actual = AtomicReferenceDeserializer.instance.referenceValue(contents);
 assertEquals(expected, actual);

 String json = objectMapper.writerWithTypeValue().writeValueAsString(node);
 AtomicReference<Object> deserialized = objectMapper.readValue(json, AtomicReference.class);
 assertEquals(expected, deserialized);
 }

 @Test
 public void testAtomicReferenceDeserializer_GetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("Test Contents");
 Object actual = AtomicReferenceDeserializer.instance.getReferenced(reference);
 assertEquals("Test Contents", actual);
 }

 @Test
 public void testAtomicReferenceDeserializer_UpdateReference() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>("Test Contents");
 Object newContents = "New Contents";
 AtomicReference<Object> expected = new AtomicReference<Object>(newContents);
 ObjectMapper mapper = new JsonMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 ObjectNode node = mapper.createObjectNode();
 node.put("c", "New Contents");

 AtomicReference<Object> actual = AtomicReferenceDeserializer.instance.updateReference(reference, newContents);
 assertEquals(expected, actual);

 String json = mapper.writerWithTypeValue().writeValueAsString(node);
 AtomicReference<Object> deserialized = mapper.readValue(json, AtomicReference.class);
 assertEquals(expected, deserialized);
 }

 @Test
 public void testAtomicReferenceDeserializer_SupportsUpdate() {
 assertTrue(AtomicReferenceDeserializer.instance.supportsUpdate(null));
 }

}