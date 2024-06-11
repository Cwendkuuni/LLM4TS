package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

 private static final Object TEST_INSTANCE = new Object();
 private static final Method TEST_SETTER = TEST_CLASS.getDeclaredMethod("setTest", Object.class);
 private static final NullValueProvider TEST_NULL_VALUE_PROVIDER = new NullValueProvider() {
  @Override
  public Object getNullValue(DeserializationContext ctxt) {
   return null;
  }
 };

 static final Class<MethodProperty> TEST_CLASS = MethodProperty.class;

 @Test
 public void testConstructorWithBeanPropertyDefinition() throws Exception {
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), TypeDeserializer.empty(), Annotations.EMPTY_ARRAY,
          createAnnotatedMethod("testSet", Object.class));
  assertEquals("testSet", methodProperty.getName());
  assertEquals(Object.class, methodProperty.getType());
  assertNull(methodProperty.getValueDeserializer());
  assertNull(methodProperty.getNullProvider());
  assertFalse(methodProperty.isWritable());
  assertFalse(methodProperty.isReadable());
  assertTrue(methodProperty.isSimpleValue());
  assertFalse(methodProperty.getAnnotations().isEmpty());
  assertEquals(MethodProperty.class, methodProperty.getClass());
  assertEquals(BeanPropertyDefinition.class, methodProperty.getBeanPropertyDefinition().getClass());
  assertEquals(JavaType.class, methodProperty.getTypeDeserializer().getClass());
  assertFalse(methodProperty.isSkipNulls());
 }

 @Test
 public void testConstructorWithValueDeserializer() throws Exception {
  JsonDeserializer<?> testDeserializer = mock(JsonDeserializer.class);
  MethodProperty methodProperty = new MethodProperty(new MethodProperty(), testDeserializer, TEST_NULL_VALUE_PROVIDER);
  assertEquals(MethodProperty.class, methodProperty.getClass());
  assertEquals(JavaType.class, methodProperty.getTypeDeserializer().getClass());
  assertEquals(JsonDeserializer.class, methodProperty.getValueDeserializer().getClass());
  assertEquals(TEST_NULL_VALUE_PROVIDER, methodProperty.getNullProvider());
  assertTrue(methodProperty.isWritable());
  assertTrue(methodProperty.isReadable());
  assertTrue(methodProperty.isSimpleValue());
  assertFalse(methodProperty.isSkipNulls());
 }

 @Test
 public void testConstructorWithName() throws Exception {
  PropertyName newName = PropertyName.construct("testNewName");
  MethodProperty methodProperty = new MethodProperty(new MethodProperty(), newName);
  assertEquals("testNewName", methodProperty.getName());
  assertEquals(MethodProperty.class, methodProperty.getClass());
  assertEquals(MethodProperty.class, methodProperty.getBeanPropertyDefinition().getClass());
  assertEquals(JavaType.class, methodProperty.getTypeDeserializer().getClass());
  assertEquals(JsonDeserializer.class, methodProperty.getValueDeserializer().getClass());
  assertEquals(TEST_NULL_VALUE_PROVIDER, methodProperty.getNullProvider());
  assertTrue(methodProperty.isWritable());
  assertTrue(methodProperty.isReadable());
  assertTrue(methodProperty.isSimpleValue());
  assertFalse(methodProperty.isSkipNulls());
 }

 @Test
 public void testConstructorWithMethod() throws Exception {
  MethodProperty methodProperty = new MethodProperty(new MethodProperty(), TEST_SETTER);
  assertEquals("setTest", methodProperty.getName());
  assertEquals(Object.class, methodProperty.getType());
  assertNull(methodProperty.getValueDeserializer());
  assertNull(methodProperty.getNullProvider());
  assertTrue(methodProperty.isWritable());
  assertTrue(methodProperty.isReadable());
  assertTrue(methodProperty.isSimpleValue());
  assertFalse(methodProperty.isSkipNulls());
 }

 @Test
 public void testGetAnnotation() throws Exception {
  Annotation testAnnotation = mock(Annotation.class);
  AnnotatedMethod annotatedMethod = createAnnotatedMethod("testGetAnnotation", void.class);
  annotatedMethod.setAnnotation(testAnnotation.getClass(), testAnnotation);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), TypeDeserializer.empty(), Annotations.EMPTY_ARRAY, annotatedMethod);
  assertEquals(testAnnotation, methodProperty.getAnnotation(testAnnotation.getClass()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotationClass() throws Exception {
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), TypeDeserializer.empty(), Annotations.EMPTY_ARRAY, createAnnotatedMethod("testGetAnnotation", void.class));
  methodProperty.getAnnotation(null);
 }

 @Test
 public void testFixAccess() throws Exception {
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), TypeDeserializer.empty(), Annotations.EMPTY_ARRAY, createAnnotatedMethod("testFixAccess", void.class));
  methodProperty.fixAccess(null);
  assertTrue(TEST_SETTER.isAccessible());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
  Object testValue = new Object();
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
  when(mockParser.nextValue()).thenReturn("testValueString");
  when(mockParser.getCurrentName()).thenReturn("testPropertyName");
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
  when(typeDeserializer.deserialize(eq("testValueString"), eq(context), any(Class.class)))
      .thenReturn(testValue);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), typeDeserializer, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeAndSet(mockParser, context, instance);
  assertEquals(testValue, TEST_INSTANCE);
 }

 @Test
 public void testDeserializeAndSetWithNullValue() throws IOException {
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeAndSet(mockParser, context, instance);
  assertNull(TEST_INSTANCE);
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeAndSetWithNullParser() throws IOException {
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeAndSet(null, context, instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithIOException() throws IOException {
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenThrow(new IOException("Test IOException"));
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeAndSet(mockParser, context, instance);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
  Object testValue = new Object();
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
  when(mockParser.nextValue()).thenReturn("testValueString");
  when(mockParser.getCurrentName()).thenReturn("testPropertyName");
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
  when(typeDeserializer.deserialize(eq("testValueString"), eq(context), any(Class.class)))
      .thenReturn(testValue);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), typeDeserializer, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  assertEquals(instance, methodProperty.deserializeSetAndReturn(mockParser, context, instance));
  assertEquals(testValue, TEST_INSTANCE);
 }

 @Test
 public void testDeserializeSetAndReturnWithNullValue() throws IOException {
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  assertNull(methodProperty.deserializeSetAndReturn(mockParser, context, instance));
  assertNull(TEST_INSTANCE);
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeSetAndReturnWithNullParser() throws IOException {
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeSetAndReturn(null, context, instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithIOException() throws IOException {
  JsonParser mockParser = mock(JsonParser.class);
  when(mockParser.getValueAsToken()).thenThrow(new IOException("Test IOException"));
  Object instance = TEST_INSTANCE;
  DeserializationContext context = mock(DeserializationContext.class);
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.deserializeSetAndReturn(mockParser, context, instance);
 }

 @Test
 public void testSet() throws IOException {
  Object testValue = new Object();
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.set(instance, testValue);
  assertEquals(testValue, TEST_INSTANCE);
 }

 @Test
 public void testSetWithNullValue() throws IOException {
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.set(instance, null);
  assertNull(TEST_INSTANCE);
 }

 @Test(expected = NullPointerException.class)
 public void testSetWithNullInstance() throws IOException {
  Object testValue = new Object();
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.set(null, testValue);
 }

 @Test(expected = IOException.class)
 public void testSetWithIOException() throws IOException {
  Object testValue = new Object();
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSet", Object.class));
  methodProperty.set(instance, testValue);
  verify(instance).setTest(testValue);
 }

 @Test
 public void testSetAndReturn() throws IOException {
  Object testValue = new Object();
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSetAndReturn", Object.class));
  assertEquals(instance, methodProperty.setAndReturn(instance, testValue));
  assertEquals(testValue, TEST_INSTANCE);
 }

 @Test
 public void testSetAndReturnWithNullValue() throws IOException {
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSetAndReturn", Object.class));
  assertNull(methodProperty.setAndReturn(instance, null));
  assertNull(TEST_INSTANCE);
 }

 @Test(expected = NullPointerException.class)
 public void testSetAndReturnWithNullInstance() throws IOException {
  Object testValue = new Object();
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSetAndReturn", Object.class));
  methodProperty.setAndReturn(null, testValue);
 }

 @Test(expected = IOException.class)
 public void testSetAndReturnWithIOException() throws IOException {
  Object testValue = new Object();
  Object instance = TEST_INSTANCE;
  MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(),
          JavaType.of(Object.class), null, Annotations.EMPTY_ARRAY, createAnnotatedMethod("testSetAndReturn", Object.class));
  methodProperty.setAndReturn(instance, testValue);
  verify(instance).setTest(testValue);
 }

 @Test
 public void testReadResolve() throws Exception {
  MethodProperty methodProperty = new MethodProperty(new MethodProperty(), TEST_SETTER);
  assertEquals(METHOD_PROPERTY_CLASS, methodProperty.readResolve());
 }

 private AnnotatedMethod createAnnotatedMethod(String name, Class<?>... parameterTypes) {
  Method method = TEST_CLASS.getDeclaredMethod(name, parameterTypes);
  method.setAccessible(true);
  return AnnotatedMethod.construct(method, new Annotation[0], null);
 }

}