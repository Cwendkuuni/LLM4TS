package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private Object instance;

 @Before
 public void setUp() throws Exception {
 Object bean = new Object();
 methodProperty = new MethodProperty(
 new BeanPropertyDefinition(new PropertyName("test"), null, null),
 null, null, Annotations.empty(),
 new AnnotatedMethod(new Method("setTest", new Class[] { Object.class }), bean.getClass()) {
 @Override
 public Annotation[] getParameterAnnotations(int i) {
 return new Annotation[] { new MyAnnotation() };
 }
 });
 instance = bean;
 }

 @Test
 public void testConstructor() {
 MethodProperty actual = new MethodProperty(methodProperty, methodProperty._annotated, methodProperty._setter);
 assertEquals(methodProperty, actual);
 }

 @Test
 public void testWithName() {
 SettableBeanProperty actual = methodProperty.withName(new PropertyName("newName"));
 assertEquals(methodProperty, actual);
 assertEquals("newName", actual.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
 SettableBeanProperty actual = methodProperty.withValueDeserializer(deserializer);
 assertEquals(methodProperty, actual);
 assertEquals(deserializer, actual.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = new NullValueProvider() {};
 SettableBeanProperty actual = methodProperty.withNullProvider(nva);
 assertEquals(methodProperty, actual);
 assertEquals(nva, actual._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 AnnotatedMethod annotated = methodProperty._annotated;
 DeserializationConfig config = new DeserializationConfig();
 methodProperty.fixAccess(config);
 assertEquals(true, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 assertEquals(annotated.getRawClass().getDeclaredMethod("setTest", new Class[] { Object.class }), annotated.getRawMethod());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<MyAnnotation> myAnnotationType = MyAnnotation.class;
 MyAnnotation expected = new MyAnnotation();
 MyAnnotation actual = (MyAnnotation) methodProperty.getAnnotation(myAnnotationType);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMember() {
 assertEquals(methodProperty._annotated, methodProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser jsonParser = new JsonFactory().createParser("{\"test\": null}");
 methodProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);
 assertNull(instance.getClass().getDeclaredMethod("getTest").invoke(instance));
 }

 @Test
 public void testDeserializeAndSetWithValue() throws IOException {
 JsonParser jsonParser = new JsonFactory().createParser("{\"test\": 1}");
 methodProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);
 assertEquals(1, instance.getClass().getDeclaredMethod("getTest").invoke(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser jsonParser = new JsonFactory().createParser("{\"test\": null}");
 Object actual = methodProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);
 assertNull(actual);
 assertNull(instance.getClass().getDeclaredMethod("getTest").invoke(instance));

 jsonParser = new JsonFactory().createParser("{\"test\": 1}");
 actual = methodProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);
 assertEquals(1, actual);
 assertEquals(1, instance.getClass().getDeclaredMethod("getTest").invoke(instance));
 }

 @Test
 public void testSet() throws IOException {
 Object value = new Object();
 methodProperty.set(instance, value);
 assertEquals(value, instance.getClass().getDeclaredMethod("getTest").invoke(instance));
 }

 @Test
 public void testSetWithValue() throws IOException {
 Object value = new Object();
 methodProperty.set(instance, value);
 assertEquals(value, instance.getClass().getDeclaredMethod("getTest").invoke(instance));
 }

 @Test(expected = InvocationTargetException.class)
 public void testSetWithException() throws IOException, InvocationTargetException {
 methodProperty.set(instance, new Object() {
 @Override public void invoke(Object obj, Object arg) throws Throwable {
 throw new IOException("Test exception");
 }
 });
 fail("Should throw InvocationTargetException");
 }

 @Test(expected = IOException.class)
 public void testSetWithIOException() throws IOException, InvocationTargetException {
 methodProperty.set(instance, new Object() {
 @Override public void invoke(Object obj, Object arg) throws IOException {
 throw new IOException("Test exception");
 }
 });
 }

 @Test
 public void testReadResolve() throws Exception {
 MethodProperty actual = (MethodProperty) methodProperty.readResolve();
 assertEquals(methodProperty, actual);
 }
}

class MyAnnotation {}