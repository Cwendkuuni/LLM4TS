package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

 private ObjectMapper mapper;
 private FieldProperty fieldProperty;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 try {
 fieldProperty = mapper.findDeserializer(String.class, Field.class);
 } catch (Exception e) {
 throw new RuntimeException("Failed to initialize test", e);
 }
 }

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 Object nullValue = fieldProperty.getNullProvider().getNullValue(new DeserializationContext.Builder()
 .forDeserializer(fieldProperty)
 .build());
 Object instance = new Object();
 fieldProperty.deserializeAndSet(mapper.getFactory().createParser("null"), new DeserializationContext(), instance);
 assertEquals(nullValue, instance._field);
 }

 @Test
 public void testDeserializeStringValue() throws JsonProcessingException {
 Object instance = new Object();
 String testValue = "test";
 ObjectNode node = mapper.createObjectNode();
 node.put("field", testValue);
 Object parsedValue = fieldProperty.deserializeSetAndReturn(mapper.getFactory().createParser(node.toString()), new DeserializationContext(), instance);
 assertEquals(testValue, instance._field.get(0));
 }

 @Test
 public void testDeserializeObjectValue() throws JsonProcessingException {
 Object instance = new Object();
 Object testObject = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.putObject("testObject", testObject);
 node.put("field", node);
 Object parsedValue = fieldProperty.deserializeSetAndReturn(mapper.getFactory().createParser(node.toString()), new DeserializationContext(), instance);
 assertEquals(testObject, instance._field.get(0));
 }

 @Test
 public void testDeserializeNullObjectValue() throws JsonProcessingException {
 Object instance = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("field");
 Object parsedValue = fieldProperty.deserializeSetAndReturn(mapper.getFactory().createParser(node.toString()), new DeserializationContext(), instance);
 assertNull(instance._field.get(0));
 }

 @Test
 public void testDeserializeInvalidValue() throws JsonProcessingException {
 Object instance = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.put("field", 42);
 JsonDeserializer<Object> intDeserializer = mapper.findDeserializer(Object.class, int.class);
 fieldProperty = new FieldProperty(fieldProperty, intDeserializer, fieldProperty.getNullProvider());
 Object parsedValue = fieldProperty.deserializeSetAndReturn(mapper.getFactory().createParser(node.toString()), new DeserializationContext(), instance);
 assertNull(instance._field.get(0));
 }

 @Test
 public void testDeserializeWithType() throws JsonProcessingException {
 Object instance = new Object();
 TypeDeserializer typeDeserializer = mapper.findValueDeserializer(String[].class, String.class);
 Object parsedValue = fieldProperty.deserializeWithType(mapper.getFactory().createParser("[\"" + "test" + "\"]"), new DeserializationContext(), instance, typeDeserializer);
 assertArrayEquals(new String[]{"test"}, instance._field);
 }

 @Test
 public void testSetValue() throws JsonProcessingException {
 Object instance = new Object();
 String testValue = "test";
 instance._field = testValue;
 fieldProperty.set(instance, "testFromDeserializer");
 assertEquals("testFromDeserializer", instance._field.get(0));
 }

 @Test
 public void testSetValueNull() throws JsonProcessingException {
 Object instance = new Object();
 instance._field = null;
 fieldProperty.set(instance, null);
 assertNull(instance._field.get(0));
 }

 @Test
 public void testSetValueInvalidType() {
 Object instance = new Object();
 String testValue = "test";
 instance._field = testValue;
 try {
 fieldProperty.set(instance, 42);
 fail("Should have thrown an exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}