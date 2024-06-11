package com.example.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws JsonProcessingException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(new BaseContext(), null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws JsonProcessingException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]"); // JSON Array
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(int[].class);
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new BaseContext(), null), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithStringAsFieldName() throws JsonProcessingException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"fieldName\": 1}");
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new BaseContext(), null), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithUnexpectedToken() throws JsonProcessingException, IOException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"UnexpectedToken\"");
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new BaseContext(), null), null);
 fail("Unexpected null result for unexpected token");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidToken() throws JsonProcessingException, JsonMappingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"invalidToken\"");
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 try {
 deserializer.deserializeWithType(jsonParser, new DeserializationContext(new BaseContext(), null), null);
 fail("Expected JsonMappingException for invalid token");
 } catch (IOException e) {
 // Expected IOException, handle as JsonMappingException
 throw new JsonMappingException(e.getMessage());
 }
 }
}