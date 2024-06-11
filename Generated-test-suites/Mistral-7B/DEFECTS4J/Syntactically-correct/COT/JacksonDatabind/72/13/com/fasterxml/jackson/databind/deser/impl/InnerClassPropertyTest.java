package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

 private Object bean;
 private DeserializationContext ctxt;
 private JsonParser jp;
 private InnerClassProperty innerClassProperty;
 private SettableBeanProperty delegate;
 private Constructor<?> creator;

 @Before
 public void setup() throws Exception {
 Class<?> innerClassType = MyInnerClass.class;
 Class<?> outerClassType = MyOuterClass.class;
 delegate = new SettableBeanProperty(null, new AnnotatedClass(innerClassType));
 creator = innerClassType.getDeclaredConstructor();
 innerClassProperty = new InnerClassProperty(delegate, creator);
 bean = new MyOuterClass();
 ctxt = new TestContext();
 jp = new JsonTestParser();
 }

 @Test
 public void testInnerClassPropertyCreation() {
 InnerClassProperty property = new InnerClassProperty(delegate, creator);
 assertNotNull(property);
 assertEquals(delegate, property._delegate);
 assertNull(property._annotated);
 assertNotNull(property._creator);
 assertEquals(innerClassType, property._creator.getDeclaringClass());
 }

 @Test
 public void testInnerClassPropertyWithAnnotation() throws Exception {
 AnnotatedConstructor constructor = new AnnotatedConstructor(null, creator, null, null);
 Annotation annotation = new MyAnnotation();
 innerClassProperty = new InnerClassProperty(delegate, constructor, annotation);
 assertNotNull(innerClassProperty);
 assertEquals(delegate, innerClassProperty._delegate);
 assertNotNull(innerClassProperty._annotated);
 assertEquals(annotation, innerClassProperty._annotated.getAnnotationInstance());
 }

 @Test
 public void testInnerClassPropertyWithDeserializer() throws Exception {
 JsonDeserializer<MyInnerClass> deserializer = new MyJsonDeserializer();
 innerClassProperty = new InnerClassProperty(delegate, deserializer);
 assertNotNull(innerClassProperty);
 assertEquals(delegate, innerClassProperty._delegate);
 assertNull(innerClassProperty._annotated);
 assertNotNull(innerClassProperty._valueDeserializer);
 assertEquals(deserializer, innerClassProperty._valueDeserializer);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String json = "{\"name\":\"value\"}";
 jp.setCurrentValue(json);
 innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 MyInnerClass expectedValue = new MyInnerClass("value");
 assertEquals(expectedValue, bean.getInnerClass());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 String json = "{\"name\":\"value\"}";
 jp.setCurrentValue(json);
 MyInnerClass actualValue = (MyInnerClass) innerClassProperty.deserializeSetAndReturn(jp, ctxt, bean);
 MyInnerClass expectedValue = new MyInnerClass("value");
 assertEquals(expectedValue, actualValue);
 assertEquals(expectedValue, bean.getInnerClass());
 }

 @Test
 public void testSet() throws Exception {
 MyInnerClass value = new MyInnerClass("value");
 innerClassProperty.set(bean, value);
 assertEquals(value, bean.getInnerClass());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 MyInnerClass value = new MyInnerClass("value");
 MyInnerClass expectedValue = new MyInnerClass("value");
 MyInnerClass actualValue = (MyInnerClass) innerClassProperty.setAndReturn(bean, value);
 assertEquals(expectedValue, actualValue);
 assertEquals(expectedValue, bean.getInnerClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws Exception {
 Constructor<?> missingConstructor = null;
 innerClassProperty = new InnerClassProperty(delegate, missingConstructor);
 }

 @Test
 public void testNullValueDeserializer() throws Exception {
 innerClassProperty = new InnerClassProperty(delegate, null);
 jp.setValue(null);
 innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 assertNull(bean.getInnerClass());
 }

 @Test
 public void testValueDeserializationWithType() throws Exception {
 String json = "{\"type\":\"MyInnerClass\", \"name\":\"value\"}";
 jp.setCurrentValue(json);
 MyJsonDeserializer deserializer = new MyJsonDeserializer();
 innerClassProperty = new InnerClassProperty(delegate, deserializer, MyInnerClass.class);
 MyInnerClass actualValue = (MyInnerClass) innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 MyInnerClass expectedValue = new MyInnerClass("value");
 assertEquals(expectedValue, actualValue);
 assertEquals(expectedValue, bean.getInnerClass());
 }

 @Test(expected = IOException.class)
 public void testFailedToInitializeInstance() throws Exception {
 creator.setAccessible(true);
 creator.setException(new IOException("Failed to initialize instance"));
 innerClassProperty = new InnerClassProperty(delegate, creator);
 jp.setValue("{}");
 innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 }

 @Test(expected = IllegalAccessException.class)
 public void testFailedToDeserialize() throws Exception {
 creator.setAccessible(true);
 creator.setException(new IllegalAccessException("Failed to deserialize"));
 innerClassProperty = new InnerClassProperty(delegate, creator);
 jp.setValue("{}");
 innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 }

 @Test(expected = InstantiationException.class)
 public void testFailedToInstantiate() throws Exception {
 creator.setException(new InstantiationException("Failed to instantiate class"));
 innerClassProperty = new InnerClassProperty(delegate, creator);
 jp.setValue("{}");
 innerClassProperty.deserializeAndSet(jp, ctxt, bean);
 }

 @Test(expected = IllegalAccessException.class)
 public void testFailedToSetValue() throws Exception {
 creator.setAccessible(true);
 creator.setException(new IllegalAccessException("Failed to set value"));
 innerClassProperty = new InnerClassProperty(delegate, creator);
 innerClassProperty.set(bean, new Object());
 }

 @Test
 public void testReadResolve() throws Exception {
 Object resolvedObject = innerClassProperty.readResolve();
 assertEquals(innerClassProperty, resolvedObject);
 }

 @Test
 public void testWriteReplace() throws Exception {
 Object replacedObject = innerClassProperty.writeReplace();
 assertEquals(innerClassProperty, replacedObject);
 }
}

 class MyAnnotation {}
 class MyInnerClass {
 private String name;

 MyInnerClass(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }
 }

 class MyJsonDeserializer extends StdDeserializer<MyInnerClass> {
 @Override
 public MyInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyInnerClass(p.getValueAsString());
 }
 }

 class MyOuterClass {
 private MyInnerClass innerClass;

 // getter and setter methods
 }