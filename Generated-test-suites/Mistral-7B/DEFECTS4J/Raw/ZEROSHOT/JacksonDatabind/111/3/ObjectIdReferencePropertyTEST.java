package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private SettableBeanProperty settableBeanPropertyMock;
 private ObjectIdInfo objectIdInfoMock;
 private JsonDeserializer<?> jsonDeserializerMock;
 private JsonParser jsonParserMock;
 private DeserializationContext deserializationContextMock;
 private Object instance;

 @Before
 public void setup() {
 settableBeanPropertyMock = Mockito.mock(SettableBeanProperty.class);
 objectIdInfoMock = Mockito.mock(ObjectIdInfo.class);
 jsonDeserializerMock = Mockito.mock(JsonDeserializer.class);
 jsonParserMock = Mockito.mock(JsonParser.class);
 deserializationContextMock = Mockito.mock(DeserializationContext.class);
 instance = new Object();

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 }

 @Test
 public void testWithName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
 assertEquals(newObjectIdReferenceProperty.getName(), newName);
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> newJsonDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(newJsonDeserializer);
 assertEquals(newObjectIdReferenceProperty.getValueDeserializer(), newJsonDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = Mockito.mock(NullValueProvider.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nva);
 assertEquals(newObjectIdReferenceProperty.getNullProvider(), nva);
 }

 @Test
 public void testFixAccess() {
 objectIdReferenceProperty.fixAccess(Mockito.mock(DeserializationConfig.class));
 Mockito.verify(settableBeanPropertyMock).fixAccess(Mockito.any(DeserializationConfig.class));
 }

 @Test
 public void testGetAnnotation() {
 Class<?> annotationType = Mockito.mock(Class.class);
 assertNull(objectIdReferenceProperty.getAnnotation(annotationType));
 Mockito.when(settableBeanPropertyMock.getAnnotation(annotationType)).thenReturn(Mockito.mock(Annotation.class));
 assertEquals(objectIdReferenceProperty.getAnnotation(annotationType), Mockito.mock(Annotation.class));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMemberMock = Mockito.mock(AnnotatedMember.class);
 assertEquals(objectIdReferenceProperty.getMember(), annotatedMemberMock);
 Mockito.when(settableBeanPropertyMock.getMember()).thenReturn(annotatedMemberMock);
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(objectIdReferenceProperty.getCreatorIndex(), -1);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object deserializedObject = new Object();
 Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn(deserializedObject);
 Mockito.when(jsonParserMock.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 Mockito.when(deserializationContextMock.findBeanProperty("testProperty")).thenReturn(objectIdReferenceProperty);
 Mockito.when(objectIdInfoMock.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));

 objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);
 assertEquals(instance.getClass().getDeclaredField("testProperty").get(instance), deserializedObject);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object deserializedObject = new Object();
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn(deserializedObject);
 Mockito.when(jsonParserMock.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 Mockito.when(deserializationContextMock.findBeanProperty("testProperty")).thenReturn(objectIdReferenceProperty);
 Mockito.when(objectIdInfoMock.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));
 Mockito.when(objectIdInfoMock.hasIdInfo()).thenReturn(true);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializerMock);
 Object result = property.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);
 assertNull(result);
 }

 @Test
 public void testSet() throws IOException {
 Object newInstance = new Object();
 Object newValue = new Object();
 objectIdReferenceProperty.set(newInstance, newValue);
 assertEquals(newInstance.getClass().getDeclaredField("testProperty").get(newInstance), newValue);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object newInstance = new Object();
 Object newValue = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(newInstance, newValue);
 assertEquals(result, newValue);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 Object id = new Object();
 Object value = new Object();
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 property.getParent().handleResolvedForwardReference(id, value);
 Mockito.verify(settableBeanPropertyMock).set(instance, value);
 }
}