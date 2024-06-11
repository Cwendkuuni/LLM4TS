package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test case for NullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(objectMapper.getFactory(), null, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(String[].class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(objectMapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithUnexpectedToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 deserializer.deserialize(jsonParser, new DeserializationContext(objectMapper.getFactory(), null, null));
 fail("Should have thrown IOException for unexpected token 'NUMBER'");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTokenIdNotFound() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("null");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 deserializer.deserializeWithType(jsonParser, new DeserializationContext(objectMapper.getFactory(), null, null), null);
 fail("Should have thrown JsonProcessingException for unrecognized token 'NULL'");
 }
}