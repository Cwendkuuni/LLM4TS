package com.example.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

 private FieldProperty fieldProperty;
 private AnnotatedField annotatedField;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object CUSTOM_OBJECT;
 private Object valueToSet;

 @Before
 public void setUp() throws Exception {
 annotatedField = mock(AnnotatedField.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 when(jsonParser.getCurrentName()).thenReturn("testField");
 when(jsonParser.getCodec()).thenReturn(mock(ObjectMapper.class));
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("testValue");
 when(deserializationContext.getAnnotationInstance(Object.class)).thenReturn(new Object());
 when(deserializationContext.getTypeFactory()).thenReturn(mock(TypeFactory.class));
 CUSTOM_OBJECT = new Object();
 valueToSet = new Object();
 fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
 }

 @Test
 public void testConstructor() {
 // Default constructor
 FieldProperty defaultFieldProperty = new FieldProperty();
 assertNull(defaultFieldProperty._annotated);
 assertNull(defaultFieldProperty._field);
 }

 @Test
 public void testConstructorWithParameter() throws Exception {
 // Constructor with parameters
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType type = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedField field = mock(AnnotatedField.class);
 fieldProperty = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);
 assertSame(propDef, fieldProperty.getPropertyDefinition());
 assertSame(type, fieldProperty.getType());
 assertSame(typeDeserializer, fieldProperty.getTypeDeserializer());
 assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
 assertSame(field, fieldProperty._annotated);
 assertSame(field, fieldProperty._field);
 }

 @Test
 public void testConstructorWithSource() throws Exception {
 // Constructor with source
 FieldProperty srcFieldProperty = new FieldProperty(fieldProperty);
 assertSame(fieldProperty, srcFieldProperty);
 assertSame(fieldProperty._annotated, srcFieldProperty._annotated);
 assertSame(fieldProperty._field, srcFieldProperty._field);
 }

 @Test
 public void testConstructorWithNewName() throws Exception {
 // Constructor with new name
 PropertyName newName = mock(PropertyName.class);
 FieldProperty newNameFieldProperty = new FieldProperty(fieldProperty, newName);
 assertSame(fieldProperty, newNameFieldProperty);
 assertSame(newName, newNameFieldProperty.getName());
 }

 @Test
 public void testWithName() throws Exception {
 FieldProperty newFieldProperty = fieldProperty.withName(new PropertyName("testField"));
 assertSame(fieldProperty, newFieldProperty);
 assertSame(new PropertyName("testField"), newFieldProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 NullValueProvider nva = mock(NullValueProvider.class);
 FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(deserializer);
 assertSame(fieldProperty, newFieldProperty);
 assertSame(deserializer, newFieldProperty._valueDeserializer);
 assertSame(nva, newFieldProperty._nullProvider);

 FieldProperty newFieldPropertyWithNVP = fieldProperty.withValueDeserializer(deserializer, nva);
 assertSame(fieldProperty, newFieldPropertyWithNVP);
 assertSame(deserializer, newFieldPropertyWithNVP._valueDeserializer);
 assertSame(nva, newFieldPropertyWithNVP._nullProvider);
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider nva = mock(NullValueProvider.class);
 FieldProperty newFieldProperty = fieldProperty.withNullProvider(nva);
 assertSame(fieldProperty, newFieldProperty);
 assertSame(nva, newFieldProperty._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 DeserializationConfig config = mock(DeserializationConfig.class);
 fieldProperty.fixAccess(config);
 ClassUtil.checkAndFixAccess(_field, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 when(annotatedField.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, fieldProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertSame(annotatedField, fieldProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object value = "testValue";
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(value.toString());
 when(typeDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(valueToSet);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertEquals(valueToSet, _field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object value = "testValue";
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(value.toString());
 when(typeDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(valueToSet);
 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertEquals(CUSTOM_OBJECT, result);
 assertEquals(valueToSet, _field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testSet() throws Exception {
 fieldProperty.set(CUSTOM_OBJECT, valueToSet);
 assertEquals(valueToSet, _field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object result = fieldProperty.setAndReturn(CUSTOM_OBJECT, valueToSet);
 assertEquals(CUSTOM_OBJECT, result);
 assertEquals(valueToSet, _field.get(CUSTOM_OBJECT));
 }

 @Test(expected = IOException.class)
 public void testSetWithException() throws Exception {
 when(jsonParser.getText()).thenThrow(new IOException("Test IOException"));
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, CUSTOM_OBJECT);
 }

 @Test
 public void testNullValueHandling() throws Exception {
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertFalse(_field.isAccessible());
 assertNull(_field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testSkippingNulls() throws Exception {
 when(_nullProvider.getNullValue(deserializationContext)).thenReturn(null);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertFalse(_field.isAccessible());
 assertNull(_field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("testValue");
 when(typeDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());
 when(_valueTypeDeserializer).thenReturn(typeDeserializer);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertEquals(new Object(), _field.get(CUSTOM_OBJECT));
 }

 @Test
 public void testCoercionFromString() throws Exception {
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("testValue");
 when(_valueTypeDeserializer).thenReturn(null);
 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, CUSTOM_OBJECT);
 assertEquals(CUSTOM_OBJECT, result);
 assertNull(_field.get(CUSTOM_OBJECT));
 }

}