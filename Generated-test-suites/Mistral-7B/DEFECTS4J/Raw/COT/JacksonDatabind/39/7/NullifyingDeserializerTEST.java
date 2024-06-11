package com.example.junit.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private TypeDeserializer emptyTypeDeserializer = new EmptyTypeDeserializer();

 @Test
 public void testDeserialize() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.asInt());
 when(jsonParser.skipChildren()).thenReturn(jsonParser);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, deserializationContext);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.asInt());
 when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
 when(deserializationContext.findBeanDeserializer(Object.class)).thenReturn(deserializer);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithTypeWhenNotSupportedTokens() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_VALUE_STRING.asInt());
 when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
 when(deserializationContext.findBeanDeserializer(Object.class)).thenReturn(deserializer);

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWhenIoException() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);

 when(jsonParser.getCurrentTokenId()).thenThrow(new IOException());

 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 deserializer.deserialize(jsonParser, deserializationContext);
 }
}