package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FieldPropertyTest {

 private static final Object TEST_INSTANCE = new Object();
 private static final AnnotatedField TEST_ANNOTATED_FIELD = Mockito.mock(AnnotatedField.class);
 private static final Field TEST_FIELD = Mockito.mock(Field.class);
 private static final TypeDeserializer TEST_TYPE_DESERIALIZER = Mockito.mock(TypeDeserializer.class);
 private static final JsonDeserializer<?> TEST_VALUE_DESERIALIZER = Mockito.mock(JsonDeserializer.class);
 private static final NullValueProvider TEST_NULL_VALUE_PROVIDER = Mockito.mock(NullValueProvider.class);

 @Test
 public void testConstructor() {
 FieldProperty fieldProperty = new FieldProperty(
 Mockito.mock(BeanPropertyDefinition.class),
 Mockito.mock(JavaType.class),
 TEST_TYPE_DESERIALIZER,
 Mockito.mock(Annotations.class),
 TEST_ANNOTATED_FIELD);
 assertEquals(TEST_ANNOTATED_FIELD, fieldProperty.getMember());
 assertEquals(TEST_FIELD, fieldProperty.getBackingField());
 assertFalse(fieldProperty.isWritable());
 }

 @Test
 public void testWithName() {
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, "newName");
 assertEquals("newName", fieldProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_VALUE_DESERIALIZER);
 assertEquals(TEST_VALUE_DESERIALIZER, fieldProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_NULL_VALUE_PROVIDER);
 assertEquals(TEST_NULL_VALUE_PROVIDER, fieldProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY);
 fieldProperty.fixAccess(config);
 assertEquals(true, ClassUtil.isAccessible(TEST_FIELD, TEST_INSTANCE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullAnnotatedField() {
 FieldProperty fieldProperty = new FieldProperty(null, TEST_FIELD_PROPERTY, TEST_INSTANCE);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetAnnotation() {
 Annotation testAnnotation = Mockito.mock(Annotation.class);
 when(TEST_ANNOTATED_FIELD.getAnnotation(Mockito.any(Annotation.class))).thenReturn(testAnnotation);
 FieldProperty fieldProperty = new FieldProperty(TEST_ANNOTATED_FIELD, TEST_FIELD_PROPERTY, TEST_INSTANCE);
 assertEquals(testAnnotation, fieldProperty.getAnnotation(Mockito.any(Annotation.class)));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetValueWithPrivateField() throws IllegalAccessException {
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_INSTANCE);
 fieldProperty.setValue(TEST_INSTANCE, "value");
 fail("Should throw IllegalAccessException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetValueWithFinalField() throws IllegalAccessException {
 Field finalField = Mockito.mock(Field.class);
 Mockito.doThrow(new IllegalAccessException("Illegal access")).when(finalField).set(TEST_INSTANCE, "value");
 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, finalField);
 fieldProperty.setValue(TEST_INSTANCE, "value");
 fail("Should throw IllegalAccessException");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object value = "value";
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn("value");
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(TEST_VALUE_DESERIALIZER.deserialize(parser, context)).thenReturn(value);
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(null);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 fieldProperty.deserializeAndSet(parser, context, TEST_INSTANCE);
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("fieldName").get(TEST_INSTANCE));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object value = "value";
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn("value");
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(TEST_VALUE_DESERIALIZER.deserialize(parser, context)).thenReturn(value);
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(null);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 Object result = fieldProperty.deserializeSetAndReturn(parser, context, TEST_INSTANCE);
 assertEquals(value, result);
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("fieldName").get(TEST_INSTANCE));
 }

 @Test
 public void testSetValue() throws IOException, IllegalAccessException {
 Object value = new Object();
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn("value");
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(TEST_VALUE_DESERIALIZER.deserialize(parser, context)).thenReturn(value);
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(null);
 when(TEST_FIELD_PROPERTY.getValueDeserializer()).thenReturn(TEST_VALUE_DESERIALIZER);
 when(TEST_FIELD_PROPERTY.getNullProvider()).thenReturn(TEST_NULL_VALUE_PROVIDER);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY);
 fieldProperty.setValue(TEST_INSTANCE, value);
 assertEquals(value, TEST_INSTANCE.getClass().getDeclaredField("fieldName").get(TEST_INSTANCE));
 }

 @Test
 public void testSetValueWithNullValue() throws IOException, IllegalAccessException {
 Object nullValue = null;
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn(null);
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(nullValue);
 when(TEST_FIELD_PROPERTY.getValueDeserializer()).thenReturn(TEST_VALUE_DESERIALIZER);
 when(TEST_FIELD_PROPERTY.getNullProvider()).thenReturn(TEST_NULL_VALUE_PROVIDER);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY);
 fieldProperty.setValue(TEST_INSTANCE, null);
 assertNull(TEST_INSTANCE.getClass().getDeclaredField("fieldName").get(TEST_INSTANCE));
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithValueDeserializerFailure() throws IOException, IllegalAccessException {
 Object value = "value";
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn("value");
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(TEST_VALUE_DESERIALIZER.deserialize(parser, context)).thenThrow(new IOException("Value deserialization failed"));
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(null);
 when(TEST_FIELD_PROPERTY.getValueDeserializer()).thenReturn(TEST_VALUE_DESERIALIZER);
 when(TEST_FIELD_PROPERTY.getNullProvider()).thenReturn(TEST_NULL_VALUE_PROVIDER);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 fieldProperty.deserializeAndSet(parser, context, TEST_INSTANCE);
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithValueDeserializerFailure() throws IOException, IllegalAccessException {
 Object value = "value";
 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 when(parser.getValueAsString()).thenReturn("value");
 when(parser.getCurrentName()).thenReturn("fieldName");
 when(parser.hasToken(Mockito.anyInt())).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(TEST_VALUE_DESERIALIZER.deserialize(parser, context)).thenThrow(new IOException("Value deserialization failed"));
 when(TEST_NULL_VALUE_PROVIDER.getNullValue(context)).thenReturn(null);
 when(TEST_FIELD_PROPERTY.getValueDeserializer()).thenReturn(TEST_VALUE_DESERIALIZER);
 when(TEST_FIELD_PROPERTY.getNullProvider()).thenReturn(TEST_NULL_VALUE_PROVIDER);

 FieldProperty fieldProperty = new FieldProperty(TEST_FIELD_PROPERTY, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 Object result = fieldProperty.deserializeSetAndReturn(parser, context, TEST_INSTANCE);
 fail("Should throw IOException");
 }
}