package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FieldPropertyTest {

 private Object testInstance;
 private FieldProperty fieldProperty;
 private AnnotatedField annotatedField;
 private Field testField;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private NullValueProvider mockNullValueProvider;
 private TypeDeserializer mockTypeDeserializer;
 private Object testValue;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 testInstance = new Object();
 annotatedField = mock(AnnotatedField.class);
 testField = mock(Field.class);
 when(annotatedField.getAnnotated()).thenReturn(testField);
 fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(new String("testProperty"), Object.class, null),
 ObjectType.DEFAULT, null, null, annotatedField);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 // Given
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockNullValueProvider = mock(NullValueProvider.class);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn("{\"testProperty\":\"testValue\"}");
 mockTypeDeserializer = mock(TypeDeserializer.class);
 when(mockContext.findTypeDeserializer(Object.class, JsonType.FIELD_DESCRIPTOR)).thenReturn(mockTypeDeserializer);

 // When
 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

 // Then
 verify(mockContext).handleNonWritableProperty(_annotated, testInstance, testField, testValue);
 assertEquals("testValue", testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testDeserializeAndSetNullValue() throws IOException {
 // Given
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 // When
 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

 // Then
 if (fieldProperty._skipNulls) {
 verifyNoMoreInteractions(mockContext);
 } else {
 verify(mockContext).handleNullValue(_annotated, testInstance, testField, null);
 assertEquals(mockNullValueProvider.getNullValue(mockContext), testInstance.getClass().getField("testProperty").get(testInstance));
 }
 }

 @Test
 public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
 // Given
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockNullValueProvider = mock(NullValueProvider.class);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn("{\"testProperty\":{\"@type\":\"testType\",\"value\":\"testValue\"}}");
 mockTypeDeserializer = mock(TypeDeserializer.class);
 when(mockContext.findValueDeserializer(Object.class, JsonType.FIELD_DESCRIPTOR, "testType")).thenReturn(mockTypeDeserializer);

 // When
 fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(new String("testProperty"), Object.class, null),
 ObjectType.DEFAULT, null, null, annotatedField, mockTypeDeserializer);
 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

 // Then
 verify(mockContext).handleNonWritableProperty(_annotated, testInstance, testField, testValue);
 assertEquals("testValue", testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithException() throws IOException {
 // Given
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 when(mockJsonParser.nextToken()).thenThrow(new IOException("Test exception"));

 // When
 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

 // Then
 verifyNoMoreInteractions(mockContext);
 }

 @Test
 public void testSet() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 // Given
 testValue = new Object();

 // When
 fieldProperty.set(testInstance, testValue);

 // Then
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testSetWithException() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 // Given
 doThrow(new IOException("Test exception")).when(testField).set(testInstance, testValue);

 // When
 // Then
 assertThrows(IOException.class, () -> fieldProperty.set(testInstance, testValue));
 }

 @Test
 public void testSetAndReturn() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 // Given
 Object testInstanceCopy = testInstance;

 // When
 Object returnedInstance = fieldProperty.setAndReturn(testInstanceCopy, testValue);

 // Then
 assertEquals(testInstanceCopy, returnedInstance);
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testSetAndReturnWithException() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 // Given
 doThrow(new IOException("Test exception")).when(testField).set(testInstance, testValue);

 // When
 // Then
 assertThrows(IOException.class, () -> fieldProperty.setAndReturn(testInstanceCopy, testValue));
 }

 @Test
 public void testFixAccess() {
 // Given
 DeserializationConfig config = mock(DeserializationConfig.class);

 // When
 fieldProperty.fixAccess(config);

 // Then
 assertTrue(ClassUtil.isAccessible(testField, testInstance.getClass()));
 }

 @Test
 public void testGetAnnotation() {
 // Given
 when(annotatedField.getAnnotation(Annotation.class)).thenReturn(mock(Annotation.class));

 // When
 Annotation actualAnnotation = fieldProperty.getAnnotation(Annotation.class);

 // Then
 assertEquals(mock(Annotation.class), actualAnnotation);
 }

 @Test
 public void testGetMember() {
 // Given
 when(annotatedField.getMember()).thenReturn(mock(AnnotatedMember.class));

 // When
 AnnotatedMember actualMember = fieldProperty.getMember();

 // Then
 assertEquals(mock(AnnotatedMember.class), actualMember);
 }
}