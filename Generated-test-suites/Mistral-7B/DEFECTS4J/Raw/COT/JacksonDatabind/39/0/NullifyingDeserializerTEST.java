package com.example.nullifyingdeserializertest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 // Testing for deserialize method
 @Test
 public void testDeserialize() throws JsonProcessingException {
 Object result = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), NullifyingDeserializer.class)
 .deserialize(new JsonFactory(), new DeserializationContext());
 assertNull(result);
 }

 // Testing for deserializeWithType method when JsonTokenId is not ID_FIELD_NAME, ID_START_ARRAY or ID_START_OBJECT
 @Test(expected = IOException.class)
 public void testDeserializeWithTypeInvalidToken() throws JsonProcessingException {
 Object result = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), NullifyingDeserializer.class)
 .deserializeWithType(new JsonFactory(), new DeserializationContext(), new SimpleTypeDeserializer(String.class));
 }

 // Testing for deserializeWithType method when JsonTokenId is ID_FIELD_NAME
 @Test
 public void testDeserializeWithTypeFieldName() throws JsonProcessingException {
 Object result = objectMapper.readerForType(String.class)
 .readValue(new JsonFactory(), new DeserializationContext(), new JsonPointer("/fieldName"),
 new StandardDeserializer(Object.class));
 Object deserializedResult = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), NullifyingDeserializer.class)
 .deserializeWithType(new JsonFactory(), new DeserializationContext(), new SimpleTypeDeserializer(String.class));
 assertEquals(result, deserializedResult);
 }

 // Testing for deserializeWithType method when JsonTokenId is ID_START_ARRAY
 @Test
 public void testDeserializeWithTypeStartArray() throws JsonProcessingException {
 Object[] result = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), ArrayType.get(Object[].class))
 .deserializeAll(new JsonFactory(), new DeserializationContext());
 Object[] deserializedResult = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), NullifyingDeserializer.class)
 .deserializeWithType(new JsonFactory(), new DeserializationContext(), new ArrayType(Object[].class));
 assertArrayEquals(result, deserializedResult);
 }

 // Testing for deserializeWithType method when JsonTokenId is ID_START_OBJECT
 @Test
 public void testDeserializeWithTypeStartObject() throws JsonProcessingException {
 Map<String, Object> result = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), MapType.construct(String.class, Object.class))
 .deserializeAll(new JsonFactory(), new DeserializationContext());
 Map<String, Object> deserializedResult = objectMapper.readValue(new ObjectReader(objectMapper.getFactory()), NullifyingDeserializer.class)
 .deserializeWithType(new JsonFactory(), new DeserializationContext(), new MapType(String.class, Object.class));
 assertEquals(result, deserializedResult);
 }
}