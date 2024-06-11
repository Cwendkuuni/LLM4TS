package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StringArrayDeserializerTest {

 @Test
 void testDefaultDeserialization() throws JsonProcessingException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("element1");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("element2");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"element1", "element2"}, result);
 }

 @Test
 void testCustomDeserialization() throws JsonProcessingException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);

 when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("element1");
 when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("customDeserializedElement");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("element2");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(context.findContextualValueDeserializer(Mockito.any(Type.class), Mockito.any(BeanProperty.class))).thenReturn(elementDeserializer);

 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"customDeserializedElement", "element2"}, result);
 }

 @Test
 void testEmptyArray() throws JsonProcessingException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 void testNullValue() throws JsonProcessingException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{null}, result);
 }

 @Test
 void testNonArray() throws JsonProcessingException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("nonArrayValue");

 try {
 String[] result = deserializer.deserialize(jsonParser, context);
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}