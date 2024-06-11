package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.TestBase;

public class SetterlessPropertyTest extends TestBase {

 private SetterlessProperty _property;
 private Object _testInstance;
 private Method _getter;

 @Before
 public void setUp() throws Exception {
 Class<?> testClass = getClass();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(
 "testProperty", String.class, testClass, null);
 JavaType type = mapper().getTypeFactory().constructType(testClass);
 TypeDeserializer typeDeser = null;
 Annotations contextAnnotations = null;
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getTestProperty");
 when(method.isAnnotated(any(Annotation.class))).thenReturn(true);
 _getter = mock(Method.class);
 when(_getter.getName()).thenReturn("setTestProperty");
 when(_getter.invoke(any(Object.class), anyArray())).thenReturn("testValue");
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getAnnotations()).thenReturn(contextAnnotations);
 when(member.getMember()).thenReturn(_getter);
 _property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
 _testInstance = mapper().createInstance(testClass);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testString = "testValue";
 JsonParser p = mapper().getFactory().createParser(mapper().writeValueAsString(testString));
 _property.deserializeAndSet(p, mapper().getDeserializationConfig(), _testInstance);
 assertEquals(testString, _getter.invoke(_testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String testString = "testValue";
 JsonParser p = mapper().getFactory().createParser(mapper().writeValueAsString(testString));
 Object result = _property.deserializeSetAndReturn(p, mapper().getDeserializationConfig(), _testInstance);
 assertEquals(_testInstance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException, InvocationTargetException, IllegalAccessException {
 _property.set(_testInstance, "testValue");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException, InvocationTargetException, IllegalAccessException {
 Object result = _property.setAndReturn(_testInstance, "testValue");
 }

 @Test
 public void testNullValue() throws IOException {
 String testString = "null";
 JsonParser p = mapper().getFactory().createParser(mapper().writeValueAsString(testString));
 _property.deserializeAndSet(p, mapper().getDeserializationConfig(), _testInstance);
 assertNull(_getter.invoke(_testInstance));
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeser = mock(TypeDeserializer.class);
 when(typeDeser.deserializeTypedFromObject(any(Object.class), any(DeserializationContext.class))).thenReturn(new Integer(1));
 JsonParser p = mapper().getFactory().createParser(mapper().writeValueAsString("1"));
 _property = new SetterlessProperty(_property, typeDeserializer, null);
 _property.deserializeAndSet(p, mapper().getDeserializationConfig(), _testInstance);
 assertTrue(_getter.invoke(_testInstance) instanceof Integer);
 }

}