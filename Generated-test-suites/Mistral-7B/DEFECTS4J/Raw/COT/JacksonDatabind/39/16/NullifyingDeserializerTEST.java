package com.example.nullifyingdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 // Test Case for deserialize method
 @Test
 public void testDeserialize() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, null));
 }

 // Test Case for deserializeWithType method when the current token is not a valid start token
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithTypeInvalidToken() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 deserializer.deserializeWithType(jsonParser, null, null);
 }

 // Test Case for deserializeWithType method when the current token is START_OBJECT
 @Test
 public void testDeserializeWithTypeStartObject() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(String.class);
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, null, typeDeserializer));
 }

 // Test Case for deserializeWithType method when the current token is START_ARRAY
 @Test
 public void testDeserializeWithTypeStartArray() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, null, typeDeserializer));
 }

 // Test Case for deserializeWithType method when the current token is FIELD_NAME
 @Test
 public void testDeserializeWithTypeFieldName() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(Person.class);
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, null, typeDeserializer));
 }
}

 class Person {
 String name;
 }