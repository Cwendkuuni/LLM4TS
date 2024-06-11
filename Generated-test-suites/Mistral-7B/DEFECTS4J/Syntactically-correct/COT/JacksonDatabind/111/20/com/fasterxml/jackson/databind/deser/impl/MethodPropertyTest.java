package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class MethodPropertyTest {

 private MethodProperty property;
 private AnnotatedMethod annotatedMethod;
 private AnnotationIntrospector introspector;
 private DeserializationContext context;
 private JsonParser parser;
 private Object instance;

 @Before
 public void setup() throws Exception {
 Class<?> testClass = ClassUtil.findClass(
 "com.example.TestClass", ClassUtil.contextClassLoader());
 property = new MethodProperty(new BeanPropertyDefinition(),
 new JavaType(testClass), new EmptyTypeDeserializer(),
 Annotations.findAnnotation(testClass.getDeclaredMethod("setTest"), JsonProperty.class),
 new AnnotatedMethod(testClass.getDeclaredMethod("setTest"), new Annotation[0]),
 new JsonDeserializer<Object>() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsString();
 }
 }, null);
 annotatedMethod = Mockito.mock(AnnotatedMethod.class);
 introspector = Mockito.mock(AnnotationIntrospector.class);
 context = Mockito.mock(DeserializationContext.class);
 parser = Mockito.mock(JsonParser.class);
 instance = new Object();
 }

 @Test
 public void testConstructor() {
 // Basic constructor test
 MethodProperty methodProperty = new MethodProperty(
 null, null, null,
 Annotations.findAnnotation(annotatedMethod, JsonProperty.class),
 annotatedMethod, null, null, null);
 // Constructor used for JDK Serialization when reading persisted object
 methodProperty = new MethodProperty(methodProperty, annotatedMethod);
 // Constructor used for deserialization
 methodProperty = new MethodProperty(methodProperty, context);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 JsonProperty jsonProperty = new JsonProperty();
 when(annotatedMethod.getAnnotation(JsonProperty.class)).thenReturn(jsonProperty);
 assertNotNull(property.getAnnotation(JsonProperty.class));
 assertEquals(jsonProperty, property.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testSetterInvocationWithNullValue() throws IOException {
 when(parser.getValueAsString()).thenReturn(null);
 assertNull(property.deserializeAndSet(parser, context, instance));
 }

 @Test
 public void testSetterInvocationWithStringValue() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 property.deserializeAndSet(parser, context, instance);
 verify(instance).setTest("test");
 }

 @Test
 public void testSetterInvocationWithIntegerValue() throws IOException {
 Converter<String, Integer> converter = ConverterUtil.findConverter(
 ClassUtil.findClass("java.lang.Integer"), String.class);
 when(parser.getValueAsString()).thenReturn("123");
 when(converter.convert(ArgumentMatchers.any(String.class))).thenReturn(123);
 property.deserializeAndSet(parser, context, instance);
 verify(instance).setTest(123);
 }

 @Test(expected = JsonMappingException.class)
 public void testSetterInvocationWithEmptyStringValue() throws IOException {
 when(parser.getValueAsString()).thenReturn("");
 property.deserializeAndSet(parser, context, instance);
 }

 @Test(expected = IOException.class)
 public void testSetterInvocationWithNullDeserializer() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 property = new MethodProperty(property, new JsonDeserializer[0]);
 property.deserializeAndSet(parser, context, instance);
 }

 @Test(expected = IOException.class)
 public void testSetterInvocationWithNullTypeDeserializer() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(context.findValueDeserializer(ArgumentMatchers.any(Class.class))).thenReturn(null);
 property = new MethodProperty(property, typeDeserializer);
 property.deserializeAndSet(parser, context, instance);
 }

 @Test
 public void testSetterInvocationWithTypeDeserializer() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(context.findValueDeserializer(ArgumentMatchers.eq(String.class))).thenReturn(typeDeserializer);
 property = new MethodProperty(property, typeDeserializer);
 property.deserializeAndSet(parser, context, instance);
 verify(typeDeserializer).deserialize(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.any(DeserializationContext.class));
 }

 @Test
 public void testSetterInvocationWithNullInstance() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 property.deserializeAndSet(parser, context, null);
 }

 @Test(expected = IOException.class)
 public void testSetterInvocationWithInvalidInstanceType() throws IOException {
 when(parser.getValueAsString()).thenReturn("test");
 Class<?> invalidInstanceType = ClassUtil.findClass("java.util.List", ClassUtil.contextClassLoader());
 property = new MethodProperty(property, annotatedMethod);
 property.deserializeAndSet(parser, context, new Object() {});
 }

 @Test
 public void testSetterInvocationWithException() throws IOException, IllegalAccessException, InvocationTargetException {
 when(parser.getValueAsString()).thenThrow(new IOException());
 when(instance.getClass().getMethod("setTest", String.class)).thenThrow(new IllegalAccessException());
 property.deserializeAndSet(parser, context, instance);
 verify(parser).getCurrentName();
 verify(parser).getCurrentElement(BeanProperty.Standard.DESERIALIZED_FROM_STREAM);
 }

 @Test
 public void testSetterInvocationWithNullValueAfterCoercion() throws IOException {
 when(parser.getValueAsString()).thenReturn(null);
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_NULL)).thenReturn("null");
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_STRING)).thenReturn("test");
 property.deserializeAndSet(parser, context, instance);
 assertNull(property.getValue());
 }

 @Test
 public void testSetterInvocationWithNullValueAfterCoercionAndSkipNulls() throws IOException {
 when(parser.getValueAsString()).thenReturn(null);
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_NULL)).thenReturn("null");
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_STRING)).thenReturn("test");
 property = new MethodProperty(property, new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return null;
 }
 });
 property.deserializeAndSet(parser, context, instance);
 assertNull(property.getValue());
 }

 @Test
 public void testSetterInvocationWithNullValueAfterCoercionAndNullValueProvider() throws IOException {
 when(parser.getValueAsString()).thenReturn(null);
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_NULL)).thenReturn("null");
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_STRING)).thenReturn("test");
 when(parser.getValueAsString(parser.getCurrentName(), JsonToken.VALUE_NUMBER_INT)).thenReturn("0");
 property = new MethodProperty(property, null);
 property.deserializeAndSet(parser, context, instance);
 assertNull(property.getValue());
 }

 @Test
 public void testSetterInvocationWithNegativeNumberValue() throws IOException {
 when(parser.getValueAsString()).thenReturn("-123");
 property.deserializeAndSet(parser, context, instance);
 verify(instance).setTest(-123);
 }

 @Test
 public void testSetterInvocationWithLongValue() throws IOException {
 when(parser.getValueAsString()).thenReturn("123456789012345678901234567890");
 property.deserializeAndSet(parser, context, instance);
 verify(instance).setTest(12345678901234567890L);
 }

 @Test
 public void testSetterInvocationWithBigDecimalValue() throws IOException {
 when(parser.getValueAsString()).thenReturn("12345678901234567890.1234567890");
 property = new MethodProperty(property, new JsonDeserializer<BigDecimal>() {
 @Override
 public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new BigDecimal("12345678901234567890.1234567890");
 }
 });
 property.deserializeAndSet(parser, context, instance);
 verify(instance).setTest(new BigDecimal("12345678901234567890.1234567890"));
 }

}