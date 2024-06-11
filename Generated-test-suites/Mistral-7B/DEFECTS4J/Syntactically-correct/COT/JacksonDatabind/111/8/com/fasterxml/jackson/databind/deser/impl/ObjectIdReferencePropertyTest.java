package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private static final ObjectIdInfo objectIdInfo = new ObjectIdInfo(null);
 private static final JsonDeserializer<?> jsonDeserializer = new IdentityObjectDeserializer();
 private static final SettableBeanProperty settableBeanProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 private static final SettableBeanProperty settableBeanPropertyForward = new ObjectIdReferenceProperty(settableBeanProperty, null);
 private static final ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyForward, jsonDeserializer, null);
 private static final NullValueProvider nullValueProvider = new NullValueProvider();

 @Test
 public void testConstructorWithSettableBeanPropertyAndObjectIdInfo() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 assertEquals("Expected instance of ObjectIdReferenceProperty", objectIdReferenceProperty.getClass(), actual.getClass());
 }

 @Test
 public void testConstructorWithObjectIdReferencePropertyAndJsonDeserializerAndNullValueProvider() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 assertEquals("Expected instance of ObjectIdReferenceProperty", objectIdReferenceProperty.getClass(), actual.getClass());
 }

 @Test
 public void testConstructorWithObjectIdReferencePropertyAndPropertyName() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(objectIdReferenceProperty, new PropertyName("testName"));
 assertEquals("Expected instance of ObjectIdReferenceProperty", objectIdReferenceProperty.getClass(), actual.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFixAccessWithNullDeserializationConfig() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(settableBeanPropertyForward, jsonDeserializer, nullValueProvider), null);
 objectIdReferenceProperty.fixAccess(null);
 }

 @Test
 public void testGetAnnotation() {
 Class<?> annotationType = Mockito.mock(Annotation.class);
 Annotation expected = new Annotation() {};
 Annotation actual = objectIdReferenceProperty.getAnnotation(annotationType.getClass());
 assertEquals("Expected instance of Annotation", expected, actual);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember expected = Mockito.mock(AnnotatedMember.class);
 AnnotatedMember actual = objectIdReferenceProperty.getMember();
 assertEquals("Expected instance of AnnotatedMember", expected, actual);
 }

 @Test
 public void testGetCreatorIndex() {
 int expected = 0;
 int actual = objectIdReferenceProperty.getCreatorIndex();
 assertEquals("Expected creator index is zero", expected, actual);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 objectIdReferenceProperty.deserializeAndSet( Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
 assertEquals("Expected instance passed to setter is the same as the instance returned by deserialization", instance, _pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 Object result = objectIdReferenceProperty.deserializeSetAndReturn( Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
 assertEquals("Expected null as result of deserialization", null, result);
 setAndReturn(instance, value);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
 Object instance = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 Object result = objectIdReferenceProperty.deserializeSetAndReturn( Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
 assertTrue("Expected UnresolvedForwardReference instance to be thrown", reference.getClass().isAssignableFrom(result.getClass()));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();Object value = new Object();
 objectIdReferenceProperty.set(instance, value);
 assertEquals("Expected instance passed to setter is the same as the instance passed to setter method", instance, _pojo);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(instance, value);
 assertEquals("Expected instance passed to setter is the same as the instance returned by setter method", instance, _pojo);
 assertEquals("Expected instance passed to setter is the same as the instance returned by getter", value, result);
 }

 @Test
 public void testPropertyReferringConstructor() {
 ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(settableBeanPropertyForward, jsonDeserializer, nullValueProvider), null);
 Object id = new Object();
 Object value = new Object();
 ObjectIdReferenceProperty.PropertyReferring actual = parent.new PropertyReferring(id, null, Object.class, new Object());
 assertEquals("Expected instance of PropertyReferring", actual.getClass(), ObjectIdReferenceProperty.PropertyReferring.class);
 assertEquals("Expected parent to be same instance as passed to PropertyReferring constructor", parent, actual._parent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyReferringConstructorWithNullId() {
 ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(settableBeanPropertyForward, jsonDeserializer, nullValueProvider), null);
 ObjectIdReferenceProperty.PropertyReferring actual = parent.new PropertyReferring(null, null, Object.class, new Object());
 }

 @Test
 public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
 ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(settableBeanPropertyForward, jsonDeserializer, nullValueProvider), null);
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 ObjectIdReferenceProperty.PropertyReferring actual = parent.new PropertyReferring(reference, null, Object.class, new Object());
 Object id = new Object();
 Object value = new Object();
 actual.handleResolvedForwardReference(id, value);
 assertEquals("Expected _pojo instance to be same as passed value", value, _pojo);
 }

}