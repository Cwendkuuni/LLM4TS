package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonTypeDeserializer;
import com.fasterxml.jackson.databind.Introspect;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MethodPropertyTest {

 private MethodProperty property;
 private AnnotatedMethod annotatedMethod;
 private Annotation annotation;
 private JavaType javaType;
 private TypeDeserializer typeDeserializer;
 private Introspect introspect;
 private NullValueProvider nullValueProvider;
 private DeserializationContext context;
 private JsonParser parser;
 private JsonDeserializer deserializer;
 private JsonTypeDeserializer typeDeserializerSpy;

 @Before
 public void setup() {
 annotation = Mockito.mock(Annotation.class);
 annotatedMethod = Mockito.spy(new MethodStub());
 javaType = Mockito.mock(JavaType.class);
 typeDeserializer = Mockito.mock(TypeDeserializer.class);
 introspect = Mockito.mock(Introspect.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 context = Mockito.mock(DeserializationContext.class);
 parser = Mockito.mock(JsonParser.class);
 deserializer = Mockito.mock(JsonDeserializer.class);
 typeDeserializerSpy = Mockito.spy(new JsonTypeDeserializer() {
 @Override
 public JsonDeserializer<?> getNullValueDeserializer(DeserializationContext ctxt, TypeDeserializer nullValueTypeDeserializer) {
 return nullValueProvider;
 }
 });

 property = new MethodProperty(new BeanPropertyDefinition(null, null, null, null, null, null), javaType, typeDeserializer, Annotations.empty(), annotatedMethod);
 }

 @Test
 public void testDeserializeAndSet() throws IOException, JsonProcessingException {
 Object instance = Mockito.mock(Object.class);
 Object value = "testValue";

 when(annotatedMethod.getAnnotated()).thenReturn(annotation);
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 Object result = args[1];
 property.set(instance, result);
 return null;
 }
 }).when(annotatedMethod).invoke(instance, any());

 when(parser.getValueAsString()).thenReturn("testValue");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getCurrentName()).thenReturn("testPropertyName");

 property.deserializeAndSet(parser, context, instance);
 assertEquals("testValue", property.getName().get(instance));

 property = new MethodProperty(property, deserializer, nullValueProvider);
 property.deserializeAndSet(parser, context, instance);
 assertEquals(null, property.getName().get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
 Object instance = Mockito.mock(Object.class);
 Object value = "testValue";
 Object result = new Object() {
 @Override
 public String toString() {
 return "testResult";
 }
 };

 when(parser.getValueAsString()).thenReturn("testValue");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getCurrentName()).thenReturn("testPropertyName");
 when(annotatedMethod.getAnnotated()).thenReturn(annotation);
 doReturn(result).when(annotatedMethod).invoke(instance, any());

 Object returned = property.deserializeSetAndReturn(parser, context, instance);
 assertEquals("testResult", returned);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = Mockito.mock(Object.class);
 Object value = "testValue";

 when(annotatedMethod.getAnnotated()).thenReturn(annotation);
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 property.set(instance, args[1]);
 return null;
 }
 }).when(annotatedMethod).invoke(instance, any());

 property.set(instance, value);
 assertEquals("testValue", property.getName().get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = Mockito.mock(Object.class);
 Object value = "testValue";
 Object result = new Object() {
 @Override
 public String toString() {
 return "testResult";
 }
 };

 when(annotatedMethod.getAnnotated()).thenReturn(annotation);
 doReturn(result).when(annotatedMethod).invoke(instance, any());

 Object returned = property.setAndReturn(instance, value);
 assertEquals("testResult", returned);
 }

 @Test
 public void testWithName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 MethodProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer deserializerSpy = Mockito.spy(deserializer);
 MethodProperty newProperty = property.withValueDeserializer(deserializerSpy);
 assertEquals(deserializerSpy, newProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProviderSpy = Mockito.spy(nullValueProvider);
 MethodProperty newProperty = property.withNullProvider(nullValueProviderSpy);
 assertEquals(nullValueProviderSpy, newProperty._nullProvider);
 }

 @Test
 public void testFixAccess() {
 property.fixAccess(context);
 verify(annotatedMethod).fixAccess(context.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testGetAnnotation() {
 assertEquals(annotation, property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(annotatedMethod, property.getMember());
 }

 @Test
 public void testInvokeSetter() throws Exception {
 Object instance = Mockito.mock(Object.class);
 Object value = "testValue";

 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 property.set(instance, args[1]);
 return null;
 }
 }).when(annotatedMethod).invoke(instance, any());

 property.set(instance, value);
 verify(annotatedMethod).invoke(instance, value);
 }

 @Test
 public void testInvokeWithTypeDeserializer() throws JsonProcessingException {
 Object instance = Mockito.mock(Object.class);
 Object value = new Object() {
 @Override
 public String toString() {
 return "testValue";
 }
 };
 Object expectedValue = new Object() {
 @Override
 public String toString() {
 return "expectedValue";
 }
 };
 TypeDeserializer typeDeserializerMock = Mockito.mock(TypeDeserializer.class);
 when(typeDeserializerMock.deserializeWithType(parser, context, Mockito.any()))
 .thenReturn(expectedValue);
 when(typeDeserializer.getNullValueDeserializer(context, Mockito.any()))
 .thenReturn(nullValueProvider);

 when(annotatedMethod.getAnnotated()).thenReturn(annotation);
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 property.set(instance, args[1]);
 return null;
 }
 }).when(annotatedMethod).invoke(instance, any());

 Object result = property.deserializeAndSet(parser, context, instance);
 assertEquals(expectedValue, result);
 }
}

class MethodStub implements AnnotatedMethod {
 @Override
 public Annotation[] getParameterAnnotations(int i) {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation getAnnotation(Class<?> annotationType) {
 return null;
 }

 @Override
 public Annotation[] getAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotations(Class<?> annotationType) {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getAnnotationsByType(Class<?> annotationType) {
 return new Annotation[0];
 }

 @Override
 public Annotation getAnnotation(String annotationName) {
 return null;
 }

 @Override
 public Annotation[] getAnnotationsByType(String annotationName) {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotationsByType(Class<?> annotationType) {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotationsByType(String annotationName) {
 return new Annotation[0];
 }

 @Override
 public Class<?>[] getParameterTypes() {
 return new Class[0];
 }

 @Override
 public int getModifiers() {
 return Modifier.PUBLIC;
 }

 @Override
 public int getParameterAnnotationsCount() {
 return 0;
 }

 @Override
 public AnnotatedElement getDeclaringElement() {
 return null;
 }

 @Override
 public String getName() {
 return "testMethod";
 }

 @Override
 public AnnotatedMember getAnnotationMember() {
 return null;
 }

 @Override
 public AnnotatedMethod getAnnotated() {
 return this;
 }

 @Override
 public void addAnnotation(Annotation annotation) {
 // Do nothing
 }

 @Override
 public void addAnnotation(String annotationName, Object value) {
 // Do nothing
 }

 @Override
 public void addAnnotations(Annotation[] annotations) {
 // Do nothing
 }

 @Override
 public void addAnnotations(String annotationName, Object value) {
 // Do nothing
 }

 @Override
 public boolean isAnnotationPresent(Class<?> annotationType) {
 return false;
 }

 @Override
 public boolean isAnnotationPresent(String annotationName) {
 return false;
 }

 @Override
 public boolean isSynthetic() {
 return false;
 }
}