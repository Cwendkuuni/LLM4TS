package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.TypeUtil;

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

 private static final Class<Object> TEST_CLASS = Object.class;

 private Object testInstance;
 private Field testField;
 private AnnotatedField mockedAnnotatedField;
 private BeanPropertyDefinition mockedBeanPropertyDefinition;
 private JsonParser mockedJsonParser;
 private DeserializationContext mockedDeserializationContext;
 private NullValueProvider mockedNullValueProvider;
 private Object mockedNullValue;
 private Object mockedDeserializedValue;
 private Object mockedValueTypeDeserializer;
 private TypeDeserializer mockedTypeDeserializer;
 private JsonDeserializer<Object> mockedJsonDeserializer;
 private TypeFactory mockedTypeFactory;

 @Before
 public void setup() throws Exception {
 testInstance = new Object();
 testField = testInstance.getClass().getField("testField");
 testField.setAccessible(true);
 mockedAnnotatedField = mock(AnnotatedField.class);
 when(mockedAnnotatedField.getMember()).thenReturn(new AnnotatedMember() {
 @Override public AnnotatedMember getMember() { return mockedAnnotatedField; }
 });
 when(mockedAnnotatedField.getAnnotated()).thenReturn(new Object() {});
 mockedBeanPropertyDefinition = mock(BeanPropertyDefinition.class);
 when(mockedBeanPropertyDefinition.getName()).thenReturn("testProperty");
 when(mockedBeanPropertyDefinition.getBeanPropertyDescription()).thenReturn(new BeanPropertyDescription() {
 @Override public String getName() { return "testProperty"; }
 @Override public BeanPropertyDefinition getPropertyDefinition() { return mockedBeanPropertyDefinition; }
 });
 mockedJsonParser = mock(JsonParser.class);
 when(mockedJsonParser.getCurrentName()).thenReturn("testProperty");
 mockedDeserializationContext = mock(DeserializationContext.class);
 mockedNullValueProvider = mock(NullValueProvider.class);
 mockedNullValue = new Object();
 mockedNullValueProvider.getNullValue(mockedDeserializationContext);
 mockedDeserializedValue = new Object();
 mockedValueTypeDeserializer = mock(TypeDeserializer.class);
 mockedTypeDeserializer = mock(TypeDeserializer.class);
 mockedJsonDeserializer = mock(JsonDeserializer.class);
 mockedTypeFactory = mock(TypeFactory.class);
 }

 @Test
 public void testConstructor() throws IOException {
 FieldProperty fieldProperty = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 assertEquals("testProperty", fieldProperty.getName());
 assertEquals(TEST_CLASS, fieldProperty.getType());
 assertNull(fieldProperty.getValueDeserializer());
 assertNull(fieldProperty.getNullProvider());
 assertEquals(mockedAnnotatedField, fieldProperty.getMember());
 }

 @Test
 public void testWithName() throws IOException {
 SettableBeanProperty original = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 SettableBeanProperty withName = original.withName(new PropertyName("newName"));
 assertEquals("newName", withName.getName());
 assertEquals("testProperty", withName.getNameAsString());
 assertEquals(original.getType(), withName.getType());
 assertEquals(original.getValueDeserializer(), withName.getValueDeserializer());
 assertEquals(original.getNullProvider(), withName.getNullProvider());
 assertEquals(original.getMember(), withName.getMember());
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
 FieldProperty original = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 SettableBeanProperty withValueDeserializer = original.withValueDeserializer(jsonDeserializer);
 assertEquals(original.getValueDeserializer(), withValueDeserializer.getValueDeserializer());
 assertEquals(jsonDeserializer, withValueDeserializer.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws IOException {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 FieldProperty original = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 SettableBeanProperty withNullProvider = original.withNullProvider(nullValueProvider);
 assertEquals(original.getNullProvider(), withNullProvider.getNullProvider());
 assertEquals(nullValueProvider, withNullProvider.getNullProvider());
 }

 @Test
 public void testFixAccess() throws IOException {
 DeserializationConfig config = mock(DeserializationConfig.class);
 FieldProperty fieldProperty = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 fieldProperty.fixAccess(config);
 assertEquals(TEST_CLASS.getField("testField"), fieldProperty.getMember().getAnnotated());
 }

 @Test
 public void testGetAnnotation() throws IOException {
 Annotation testAnnotation = mock(Annotation.class);
 AnnotatedField mockedAnnotated = mock(AnnotatedField.class);
 when(mockedAnnotated.getAnnotated()).thenReturn(testAnnotation);
 FieldProperty fieldProperty = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotated);
 assertEquals(testAnnotation, fieldProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(mockedJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockedJsonParser.getValueAsString()).thenReturn("testValue");
 when(mockedJsonDeserializer.deserialize(mockedJsonParser, mockedDeserializationContext)).thenReturn(mockedDeserializedValue);
 when(mockedTypeDeserializer.deserializeTypedObject(mockedJsonParser, mockedDeserializationContext, TypeUtil.constructType(Object.class, TEST_CLASS.getTypeName()))).thenReturn(mockedDeserializedValue);
 when(mockedTypeDeserializer.getValueDeserializer()).thenReturn(mockedJsonDeserializer);
 when(mockedTypeDeserializer.getValueType()).thenReturn(Object.class);
 when(mockedJsonParser.getValueAsInt()).thenReturn(1);

 FieldProperty fieldProperty = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 Object testValue = "testValue";
 fieldProperty.deserializeAndSet(mockedJsonParser, mockedDeserializationContext, testInstance);
 assertEquals(testValue, testInstance.getClass().getField("testField").get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 when(mockedJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockedJsonParser.getValueAsString()).thenReturn("testValue");
 when(mockedJsonDeserializer.deserialize(mockedJsonParser, mockedDeserializationContext)).thenReturn(mockedDeserializedValue);
 when(mockedTypeDeserializer.deserializeTypedObject(mockedJsonParser, mockedDeserializationContext, TypeUtil.constructType(Object.class, TEST_CLASS.getTypeName()))).thenReturn(mockedDeserializedValue);
 when(mockedTypeDeserializer.getValueDeserializer()).thenReturn(mockedJsonDeserializer);
 when(mockedTypeDeserializer.getValueType()).thenReturn(Object.class);
 when(mockedJsonParser.getValueAsInt()).thenReturn(1);

 FieldProperty fieldProperty = new FieldProperty(mockedBeanPropertyDefinition,
 TypeFactory.defaultInstance.constructType(TEST_CLASS),
 mockedTypeDeserializer, Annotations.empty(), mockedAnnotatedField);
 Object testValue = "testValue";
 Object result = fieldProperty.deserializeSetAndReturn(mockedJsonParser, mockedDeserializationContext, testInstance);
 assertEquals(testInstance, result);
 assertEquals(testValue, testInstance.getClass().getField("testField").get(testInstance));
 }

 @Test
 public void testSet() throws IOException {
 Object testValue = new Object();
 fieldProperty.set(testInstance, testValue);
 assertEquals(testValue, testInstance.getClass().getField("testField").get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object testValue = new Object();
 Object result = fieldProperty.setAndReturn(testInstance, testValue);
 assertEquals(testInstance, result);
 assertEquals(testValue, testInstance.getClass().getField("testField").get(testInstance));
 }

 @Test
 public void testReadResolve() throws IOException {
 Object resolvedInstance = fieldProperty.readResolve();
 assertEquals(testInstance.getClass(), resolvedInstance.getClass());
 }
}