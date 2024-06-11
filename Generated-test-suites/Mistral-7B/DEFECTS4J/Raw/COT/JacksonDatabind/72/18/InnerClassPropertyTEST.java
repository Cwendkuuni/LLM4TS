package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AnnotatedConstructor;

public class InnerClassPropertyTest {

 private InnerClassProperty property;
 private Object bean;
 private JsonParser parser;
 private DeserializationContext context;
 private JsonDeserializer<Object> deserializer;
 private TypeDeserializer typeDeserializer;
 private AnnotatedConstructor constructor;

 @Before
 public void setup() throws Exception {
 Class<?> innerClass = new Object() {}.getClass();
 Class<?> outerClass = new Object() {
 public Inner inner = new Inner();
 }.getClass();
 Constructor<?> innerConstructor = innerClass.getDeclaredConstructor();
 innerConstructor.setAccessible(true);
 property = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), new JsonDeserializer.Std()), innerConstructor);
 bean = new Object();
 parser = new MockJsonParser();
 context = new MockContext();
 deserializer = new JsonDeserializer.Std();
 typeDeserializer = new StandardTypeDeserializer();
 constructor = new AnnotatedConstructor(null, innerConstructor, null, null);
 }

 @Test
 public void testConstructor() {
 InnerClassProperty instance = new InnerClassProperty(null, null);
 assertNull(instance._delegate);
 assertNull(instance._creator);
 assertNull(instance._annotated);
 }

 @Test
 public void testSettableBeanPropertyDelegate() {
 InnerClassProperty instance = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), new JsonDeserializer.Std()), innerConstructor);
 assertNotNull(instance._delegate);
 assertEquals(new StringProperty("name"), instance._delegate);
 }

 @Test
 public void testCreator() {
 InnerClassProperty instance = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), new JsonDeserializer.Std()), innerConstructor);
 assertNotNull(instance._creator);
 assertEquals(innerConstructor, instance._creator);
 }

 @Test
 public void testAnnotatedConstructor() {
 InnerClassProperty instance = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), new JsonDeserializer.Std()), constructor);
 assertNotNull(instance._annotated);
 assertEquals(constructor, instance._annotated.getAnnotated());
 }

 @Test
 public void testWithName() {
 PropertyName newName = new PropertyName("newName");
 InnerClassProperty instance = new InnerClassProperty(this.property, newName);
 assertEquals(newName, instance.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> deserializer = new JsonDeserializer.Std();
 InnerClassProperty instance = new InnerClassProperty(this.property, deserializer);
 assertEquals(deserializer, instance.getValueDeserializer());
 }

 @Test
 public void testAssignIndex() {
 InnerClassProperty instance = new InnerClassProperty(this.property, innerConstructor);
 int index = 0;
 instance.assignIndex(index);
 assertEquals(index, instance.getPropertyIndex());
 }

 @Test
 public void testGetPropertyIndex() {
 InnerClassProperty instance = new InnerClassProperty(this.property, innerConstructor);
 assertEquals(this.property.getPropertyIndex(), instance.getPropertyIndex());
 }

 @Test
 public void testGetAnnotation() {
 @JsonDeserialize JsonDeserializer jsonDeserializer = new JsonDeserializer.Std();
 @JsonSerialize JsonSerializer jsonSerializer = new JsonSerializer.Std();
 InnerClassProperty instance = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), jsonDeserializer), innerConstructor);
 assertEquals(jsonDeserializer, instance.getAnnotation(JsonDeserializer.class));
 assertEquals(jsonSerializer, instance.getAnnotation(JsonSerializer.class));
 }

 @Test
 public void testGetMember() {
 InnerClassProperty instance = new InnerClassProperty(this.property, innerConstructor);
 assertEquals(this.property.getMember(), instance.getMember());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() {
 InnerClassProperty instance = new InnerClassProperty(new SettableBeanProperty.Std(new StringProperty("name"), innerConstructor), null);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object value = new Object();
 property.deserializeAndSet(parser, context, bean);
 assertEquals(value, property.get(bean));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = property.deserializeSetAndReturn(parser, context, bean);
 assertEquals(instance, property.get(bean));
 }

 @Test
 public void testSet() throws IOException {
 Object value = new Object();
 property.set(bean, value);
 assertEquals(value, property.get(bean));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = property.setAndReturn(bean, new Object());
 assertEquals(instance, property.get(bean));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInstance() throws IOException {
 property.set(null, new Object());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() throws IOException {
 property.set(bean, null);
 }

 @Test
 public void testReadResolve() {
 InnerClassProperty instance = new InnerClassProperty(this.property, constructor);
 InnerClassProperty resolvedInstance = (InnerClassProperty) instance.readResolve();
 assertEquals(constructor, resolvedInstance._annotated.getAnnotated());
 }

 @Test
 public void testWriteReplace() {
 InnerClassProperty instance = new InnerClassProperty(this.property, constructor);
 Object replacedInstance = instance.writeReplace();
 assertEquals(constructor, replacedInstance._annotated.getAnnotated());
 }
}