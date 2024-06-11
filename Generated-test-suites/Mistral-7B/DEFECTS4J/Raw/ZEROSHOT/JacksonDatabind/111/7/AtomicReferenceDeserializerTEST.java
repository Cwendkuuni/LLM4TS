package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @org.junit.BeforeClass
 static void setUpBeforeClass() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new ParameterNamesModule());
 AtomicReferenceDeserializer.findAndRegisterDeserializer(objectMapper);
 }

 @org.junit.Test
 public void testEmptyDeserialization() throws Exception {
 ObjectNode json = mapper.createObjectNode();
 AtomicReference<Object> reference = mapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}.getType());
 assertNull(reference.get());
 }

 @org.junit.Test
 public void testNullDeserialization() throws Exception {
 ObjectNode json = mapper.createObjectNode();
 json.put("value", null);
 AtomicReference<Object> reference = mapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}.getType());
 assertNull(reference.get());
 }

 @org.junit.Test
 public void testValueDeserialization() throws Exception {
 ObjectNode json = mapper.createObjectNode();
 json.put("value", 123);
 AtomicReference<Object> reference = mapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}.getType());
 assertEquals(123, reference.get());
 }

 @org.junit.Test
 public void testUpdateDeserialization() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(123);
 ObjectNode json = mapper.valueToTree(reference);
 AtomicReference<Object> updatedReference = mapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}.getType());
 updatedReference.set(456);
 assertEquals(456, updatedReference.get());
 }

 @org.junit.Test
 public void testDeserializerWithType() throws Exception {
 ObjectNode json = mapper.createObjectNode();
 json.put("value", new TypeReference<AtomicReference<String>>() {}.getType());
 AtomicReference<String> reference = mapper.readValue(json, new TypeReference<AtomicReference<String>>() {}.getType());
 assertNull(reference.get());
 }

 @org.junit.Test
 public void testDeserializerWithWrongType() throws Exception {
 ObjectNode json = mapper.createObjectNode();
 json.put("value", new TypeReference<AtomicInteger>() {}.getType());
 try {
 AtomicReference<Object> reference = mapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}