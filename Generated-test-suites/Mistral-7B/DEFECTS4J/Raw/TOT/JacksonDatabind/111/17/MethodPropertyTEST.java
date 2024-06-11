package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.MethodProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodPropertyTest {

 private Object targetObject;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private MethodProperty methodProperty;

 @Before
 public void setup() throws Exception {
 targetObject = new Object();
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType type = mock(JavaType.class);
 TypeDeserializer typeDeser = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertNull(targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testNonNullValue() throws JsonProcessingException {
 Object value = new Object();
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertEquals(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testNullValueProvider() throws JsonProcessingException {
 when(mockContext.findNullValueProvider()).thenReturn(null);
 methodProperty = new MethodProperty(methodProperty, null);
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertNull(targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test(expected = IOException.class)
 public void testException() throws Exception {
 Object value = new Object();
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenThrow(new IOException("Test Exception"));
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 }

 @Test(expected = IOException.class)
 public void testInvokeException() throws Exception {
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
 when(targetObject.getClass().getDeclaredMethod("setPropertyName", Object.class)).thenThrow(new IOException("Test Exception"));
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 }

 @Test
 public void testDeserializeAndSetNullValueWithValueDeserializer() throws JsonProcessingException {
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 Object nullValue = mockContext.findNullValueProvider().getNullValue(mockContext);
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenReturn(nullValue);
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertNull(targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testDeserializeAndSetValueWithValueDeserializer() throws JsonProcessingException {
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 Object value = new Object();
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertEquals(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testDeserializeAndSetValueWithTypeDeserializer() throws JsonProcessingException {
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 Object value = new Object();
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(mockValueDeserializer.deserializeWithType(mockParser, mockContext, typeDeserializer)).thenReturn(value);
 methodProperty.deserializeAndSet(mockParser, mockContext, targetObject);
 assertEquals(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testSetValue() throws Exception {
 Object value = new Object();
 methodProperty.set(targetObject, value);
 assertEquals(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testSetValueAndReturnNull() throws Exception {
 Object value = new Object();
 when(targetObject.getClass().getDeclaredField("propertyName").get(targetObject)).thenReturn(new Object());
 methodProperty.setAndReturn(targetObject, value);
 assertNull(targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testSetValueAndReturnSameObject() throws Exception {
 Object value = new Object();
 when(targetObject.getClass().getDeclaredField("propertyName").get(targetObject)).thenReturn(value);
 methodProperty.setAndReturn(targetObject, value);
 assertEquals(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testSetValueAndReturnNewObject() throws Exception {
 Object value = new Object();
 Object newValue = new Object();
 when(targetObject.getClass().getDeclaredField("propertyName").get(targetObject)).thenReturn(value);
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenReturn(newValue);
 methodProperty.setAndReturn(mockParser, mockContext, targetObject);
 assertNotSame(value, targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test
 public void testSetValueWithInvokeException() throws Exception {
 Object value = new Object();
 when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(targetObject.getClass().getDeclaredMethod("setPropertyName", Object.class)).thenThrow(new IOException("Test Exception"));
 methodProperty.setAndReturn(mockParser, mockContext, targetObject);
 }

 @Test
 public void testSetValueWithNullValue() throws Exception {
 Object value = null;
 methodProperty.set(targetObject, value);
 assertNull(targetObject.getClass().getDeclaredField("propertyName").get(targetObject));
 }

 @Test(expected = IOException.class)
 public void testSetValueWithNullParser() throws Exception {
 methodProperty.setAndReturn(null, targetObject, value);
 }
}