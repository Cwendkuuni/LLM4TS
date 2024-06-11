package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {

 private DeserializationContext ctxt;
 private JsonParser jp;
 private ObjectBuffer buffer;
 private StringArrayDeserializer deserializer;

 @Before
 public void setup() {
 ctxt = mock(DeserializationContext.class);
 jp = mock(JsonParser.class);
 buffer = new ObjectBuffer(16);
 deserializer = new StringArrayDeserializer();
 }

 @Test
 public void testDeserializeStandard() throws Exception {
 when(jp.isExpectedStartArrayToken()).thenReturn(true);
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jp.getText()).thenReturn("test1");
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jp.getText()).thenReturn("test2");
 when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[]{"test1", "test2"}, result);

 verify(ctxt).returnObjectBuffer(buffer);
 }

 @Test
 public void testDeserializeWithNulls() throws Exception {
 when(jp.isExpectedStartArrayToken()).thenReturn(true);
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[] {null, null}, result);

 verify(ctxt).returnObjectBuffer(buffer);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws Exception {
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 StringArrayDeserializer customDeserializer = new StringArrayDeserializer(elementDeserializer);

 String[] expectedValues = {"test1", "test2"};
 when(elementDeserializer.deserialize(jp, ctxt)).thenReturn(expectedValues[0]);
 when(elementDeserializer.deserialize(jp, ctxt)).thenReturn(expectedValues[1]);
 when(jp.isExpectedStartArrayToken()).thenReturn(true);
 when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jp.getText()).thenReturn("test1");
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jp.getText()).thenReturn("test2");
 when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = customDeserializer.deserialize(jp, ctxt);
 assertArrayEquals(expectedValues, result);

 verify(ctxt).returnObjectBuffer(buffer);
 }

 @Test
 public void testDeserializeWithEmptyList() throws Exception {
 when(jp.isExpectedStartArrayToken()).thenReturn(true);
 when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[]{}, result);

 verify(ctxt).returnObjectBuffer(buffer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws Exception {
 when(jp.isExpectedStartArrayToken()).thenReturn(true);
 when(jp.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

 deserializer.deserialize(jp, ctxt);

 verify(ctxt).mappingException(eq(String[].class));
 }

 @Test
 public void testCreateContextual() {
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 StringArrayDeserializer deserializerWithElementDeserializer = new StringArrayDeserializer(elementDeserializer);

 assertEquals(deserializerWithElementDeserializer, deserializerWithElementDeserializer.createContextual(ctxt, null));
 }

 @Test
 public void testCreateContextualWithNullElementDeserializer() {
 StringArrayDeserializer deserializerWithoutElementDeserializer = new StringArrayDeserializer();

 assertNull(deserializerWithoutElementDeserializer.createContextual(ctxt, null));
 }

 @Test
 public void testCreateContextualWithCustomDeserializer() throws Exception {
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 StringArrayDeserializer deserializerWithElementDeserializer = new StringArrayDeserializer(elementDeserializer);

 JsonDeserializer<String> contextualDeserializer = deserializerWithElementDeserializer.createContextual(ctxt, null);
 assertEquals(deserializerWithElementDeserializer, contextualDeserializer);
 }
}