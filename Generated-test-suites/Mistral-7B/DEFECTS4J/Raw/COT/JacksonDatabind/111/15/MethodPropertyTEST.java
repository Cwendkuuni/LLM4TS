package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private Object objectToBePopulated;
 private Object nullValue;
 private JsonParser jsonParser;
 private DeserializationContext context;
 private ObjectInstanceContext instanceContext;
 private JsonDeserializer<?> valueDeserializer;
 private NullValueProvider nullValueProvider;
 private TypeDeserializer typeDeserializer;
 private AnnotatedMethod annotatedMethod;
 private Object instance;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 Object constructorArgument = new Object() {};
 instance = constructorArgument.getClass().newInstance();
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 objectToBePopulated = new Object();
 nullValue = null;
 jsonParser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 instanceContext = mock(ObjectInstanceContext.class);
 valueDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 annotatedMethod = mock(AnnotatedMethod.class);
 }

 @Test
 public void testMethodPropertyConstructor() {
 MethodProperty methodPropertyActual = new MethodProperty(null, null, null, null, annotatedMethod);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testMethodPropertyWithName() throws InstantiationException, IllegalAccessException {
 MethodProperty methodPropertyActual = methodProperty.withName(new PropertyName("testProperty"));
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testMethodPropertyWithValueDeserializer() {
 MethodProperty methodPropertyActual = methodProperty.withValueDeserializer(valueDeserializer);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testMethodPropertyWithNullProvider() {
 MethodProperty methodPropertyActual = methodProperty.withNullProvider(nullValueProvider);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testFixAccess() throws IOException {
 methodProperty.fixAccess(context);
 assertEquals("Expected _annotated to have access set", Modifier.PUBLIC, _privatelyAccessAnnotatedMethod(annotatedMethod).getModifiers());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMethodPropertyWithNullAnnotatedMethod() {
 methodProperty = new MethodProperty(null, null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMethodPropertyWithNullSetter() {
 MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
 methodProperty = new MethodProperty(methodProperty, null, nullValueProvider);
 methodProperty = new MethodProperty(methodProperty, valueDeserializer, nullValueProvider);
 methodProperty = new MethodProperty(methodProperty, null);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testValue = "testValue";
 Object testInstance = new Object();
 when(jsonParser.getValueAsString()).thenReturn(testValue);
 methodProperty.deserializeAndSet(jsonParser, context, testInstance);
 verify(instance, times(1)).invoke(testInstance, testValue);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String testValue = "testValue";
 Object testInstance = new Object();
 when(jsonParser.getValueAsString()).thenReturn(testValue);
 when(instance.getClass()).thenReturn(Object.class);
 when(_setter.invoke(instance, testValue)).thenReturn(new Object());
 Object result = methodProperty.deserializeSetAndReturn(jsonParser, context, testInstance);
 assertEquals("Expected instance to be returned", testInstance, result);
 }

 @Test
 public void testSet() throws IOException, InstantiationException, IllegalAccessException {
 String testValue = "testValue";
 methodProperty.set(instance, testValue);
 assertEquals("Expected testValue to be set", testValue, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException, InstantiationException, IllegalAccessException {
 String testValue = "testValue";
 when(instance.getClass()).thenReturn(Object.class);
 when(_setter.invoke(instance, testValue)).thenReturn(new Object());
 Object result = methodProperty.setAndReturn(instance, testValue);
 assertEquals("Expected instance to be returned", testInstance, result);
 }

 @Test
 public void testReadResolve() throws InstantiationException, IllegalAccessException {
 MethodProperty methodPropertyResolved = methodProperty.readResolve();
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyResolved);
 }

 @Test
 public void testWithValueDeserializerWhenValueDeserializerIsNull() {
 MethodProperty methodPropertyActual = methodProperty.withValueDeserializer(null);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testWithNullProviderWhenNullProviderIsNull() {
 MethodProperty methodPropertyActual = methodProperty.withNullProvider(null);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test
 public void testWithTypeDeserializerWhenTypeDeserializerIsNull() {
 MethodProperty methodPropertyActual = methodProperty.withTypeDeserializer(null);
 assertEquals("Expected instances to be equal", methodProperty, methodPropertyActual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithTypeDeserializerWhenValueDeserializerAndTypeDeserializerAreBothNull() {
 MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = new MethodProperty(methodProperty, nullValueProvider);
 methodProperty = methodProperty.withValueDeserializer(null);
 methodProperty = methodProperty.withTypeDeserializer(null);
 }

 @Test
 public void testInvokeSetterWithNullInstance() throws IllegalAccessException, InvocationTargetException {
 methodProperty.set(null, null);
 // No exception expected
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvokeSetterWithNullValue() throws IllegalAccessException, InvocationTargetException {
 methodProperty.set(instance, null);
 // No exception expected
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvokeSetterWithNonMatchingMethod() throws IllegalAccessException, InvocationTargetException {
 MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = new MethodProperty(methodProperty, nullValueProvider);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withTypeDeserializer(typeDeserializer);
 methodProperty._setter = new Method() {};
 try {
 methodProperty.set(instance, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullJsonParser() throws IOException {
 methodProperty.deserializeAndSet(null, context, instance);
 // No exception expected
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullDeserializationContext() throws IOException {
 methodProperty.deserializeAndSet(jsonParser, null, instance);
 // No exception expected
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullInstance() throws IOException {
 methodProperty.deserializeAndSet(jsonParser, context, null);
 // No exception expected
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeAndSetWithNullValueDeserializer() throws IOException {
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 methodProperty = methodProperty.withValueDeserializer(null);
 // Expected NullPointerException to be thrown
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeAndSetWithNullTypeDeserializer() throws IOException {
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 methodProperty = methodProperty.withTypeDeserializer(null);
 // Expected NullPointerException to be thrown
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNullJsonParser() throws IOException {
 methodProperty.deserializeSetAndReturn(null, context, instance);
 // No exception expected
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNullDeserializationContext() throws IOException {
 methodProperty.deserializeSetAndReturn(jsonParser, null, instance);
 // No exception expected
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNullInstance() throws IOException {
 methodProperty.deserializeSetAndReturn(jsonParser, context, null);
 // No exception expected
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeSetAndReturnWithNullValueDeserializer() throws IOException {
 methodProperty.deserializeSetAndReturn(jsonParser, context, instance);
 methodProperty = methodProperty.withValueDeserializer(null);
 // Expected NullPointerException to be thrown
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeSetAndReturnWithNullTypeDeserializer() throws IOException {
 methodProperty.deserializeSetAndReturn(jsonParser, context, instance);
 methodProperty = methodProperty.withTypeDeserializer(null);
 // Expected NullPointerException to be thrown
 }

 @Test(expected = IllegalAccessException.class)
 public void testInvokeSetterWithPrivateSetter() throws IllegalAccessException, InvocationTargetException {
 methodProperty._setter = new Method() {
 private static final long serialVersionUID = 1;
 private AccessibleObject lock = new Object();
 private boolean accessible;

 @Override
 public Object invoke(Object obj, Object... args) throws IllegalAccessException, InvocationTargetException {
 synchronized (lock) {
 if (!accessible) {
 try {
 setAccessible(true);
 } catch (SecurityException e) {
 throw new RuntimeException(e);
 }
 accessible = true;
 }
 return super.invoke(obj, args);
 }
 };

 @Override
 public boolean isAccessible() {
 return accessible;
 }

 @Override
 public void setAccessible(boolean accessible) {
 this.accessible = accessible;
 }
 };
 methodProperty.set(instance, null);
 // Expected IllegalAccessException to be thrown
 }

 @Test
 public void testDeserializeNullValueWithSkipNullsEnabled() throws IOException {
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withNullProvider(nullValueProvider);
 methodProperty._skipNulls = true;
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verifyZeroInteractions(instance);
 }

 @Test
 public void testDeserializeNullValueWithSkipNullsDisabled() throws IOException {
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withNullProvider(nullValueProvider);
 methodProperty._skipNulls = false;
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verify(instance, times(1)).invoke(instance, null);
 }

 @Test
 public void testDeserializeNullValueWithNullProviderReturnsNullValue() throws IOException {
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withNullProvider(nullValueProvider);
 methodProperty._skipNulls = false;
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verify(instance, times(1)).invoke(instance, null);
 assertEquals("Expected null value to be set", null, instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testDeserializeNullValueWithNullProviderReturnsDefaultValue() throws IOException {
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withNullProvider(new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return new Object();
 }
 });
 methodProperty._skipNulls = false;
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 assertEquals("Expected default value to be set", new Object(), instance.getClass().getDeclaredField("testProperty").get(instance));
 }

 @Test
 public void testDeserializeNullValueWithNullProviderReturnsDefaultValueWithSkipNullsEnabled() throws IOException {
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 methodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 methodProperty = methodProperty.withNullProvider(new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return new Object();
 }
 });
 methodProperty._skipNulls = true;
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verifyZeroInteractions(instance);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException {
 Object testValue = new Object();
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(_valueTypeDeserializer.deserialize(jsonParser, context, Object.class)).thenReturn(testValue);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 verify(instance, times(1)).invoke(instance, testValue);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTypeDeserializerWhenValueDeserializerReturnsNull() throws IOException {
 Object testValue = null;
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn(testValue);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 // Expected JsonProcessingException to be thrown
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTypeDeserializerWhenValueDeserializerReturnsAnotherType() throws IOException {
 Object testValue = new Object();
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn(new Object());
 when(_valueTypeDeserializer.deserialize(jsonParser, context, Object.class)).thenReturn(testValue);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 // Expected JsonProcessingException to be thrown
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTypeDeserializerWhenTypeDeserializerReturnsNull() throws IOException {
 Object testValue = new Object();
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn(testValue);
 when(_valueTypeDeserializer.deserialize(jsonParser, context, Object.class)).thenReturn(null);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 // Expected JsonProcessingException to be thrown
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTypeDeserializerWhenTypeDeserializerReturnsAnotherType() throws IOException {
 Object testValue = new Object();
 Object testValueType = new Object();
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(_valueDeserializer.deserialize(jsonParser, context)).thenReturn(testValue);
 when(_valueTypeDeserializer.deserialize(jsonParser, context, Object.class)).thenReturn(testValueType);
 when(_valueTypeDeserializer.deserialize(jsonParser, context, testValueType.getClass())).thenReturn(testValue);
 methodProperty.deserializeAndSet(jsonParser, context, instance);
 // Expected JsonProcessingException to be thrown
 }

 private Method _privatelyAccessAnnotatedMethod(AnnotatedMember member) {
 try {
 return member.getAnnotated().getMethod();
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }
}