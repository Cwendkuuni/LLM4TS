package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMap;

public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private SettableBeanProperty delegate;
 private InnerClassProperty innerClassProperty;
 private AnnotatedConstructor annotatedConstructor;
 private Object instance;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 AnonymousInnerClass anonymousInnerClass = new AnonymousInnerClass();
 Class<?> innerClass = AnonymousInnerClass.InnerClass.class;
 Constructor<?> constructor = innerClass.getDeclaredConstructor(Object.class);
 constructor.setAccessible(true);
 delegate = new SettableBeanProperty(new PropertyName("testProperty"), new InnerClassSerializer(), null);
 innerClassProperty = new InnerClassProperty(delegate, constructor);
 annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
 instance = Mockito.mock(Object.class);
 }

 @Test
 public void testDefaultConstructor() throws Exception {
 InnerClassProperty innerClassPropertyWithNullAnnotatedConstructor = new InnerClassProperty(delegate, null);
 assertNull(innerClassPropertyWithNullAnnotatedConstructor._annotated);
 assertNull(innerClassPropertyWithNullAnnotatedConstructor._creator);

 InnerClassProperty innerClassPropertyWithAnnotatedConstructor = new InnerClassProperty(delegate, annotatedConstructor);
 assertNotNull(innerClassPropertyWithAnnotatedConstructor._annotated);
 assertSame(annotatedConstructor, innerClassPropertyWithAnnotatedConstructor._creator);
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Object> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 InnerClassProperty innerClassPropertyWithJsonDeserializer = new InnerClassProperty(innerClassProperty, jsonDeserializer);
 assertEquals(innerClassProperty, innerClassPropertyWithJsonDeserializer);
 assertSame(innerClassProperty._delegate, innerClassPropertyWithJsonDeserializer._delegate);
 assertSame(innerClassProperty._creator, innerClassPropertyWithJsonDeserializer._creator);
 assertSame(jsonDeserializer, innerClassPropertyWithJsonDeserializer._valueDeserializer);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = new PropertyName("testProperty2");
 InnerClassProperty innerClassPropertyWithNewName = new InnerClassProperty(innerClassProperty, newName);
 assertEquals(newName, innerClassPropertyWithNewName.getName());
 assertEquals(innerClassProperty, innerClassPropertyWithNewName._delegate);
 assertEquals(innerClassProperty._creator, innerClassPropertyWithNewName._creator);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testValue = new Object();
 Mockito.when(annotatedConstructor.getAnnotated()).thenReturn(annotatedConstructor);
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\":{\"value\":{\"test\":\"testValue\"}}}");
 innerClassProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
 Object actualValue = innerClassProperty.get(instance);
 assertEquals(testValue, actualValue);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testValue = new Object();
 Mockito.when(annotatedConstructor.getAnnotated()).thenReturn(annotatedConstructor);
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\":{\"value\":{\"test\":\"testValue\"}}}");
 Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
 assertEquals(testValue, result);
 }

 @Test
 public void testSet() throws Exception {
 Object testValue = new Object();
 innerClassProperty.set(instance, testValue);
 Object actualValue = innerClassProperty.get(instance);
 assertEquals(testValue, actualValue);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object testValue = new Object();
 Object result = innerClassProperty.setAndReturn(instance, testValue);
 assertEquals(testValue, result);
 }

 @Test
 public void testReadResolve() throws Exception {
 InnerClassProperty innerClassPropertyReadResolve = (InnerClassProperty) innerClassProperty.readResolve();
 assertEquals(innerClassProperty, innerClassPropertyReadResolve);
 assertEquals(innerClassProperty._delegate, innerClassPropertyReadResolve._delegate);
 assertEquals(innerClassProperty._creator, innerClassPropertyReadResolve._creator);
 assertEquals(innerClassProperty._annotated, innerClassPropertyReadResolve._annotated);
 }

 @Test
 public void testWriteReplace() throws Exception {
 Object innerClassPropertyWriteReplace = innerClassProperty.writeReplace();
 assertEquals(innerClassProperty, innerClassPropertyWriteReplace);
 assertEquals(innerClassProperty._delegate, innerClassPropertyWriteReplace._delegate);
 assertEquals(innerClassProperty._creator, innerClassPropertyWriteReplace._creator);
 assertEquals(annotatedConstructor, innerClassPropertyWriteReplace._annotated);
 }
}

 class AnonymousInnerClass {
 static class InnerClass {
 public Object test;
 }

 public static class InnerClassSerializer extends StdSerializer<InnerClass> {
 protected InnerClassSerializer() {
 this(null);
 }

 protected InnerClassSerializer(Class<InnerClass> t) {
 super(t);
 }
 }
 }