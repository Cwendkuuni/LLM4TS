package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class InnerClassPropertyTest {

 private InnerClassProperty innerClassProperty;
 private SettableBeanProperty delegate;
 private Object bean;
 private AnnotatedConstructor constructor;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;

 @Before
 public void setup() throws Exception {
 Class<?> innerClass = Object.class.getDeclaredClass().getDeclaredInnerClass("Inner");
 Constructor<?> innerClassConstructor = innerClass.getDeclaredConstructor();
 innerClassConstructor.setAccessible(true);
 delegate = new SettableBeanProperty(new SettableBeanProperty(new Object(), new SettableBeanProperty.StdGetter((Object o) -> null)), null);
 innerClassProperty = new InnerClassProperty(delegate, innerClassConstructor);
 bean = new Object();
 constructor = new AnnotatedConstructor(new Annotation[]{}, innerClassConstructor);
 jsonParser = new TestJsonFactory().createParser("{}");
 deserializationContext = new TestJsonFactory().createContext();
 }

 @After
 public void teardown() throws Exception {
 jsonParser.close();
 }

 @Test
 public void testInnerClassPropertyConstructor() {
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
 assertNotNull(innerClassProperty);
 assertSame(delegate, innerClassProperty._delegate);
 assertSame(constructor, innerClassProperty._creator);
 }

 @Test
 public void testInnerClassPropertyWithValueDeserializer() throws IOException {
 JsonDeserializer<Object> valueDeserializer = new BaseJsonDeserializer() {
 @Override
 public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsObject();
 }
 };
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor, valueDeserializer);
 assertNotNull(innerClassProperty);
 assertSame(delegate, innerClassProperty._delegate);
 assertSame(constructor, innerClassProperty._creator);
 assertSame(valueDeserializer, innerClassProperty._valueDeserializer);
 }

 @Test
 public void testInnerClassPropertyWithJdkSerialization() throws IOException {
 InnerClassProperty innerClassProperty = new InnerClassProperty(innerClassProperty, constructor);
 assertNotNull(innerClassProperty);
 assertSame(innerClassProperty, innerClassProperty.readResolve());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String json = "{\"class\":\"java.lang.String\", \"value\":\"test\"}";
 jsonParser.setValueFactory(new SimpleValueFactory(String.class));
 jsonParser.setValue("class", "java.lang.String");
 jsonParser.setValue("value", "test");
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
 assertEquals("test", bean.getClass().getDeclaredField("inner").get(bean));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructorException() throws IOException {
 constructor = null;
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccessException() throws IOException {
 Class<?> innerClass = Object.class.getDeclaredClass();
 Constructor<?> innerClassConstructor = innerClass.getDeclaredConstructor();
 innerClassConstructor.setAccessible(false);
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, innerClassConstructor);
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
 }

 @Test(expected = InstantiationException.class)
 public void testInstantiationException() throws IOException {
 Class<?> innerClass = Object.class.getDeclaredClass();
 Constructor<?> innerClassConstructor = innerClass.getDeclaredConstructor("String");
 innerClassConstructor.setAccessible(true);
 when(innerClassConstructor.newInstance(any()))
 .thenThrow(new InstantiationException("Failed to instantiate class " + innerClass.getName()));
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, innerClassConstructor);
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonProcessingException() throws IOException {
 JsonDeserializer<Object> valueDeserializer = new BaseJsonDeserializer() {
 @Override
 public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 throw new JsonProcessingException("Test JsonProcessingException");
 }
 };
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor, valueDeserializer);
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
 }
}