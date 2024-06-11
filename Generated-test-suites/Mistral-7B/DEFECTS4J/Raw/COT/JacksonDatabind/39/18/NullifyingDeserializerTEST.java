package com.example.junit.nullifyingdeserializer;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserialize() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_NULL.getId());
 when(mockParser.skipChildren()).thenReturn(mockParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(mockParser, mockContext);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_ValueNull() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_NULL.getId());
 when(mockParser.skipChildren()).thenReturn(mockParser);
 when(typeDeserializer.deserializeTypedFromAny(mockParser, mockContext)).thenReturn("test");

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(mockParser, mockContext, typeDeserializer);

 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithType_UnexpectedToken() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_STRING.getId());
 when(mockParser.skipChildren()).thenReturn(mockParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 deserializer.deserializeWithType(mockParser, mockContext, typeDeserializer);
 }

 @Test
 public void testDeserializeWithType_StartArray() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_START_ARRAY.getId());
 when(mockParser.skipChildren()).thenReturn(mockParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(mockParser, mockContext, typeDeserializer);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_StartObject() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.VALUE_START_OBJECT.getId());
 when(mockParser.skipChildren()).thenReturn(mockParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(mockParser, mockContext, typeDeserializer);

 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_FieldName() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getCurrentTokenId()).thenReturn(JsonToken.FIELD_NAME.getId());
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getValueAsString()).thenReturn("test");
 when(mockParser.skipChildren()).thenReturn(mockParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(mockParser, mockContext, typeDeserializer);

 assertNull(result);
 }
}