package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.type.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.deser.impl.MethodProperty.*;

public class MethodPropertyTest {

 private ObjectMapper mapper;
 private MethodProperty property;
 private Object instance;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 property = new MethodProperty(mock(BeanPropertyDefinition.class),
 jerseyTypeFactory, null, mock(Annotations.class),
 mock(AnnotatedMethod.class));
 instance = new Object();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsInt()).thenReturn(1);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(parser.getValueAsInt()).thenReturn(2);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.getDefaultValueFactory()).thenReturn(mapper.getDefaultValueFactory());

 property.deserializeAndSet(parser, ctxt, instance);

 verify(instance).invoke(eq(_setter), eq(1));
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsInt()).thenReturn(null);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.getDefaultValueFactory()).thenReturn(mapper.getDefaultValueFactory());

 if (property._skipNulls) {
 property.deserializeAndSet(parser, ctxt, instance);
 verifyNoMoreInteractions(instance);
 } else {
 property.deserializeAndSet(parser, ctxt, instance);
 verify(instance).invoke(eq(_setter), null);
 }
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsInt()).thenReturn(null);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.getDefaultValueFactory()).thenReturn(mapper.getDefaultValueFactory());

 Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsInt()).thenReturn(1);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.getDefaultValueFactory()).thenReturn(mapper.getDefaultValueFactory());

 Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 Object newValue = new Object();
 property.set(instance, newValue);
 verify(instance).invoke(_setter, eq(newValue));
 }

 @Test
 public void testSetNull() throws IOException {
 Object newValue = null;
 property.set(instance, newValue);
 verify(instance).invoke(_setter, null);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 doThrow(new IOException("Test Error")).when(parser).nextToken();
 property.deserializeAndSet(parser, ctxt, instance);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionInSetter() throws Exception {
 doThrow(new IOException("Test Error")).when(instance).invoke(_setter, any());
 property.deserializeAndSet(parser, ctxt, instance);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.nextValue()).thenReturn("Test String");
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.nextValue()).thenReturn("Test Type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(typeDeserializer.deserialize(parser, ctxt, String.class)).thenReturn("Test Value");

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.findValueDeserializer(eq(String.class))).thenReturn(new JsonDeserializer<String>() {});
 when(ctxt.findValueDeserializer(eq(String[].class))).thenReturn(new JsonDeserializer<String[]>() {});
 when(ctxt.findContentConverter(eq(String.class), eq(String[].class))).thenReturn(null);

 property = new MethodProperty(property, null, typeDeserializer);
 property.deserializeAndSet(parser, ctxt, instance);

 verify(instance).invoke(_setter, "Test Value");
 }

 @Test
 public void testDeserializeWithTypeNull() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.nextValue()).thenReturn(null);
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.nextValue()).thenReturn("Test Type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.findValueDeserializer(eq(String.class))).thenReturn(new JsonDeserializer<String>() {});
 when(ctxt.findValueDeserializer(eq(String[].class))).thenReturn(new JsonDeserializer<String[]>() {});
 when(ctxt.findContentConverter(eq(String.class), eq(String[].class))).thenReturn(null);

 property = new MethodProperty(property, null, typeDeserializer);
 property.deserializeAndSet(parser, ctxt, instance);

 verifyNoMoreInteractions(instance);
 }
}