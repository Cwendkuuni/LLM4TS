package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private ObjectMapper objectMapper;
 private SetterlessProperty setterlessProperty;

 @Before
 public void setup() {
 ObjectMapper.DefaultTypingResolver defaultTyper = new ObjectMapper.DefaultTypingResolver();
 defaultTyper.activateAnnotations(true);
 ObjectMapper.DefaultTypingResolver.Builder defaultTypingBuilder = new ObjectMapper.DefaultTypingResolver.Builder();
 defaultTypingBuilder.activeAnnotations(true);
 ObjectMapper.DefaultTypingResolver.Builder defaultTypingBuilder2 = new ObjectMapper.DefaultTypingResolver.Builder();
 defaultTypingBuilder2.activeAnnotations(true);

 SimpleModule module = new SimpleModule();
 module.setSerializerProvider(defaultTypingBuilder.build());
 module.setDeserializerProvider(defaultTypingBuilder2.build());
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(module);

 try {
 TypeDeserializer mapTypeDeserializer = objectMapper.getTypeFactory().constructTypeDeserializer(Map.class);
 setterlessProperty = new SetterlessProperty(new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(MyClass.class.getMethod("getMyMap")), null), null, null), Object.class, mapTypeDeserializer, null, null);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException, IllegalAccessException, InvocationTargetException {
 Object instance = new MyClass();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");

 // Test with valid JSON input
 String jsonInput = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonInput);
 setterlessProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals("value1", instance.getMyMap().get("key1"));
 assertEquals("value2", instance.getMyMap().get("key2"));

 // Test with empty JSON input
 jsonParser.nextToken(); // consume [START_OBJECT] token
 setterlessProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertTrue(instance.getMyMap().isEmpty());

 // Test with null JSON input
 jsonParser.nextToken(JsonToken.VALUE_NULL);
 setterlessProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertTrue(instance.getMyMap() == null);

 // Test with invalid JSON input
 jsonParser.nextToken(JsonToken.VALUE_STRING);
 jsonParser.nextToken(JsonToken.COMMA);
 setterlessProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertTrue(instance.getMyMap() == null);
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException, IllegalAccessException, InvocationTargetException {
 Object instance = new MyClass();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");

 // Test with valid JSON input
 String jsonInput = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonInput);
 Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals(instance, result);
 assertEquals("value1", instance.getMyMap().get("key1"));
 assertEquals("value2", instance.getMyMap().get("key2"));

 // Test with empty JSON input
 jsonParser.nextToken(); // consume [START_OBJECT] token
 Object result2 = setterlessProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals(instance, result2);
 assertTrue(instance.getMyMap().isEmpty());

 // Test with null JSON input
 jsonParser.nextToken(JsonToken.VALUE_NULL);
 Object result3 = setterlessProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals(instance, result3);
 assertTrue(instance.getMyMap() == null);

 // Test with invalid JSON input
 jsonParser.nextToken(JsonToken.VALUE_STRING);
 jsonParser.nextToken(JsonToken.COMMA);
 Object result4 = setterlessProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals(instance, result4);
 assertTrue(instance.getMyMap() == null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 Object instance = new MyClass();
 setterlessProperty.set(instance, new HashMap<>());
 fail("Should not call `set()` on setterless property");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 Object instance = new MyClass();Object result = setterlessProperty.setAndReturn(instance, new HashMap<>());
 fail("Should not call `set()` on setterless property");
 assertEquals(instance, result);
 }
}

class MyClass {
 private Map<String, String> myMap;

 public Map<String, String> getMyMap() {
 return myMap;
 }
}