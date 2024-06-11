package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class MethodPropertyTest {

 private static final Class<?> CLASS_UNDER_TEST = MethodProperty.class;
 private static final Object TEST_INSTANCE = new Object();
 private static final String TEST_PROPERTY_NAME = "testProperty";
 private static final String TEST_SETTER_NAME = "setTestProperty";
 private static final Method TEST_SETTER_METHOD;
 private static final Class<?> TEST_VALUE_TYPE = String.class;
 private static final JsonDeserializer<String> TEST_VALUE_DESERIALIZER = mock(JsonDeserializer.class);
 private static final NullValueProvider TEST_NULL_VALUE_PROVIDER = mock(NullValueProvider.class);
 private static final TypeDeserializer TEST_VALUE_TYPE_DESERIALIZER = mock(TypeDeserializer.class);
 private static final BeanPropertyDefinition TEST_PROPERTY_DEFINITION = mock(BeanPropertyDefinition.class);
 private static final JavaType TEST_TYPE = mock(JavaType.class);
 private static final Annotations TEST_CONTEXT_ANNOTATIONS = mock(Annotations.class);
 private static final AnnotatedMethod TEST_ANNOTATED_METHOD;
 private static final MethodProperty TEST_METHOD_PROPERTY;
 private static final MethodProperty TEST_COPY_CONSTRUCTOR_METHOD_PROPERTY;
 private static final PropertyName TEST_PROPERTY_NAME_WITH_NEW_VALUE;

 static {
 try {
 TEST_SETTER_METHOD = ClassUtil.findMethod(CLASS_UNDER_TEST, TEST_SETTER_NAME, TEST_VALUE_TYPE);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException("Could not find setter method", e);
 }

 TEST_ANNOTATED_METHOD = mock(AnnotatedMethod.class);
 when(TEST_ANNOTATED_METHOD.getAnnotated()).thenReturn(TEST_SETTER_METHOD);

 try {
 TEST_METHOD_PROPERTY = new MethodProperty(TEST_PROPERTY_DEFINITION, TEST_TYPE, TEST_VALUE_TYPE_DESERIALIZER, TEST_CONTEXT_ANNOTATIONS, TEST_ANNOTATED_METHOD);
 } catch (IllegalAccessException | InstantiationException e) {
 throw new RuntimeException("Could not create MethodProperty instance", e);
 }

 TEST_COPY_CONSTRUCTOR_METHOD_PROPERTY = new MethodProperty(TEST_METHOD_PROPERTY, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 TEST_PROPERTY_NAME_WITH_NEW_VALUE = new PropertyName("newTestProperty");
}

 @org.junit.Before
 public void setUp() throws Exception {
 // Do nothing
 }

 @org.junit.Test
 public void testNewInstance() {
 MethodProperty instance = new MethodProperty(TEST_PROPERTY_DEFINITION, TEST_TYPE, TEST_VALUE_TYPE_DESERIALIZER, TEST_CONTEXT_ANNOTATIONS, TEST_ANNOTATED_METHOD);
 assertSame(TEST_PROPERTY_DEFINITION, instance.getPropertyDefinition());
 assertSame(TEST_TYPE, instance.getType());
 assertSame(TEST_VALUE_TYPE_DESERIALIZER, instance.getValueDeserializer());
 assertSame(TEST_CONTEXT_ANNOTATIONS, instance.getContextAnnotationInstances());
 assertSame(TEST_ANNOTATED_METHOD, instance.getMember());
 assertFalse(instance._skipNulls);
 assertNull(instance._setter);
 }

 @org.junit.Test
 public void testCopyConstructor() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 MethodProperty copy = new MethodProperty(instance, TEST_VALUE_DESERIALIZER, TEST_NULL_VALUE_PROVIDER);
 assertSame(instance.getPropertyDefinition(), copy.getPropertyDefinition());
 assertSame(instance.getType(), copy.getType());
 assertSame(instance.getValueDeserializer(), copy.getValueDeserializer());
 assertSame(instance.getContextAnnotationInstances(), copy.getContextAnnotationInstances());
 assertSame(instance.getMember(), copy.getMember());
 assertFalse(copy._skipNulls);
 assertNull(copy._setter);
 }

 @org.junit.Test
 public void testWithName() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 PropertyName newName = TEST_PROPERTY_NAME_WITH_NEW_VALUE;
 MethodProperty result = instance.withName(newName);
 assertSame(instance, result.getPropertyDefinition());
 assertSame(instance.getType(), result.getType());
 assertSame(instance.getValueDeserializer(), result.getValueDeserializer());
 assertSame(instance.getContextAnnotationInstances(), result.getContextAnnotationInstances());
 assertSame(newName, result.getName());
 assertSame(instance.getMember(), result.getMember());
 assertFalse(result._skipNulls);
 assertNull(result._setter);
 }

 @org.junit.Test
 public void testWithValueDeserializer() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 JsonDeserializer<String> deser = new StdConverter<String, String>() {};
 MethodProperty result = instance.withValueDeserializer(deser);
 assertSame(instance, result.getPropertyDefinition());
 assertSame(instance.getType(), result.getType());
 assertSame(deser, result.getValueDeserializer());
 assertSame(instance.getContextAnnotationInstances(), result.getContextAnnotationInstances());
 assertSame(instance.getMember(), result.getMember());
 assertFalse(result._skipNulls);
 assertNull(result._setter);
 }

 @org.junit.Test
 public void testWithNullProvider() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 NullValueProvider nva = mock(NullValueProvider.class);
 MethodProperty result = instance.withNullProvider(nva);
 assertSame(instance, result.getPropertyDefinition());
 assertSame(instance.getType(), result.getType());
 assertSame(instance.getValueDeserializer(), result.getValueDeserializer());
 assertSame(instance.getContextAnnotationInstances(), result.getContextAnnotationInstances());
 assertSame(instance.getMember(), result.getMember());
 assertSame(nva, result._nullProvider);
 assertFalse(result._skipNulls);
 assertNull(result._setter);
 }

 @org.junit.Test
 public void testFixAccess() throws IOException {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 DeserializationConfig config = mock(DeserializationConfig.class);
 instance.fixAccess(config);
 // Do nothing
 }

 @org.junit.Test
 public void testGetAnnotation() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 Class<Annotation> annotationType = mock(Annotation.class);
 Annotation result = instance.getAnnotation(annotationType);
 assertNull(result);
 }

 @org.junit.Test
 public void testGetMember() {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 AnnotatedMember result = instance.getMember();
 assertSame(TEST_ANNOTATED_METHOD, result);
 }

 @org.junit.Test
 public void testDeserializeAndSet() throws IOException {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 Object instanceValue = TEST_INSTANCE;
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(p.getValueAsString()).thenReturn("testValue");
 Object value = "testValue";
 when(TEST_VALUE_DESERIALIZER.deserialize(p, ctxt)).thenReturn(value);
 instance.deserializeAndSet(p, ctxt, instanceValue);
 assertSame(value, TEST_INSTANCE);
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws IOException {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 Object instanceValue = TEST_INSTANCE;
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(p.getValueAsString()).thenReturn("testValue");
 Object value = "testValue";
 when(TEST_VALUE_DESERIALIZER.deserialize(p, ctxt)).thenReturn(value);
 Object result = instance.deserializeSetAndReturn(p, ctxt, instanceValue);
 assertSame(value, result);
 }

 @org.junit.Test
 public void testSet() throws IOException {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 Object instanceValue = TEST_INSTANCE;
 Object value = "testValue";
 instance.set(instanceValue, value);
 assertSame(value, TEST_INSTANCE);
 }

 @org.junit.Test
 public void testSetAndReturn() throws IOException {
 MethodProperty instance = TEST_METHOD_PROPERTY;
 Object instanceValue = TEST_INSTANCE;
 Object value = "testValue";
 Object result = instance.setAndReturn(instanceValue, value);
 assertSame(value, result);
 }

 @org.junit.Test
 public void testReadResolve() throws IOException {
 Object result = TEST_COPY_CONSTRUCTOR_METHOD_PROPERTY.readResolve();
 assertSame(TEST_METHOD_PROPERTY, result);
 }
}