package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

 private static class TestInnerClass {
 static class Inner {
 int i;
 }

 public static Inner createInnerInstance() {
 return new Inner();
 }
 }

 public static class TestBean {
 private TestInnerClass.Inner inner;

 public TestBean() {}

 public TestBean(TestInnerClass.Inner inner) {
 this.inner = inner;
 }

 @JsonCreator
 public TestBean(@JsonProperty("inner") TestInnerClass.Inner inner) {
 this.inner = inner;
 }
 }

 @Test
 public void testConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 Object bean = new Object()[0];
 Object innerInstance = TestInnerClass.createInnerInstance();
 property.deserializeAndSet(new JsonFactory(), new JsonDeserializationContext(), bean);
 TestBean testBean = (TestBean) bean;
 assertEquals(innerInstance, testBean.getInner());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser parser = jsonFactory.createParser("{\"inner\":{\"i\":1}}");
 Object bean = new Object()[0];
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 property.deserializeAndSet(parser, new JsonDeserializationContext(), bean);
 TestBean testBean = (TestBean) bean;
 assertEquals(1, testBean.getInner().i);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser parser = jsonFactory.createParser("{\"inner\":{\"i\":1}}");
 Object instance = new TestBean();
 Object newInnerInstance = TestInnerClass.createInnerInstance();
 InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null), null);
 Object result = property.deserializeSetAndReturn(parser, new JsonDeserializationContext(), instance);
 TestBean testBean = (TestBean) instance;
 assertEquals(newInnerInstance, testBean.getInner());
 assertEquals(testBean, result);
 }

 @Test
 public void testSet() throws IOException {
 TestBean testBean = new TestBean();
 Object innerInstance = TestInnerClass.createInnerInstance();
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 property.set(testBean, innerInstance);
 assertEquals(innerInstance, testBean.getInner());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 TestBean testBean = new TestBean();
 Object innerInstance = TestInnerClass.createInnerInstance();
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 Object newInnerInstance = TestInnerClass.createInnerInstance();
 assertEquals(testBean, property.setAndReturn(testBean, innerInstance));
 assertEquals(innerInstance, testBean.getInner());
 assertEquals(testBean, property.setAndReturn(testBean, newInnerInstance));
 assertEquals(newInnerInstance, testBean.getInner());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class, null);
 Object bean = new Object()[0];
 try {
 property.deserializeAndSet(new JsonFactory(), new JsonDeserializationContext(), bean);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testNullValueDeserializer() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 Object bean = new Object()[0];
 try {
 property.deserializeAndSet(new JsonFactory(), new JsonDeserializationContext(), bean);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testNullInnerInstance() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser parser = jsonFactory.createParser("{\"inner\":null}");
 try {
 property.deserializeAndSet(parser, new JsonDeserializationContext(), new Object()[0]);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testInvalidInnerInstanceDeserialization() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser parser = jsonFactory.createParser("{\"inner\":[1]}");
 try {
 property.deserializeAndSet(parser, new JsonDeserializationContext(), new Object()[0]);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNullDelegate() {
 InnerClassProperty property = new InnerClassProperty(null, null);
 try {
 property.withName(null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCreator() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class, null);
 Constructor<?> constructor = TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class, AnnotatedConstructor.class);
 try {
 property = new InnerClassProperty(property, constructor);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAnnotatedConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class, null);
 Constructor<?> constructor = TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class, AnnotatedConstructor.class);
 AnnotatedConstructor ann = (AnnotatedConstructor) constructor.getParameterTypes()[2];
 try {
 property = new InnerClassProperty(property, constructor, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testWithName() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 PropertyName newName = PropertyName.construct("testName");
 InnerClassProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 JsonDeserializer<?> deser = new StdDeserializer<Object>() {};
 InnerClassProperty newProperty = property.withValueDeserializer(deser);
 assertNotNull(newProperty.getValueDeserializer());
 assertEquals(deser, newProperty.getValueDeserializer());
 }

 @Test
 public void testAssignIndex() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 property.assignIndex(1);
 assertEquals(1, property.getPropertyIndex());
 }

 @Test
 public void testGetPropertyIndex() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 assertEquals(-1, property.getPropertyIndex());
 }

 @Test
 public void testGetAnnotation() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 Annotation annotation = new TestAnnotation();
 TestBean.Inner innerClass = TestBean.class.getDeclaredInnerClass("Inner");
 innerClass.getDeclaredAnnotation(TestAnnotation.class).forInstance(innerClass);
 assertEquals(annotation, property.getAnnotation(TestAnnotation.class));
 }

 @Test
 public void testGetMember() {
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 TestBean.Inner innerClass = TestBean.class.getDeclaredInnerClass("Inner");
 assertEquals(innerClass.getMember(), property.getMember());
 }

 @Test
 public void testReadResolve() throws IOException {
 Object bean = new Object()[0];
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 property.setValueDeserializer(new StdDeserializer<Object>() {});
 property.writeValueAsBytes(new ObjectOutputStream(new ByteArrayOutputStream()));
 byte[] serialized = ((ByteArrayOutputStream)new ByteArrayOutputStream()).toByteArray();
 Object deserialized = new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject();
 assertEquals(property, deserialized);
 }

 @Test
 public void testWriteReplace() throws IOException {
 Object bean = new Object()[0];
 InnerClassProperty property = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(SettableBeanProperty.class, Constructor.class), null);
 Object fakeInstance = new Object() {};
 property.setValueDeserializer(new StdDeserializer<Object>() {});
 property.writeValueAsBytes(new ObjectOutputStream(new ByteArrayOutputStream()));
 byte[] serialized = ((ByteArrayOutputStream)new ByteArrayOutputStream()).toByteArray();
 Object deserialized = new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject();
 assertEquals(property, deserialized);
 }
}