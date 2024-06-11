package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SetterlessPropertyTest {

 private SettableBeanProperty property;
 private AnnotatedMethod annotatedMethod;
 private Object instance;

 @Before
 public void setup() throws Exception {
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 JavaType javaType = Mockito.mock(JavaType.class);
 TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
 Annotations contextAnnotations = Mockito.mock(Annotations.class);
 TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
 NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
 Method getter = Mockito.mock(Method.class);
 PropertyName propertyName = new PropertyName("testProperty");
 annotatedMethod = Mockito.mock(AnnotatedMethod.class);
 instance = Mockito.mock(Object.class);
 when(annotatedMember.getName()).thenReturn(propertyName);
 when(annotatedMethod.getAnnotations()).thenReturn(contextAnnotations);
 when(annotatedMethod.getDeclaringClass()).thenReturn(Object.class);
 when(annotatedMethod.getParameterTypes()).thenReturn(new Class[0]);
 when(annotatedMethod.getAnnotated()).thenReturn(annotatedMember);
 when(annotatedMethod.getMethod()).thenReturn(getter);
 property = new SetterlessProperty(
 new BeanPropertyDefinition(propertyName, javaType, typeDeserializer, contextAnnotations),
 javaType, typeDeserializer, contextAnnotations, annotatedMethod);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<Annotation> annotationType = Mockito.mock(Class.class);
 assertEquals(annotationType.newInstance(), property.getAnnotation(annotationType));
 }

 @Test
 public void testGetMember() throws Exception {
 assertEquals(annotatedMethod, property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
 Object expectedValue = new Object();
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn("test");
 when(valueTypeDeserializer.deserialize(jsonParser, deserializationContext, null)).thenReturn(expectedValue);
 property.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNotNull(property.getValueId(instance));
 assertEquals(expectedValue, property.getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
 Object expectedValue = new Object();
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn("test");
 when(valueTypeDeserializer.deserialize(jsonParser, deserializationContext, null)).thenReturn(expectedValue);
 Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 property.set(instance, new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 property.setAndReturn(instance, new Object());
 }
}