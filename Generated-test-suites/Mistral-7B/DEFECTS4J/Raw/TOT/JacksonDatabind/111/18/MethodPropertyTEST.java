package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.fasterxml.jackson.databind.JsonTest.MAPPER;
import static org.junit.Assert.*;

public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private Object targetObject;

 @Before
 public void setUp() throws Exception {
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 Method method = mock(Method.class);
 when(annotatedMethod.getAnnotated()).thenReturn(method);
 when(method.getName()).thenReturn("setName");
 when(method.getParameterTypes()).thenReturn(new Class[]{String.class});
 when(method.getDeclaringClass()).thenReturn(Object.class);
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 targetObject = new Object();
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 String value = "John Doe";
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenReturn(value);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(deserializer.deserialize(mockParser, context)).thenReturn(value);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);

 methodProperty.deserializeAndSet(mockParser, context, targetObject);
 assertEquals("John Doe", targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test
 public void testDeserializeAndSet_NullValue() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenReturn(null);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(deserializer.deserialize(mockParser, context)).thenReturn(null);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);

 methodProperty.deserializeAndSet(mockParser, context, targetObject);
 assertNull(targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSet_IOException() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenThrow(new IOException());
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);

 methodProperty.deserializeAndSet(mockParser, context, targetObject);
 fail("IOException should be thrown");
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 String value = "John Doe";
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenReturn(value);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(deserializer.deserialize(mockParser, context)).thenReturn(value);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);
 when(methodProperty.deserializeSetAndReturn(mockParser, context, targetObject)).thenReturn("John Doe X");

 assertEquals("John Doe X", targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test
 public void testDeserializeSetAndReturn_NullValue() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenReturn(null);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(deserializer.deserialize(mockParser, context)).thenReturn(null);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);
 when(methodProperty.deserializeSetAndReturn(mockParser, context, targetObject)).thenReturn(null);

 assertNull(targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturn_IOException() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getValueAsString()).thenThrow(new IOException());
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 when(context.findBeanDeserializer(String.class)).thenReturn(deserializer);
 when(context.findBeanProperty("name", null)).thenReturn(methodProperty);

 assertEquals(null, methodProperty.deserializeSetAndReturn(mockParser, context, targetObject));
 fail("IOException should be thrown");
 }

 @Test
 public void testSet() throws Exception {
 String value = "John Doe";
 methodProperty.set(targetObject, value);
 assertEquals("John Doe", targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test
 public void testSet_NullValue() throws Exception {
 methodProperty.set(targetObject, null);
 assertNull(targetObject.getClass().getDeclaredField("name").get(targetObject));
 }

 @Test(expected = IOException.class)
 public void testSet_IOException() throws Exception {
 throw new IOException();
 }

 @Test
 public void testSetAndReturn() throws Exception {
 String value = "John Doe";
 Object result = methodProperty.setAndReturn(targetObject, value);
 assertEquals("John Doe", targetObject.getClass().getDeclaredField("name").get(targetObject));
 assertEquals("John Doe", result);
 }

 @Test
 public void testSetAndReturn_NullValue() throws Exception {
 Object result = methodProperty.setAndReturn(targetObject, null);
 assertNull(targetObject.getClass().getDeclaredField("name").get(targetObject));
 assertEquals(targetObject, result);
 }

 @Test(expected = IOException.class)
 public void testSetAndReturn_IOException() throws Exception {
 throw new IOException();
 }

 @Test
 public void testFixAccess() throws Exception {
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 methodProperty.fixAccess(MAPPER.getDeserializationConfig());
 verify(annotatedMember).fixAccess(MAPPER.getDeserializationConfig().isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }
}