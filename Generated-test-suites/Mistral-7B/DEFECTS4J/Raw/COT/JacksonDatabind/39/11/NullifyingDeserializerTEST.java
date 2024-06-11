package com.example.junit.tests.jackson.deserializer;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;

import org.junit.Test;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.START_ARRAY.getId());

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, context);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_Array() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.START_ARRAY.getId());
 when(typeDeserializer.deserializeTypedFromAny(jsonParser, context)).thenThrow(JsonMappingException.class);
 when(objectMapper.getTypeFactory().constructCollectionType(NullifyingDeserializer.class, Integer.class)).thenReturn(java.util.List.class);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_Object() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.START_OBJECT.getId());
 when(typeDeserializer.deserializeTypedFromAny(jsonParser, context)).thenThrow(JsonMappingException.class);
 when(objectMapper.getTypeFactory().constructType(String.class)).thenReturn(String.class);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_FieldName() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.FIELD_NAME.getId());
 when(objectMapper.getTypeFactory().constructValueType(NullifyingDeserializer.class, Object.class)).thenReturn(Object.class);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);

 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithType_UnexpectedToken() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_STRING.getId());
 when(typeDeserializer.deserializeTypedFromAny(jsonParser, context)).thenReturn("Unexpected token (VALUE_STRING)");

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 }

 @Test
 public void testDeserializeWithType_NoneTypeDeserializer() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = new None();

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonToken.START_OBJECT.getId());

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);

 assertNull(result);
 }
}