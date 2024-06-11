package com.example.jackson.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class NullifyingDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingDeserializer() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(parser, new DeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(ArrayList.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializerDifferentToken() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\"}");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializerSameToken() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructArrayType(Integer.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 assertArrayEquals(new Object[0], result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializerObject() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{}");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithTypeNullifyingDeserializerInvalidToken() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("5");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 fail("Expected JsonMappingException but got " + result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializerArray() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructArrayType(Integer.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
 assertArrayEquals(new Object[0], result);
 }
}