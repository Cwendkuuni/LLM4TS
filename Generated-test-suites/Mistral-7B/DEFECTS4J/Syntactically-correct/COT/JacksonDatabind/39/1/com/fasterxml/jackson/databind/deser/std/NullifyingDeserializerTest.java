package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws Exception {
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 JsonParser mockJsonParser = createMockJsonParser();
 DeserializationContext context = createMockDeserializationContext();

 Object result = serializerUnderTest.deserialize(mockJsonParser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_JsonTokenId_ID_START_ARRAY() throws Exception {
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 JsonParser mockJsonParser = createMockJsonParser(JsonTokenId.ID_START_ARRAY);
 DeserializationContext context = createMockDeserializationContext();
 TypeDeserializer typeDeserializer = createMockTypeDeserializer();

 Object result = serializerUnderTest.deserializeWithType(mockJsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_JsonTokenId_ID_START_OBJECT() throws Exception {
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 JsonParser mockJsonParser = createMockJsonParser(JsonTokenId.ID_START_OBJECT);
 DeserializationContext context = createMockDeserializationContext();
 TypeDeserializer typeDeserializer = createMockTypeDeserializer();

 Object result = serializerUnderTest.deserializeWithType(mockJsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_JsonTokenId_ID_FIELD_NAME() throws Exception {
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 JsonParser mockJsonParser = createMockJsonParser(JsonTokenId.ID_FIELD_NAME);
 DeserializationContext context = createMockDeserializationContext();
 TypeDeserializer typeDeserializer = createMockTypeDeserializer();

 Object result = serializerUnderTest.deserializeWithType(mockJsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithType_UnexpectedJsonTokenId() throws Exception {
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 JsonParser mockJsonParser = createMockJsonParser(JsonTokenId.ID_VALUE_STRING);
 DeserializationContext context = createMockDeserializationContext();
 TypeDeserializer typeDeserializer = createMockTypeDeserializer();

 Object result = serializerUnderTest.deserializeWithType(mockJsonParser, context, typeDeserializer);
 fail("Deserialization should fail with IOException when an unexpected JsonTokenId is encountered.");
 }

 private JsonParser createMockJsonParser() {
 return createMock(JsonParser.class);
 }

 private JsonParser createMockJsonParser(int jsonTokenId) {
 JsonParser mockJsonParser = createMock(JsonParser.class);
 when(mockJsonParser.getCurrentTokenId()).thenReturn(jsonTokenId);
 return mockJsonParser;
 }

 private DeserializationContext createMockDeserializationContext() {
 return createMock(DeserializationContext.class);
 }

 private TypeDeserializer createMockTypeDeserializer() {
 return createMock(TypeDeserializer.class);
 }
}