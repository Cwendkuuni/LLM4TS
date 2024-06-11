package com.example.tests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringArrayDeserializerTest {

 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private ObjectMapper objectMapper;
 private StringArrayDeserializer stringArrayDeserializer;

 @Before
 public void setUp() {
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 objectMapper = new ObjectMapper();
 stringArrayDeserializer = new StringArrayDeserializer();
 }

 @Test
 public void testDeserializeStandard() throws Exception {
 String[] expected = {"expected1", "expected2"};
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("expected1");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("expected2");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws Exception {
 String[] expected = {"expected1", "expected2"};
 JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("expected1");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("expected2");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(stringDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("deserializedValue");
 when(stringDeserializer.getNullValue()).thenReturn(null);
 when(deserializationContext.findContextualValueDeserializer(String.class, null)).thenReturn(stringDeseralizer);

 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(new String[]{"deserializedValue", "expected1", "expected2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("invalidJson");

 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullInput() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 String[] expected = {};
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeEmptyArrayWithNullValue() throws Exception {
 String[] expected = {null};
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJsonArray() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullElement() throws Exception {
 String[] expected = {null};
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyElement() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 }

 @Test
 public void testCreateContextual() throws Exception {
 JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
 BeanProperty property = mock(BeanProperty.class);
 when(deserializationContext.findContextualValueDeserializer(String.class, property)).thenReturn(stringDeserializer);
 when(stringDeserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(false);

 JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);
 assertEquals(new StringArrayDeserializer(stringDeserializer), result);
 }

 @Test
 public void testCreateContextualWithDefaultDeserializer() throws Exception {
 JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
 BeanProperty property = mock(BeanProperty.class);
 when(deserializationContext.findContextualValueDeserializer(String.class, property)).thenReturn(stringDeserializer);
 when(stringDeserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(true);

 JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);
 assertEquals(stringArrayDeserializer, result);
 }
}