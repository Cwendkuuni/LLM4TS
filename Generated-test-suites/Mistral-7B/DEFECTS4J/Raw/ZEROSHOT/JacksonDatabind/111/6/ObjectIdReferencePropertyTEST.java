package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private SettableBeanProperty settableBeanPropertyMock;
 private ObjectIdInfo objectIdInfoMock;
 private JsonDeserializer<?> jsonDeserializerMock;
 private JsonParser jsonParserMock;
 private DeserializationContext deserializationContextMock;
 private NullValueProvider nullValueProviderMock;
 private Object pojo;

 @Before
 public void setup() {
 settableBeanPropertyMock = Mockito.mock(SettableBeanProperty.class);
 objectIdInfoMock = Mockito.mock(ObjectIdInfo.class);
 jsonDeserializerMock = Mockito.mock(JsonDeserializer.class);
 jsonParserMock = Mockito.mock(JsonParser.class);
 deserializationContextMock = Mockito.mock(DeserializationContext.class);
 nullValueProviderMock = Mockito.mock(NullValueProvider.class);

 pojo = Mockito.mock(Object.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 }

 @Test
 public void testNewObjectIdReferenceProperty() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, null, null);
 assertEquals(settableBeanPropertyMock, objectIdReferenceProperty._forward);
 assertNull(objectIdReferenceProperty._objectIdInfo);
 assertNull(objectIdReferenceProperty._valueDeserializer);
 assertNull(objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testNewObjectIdReferencePropertyWithObjectIdInfo() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, jsonDeserializerMock, nullValueProviderMock, objectIdInfoMock);
 assertEquals(settableBeanPropertyMock, objectIdReferenceProperty._forward);
 assertEquals(objectIdInfoMock, objectIdReferenceProperty._objectIdInfo);
 assertNull(objectIdReferenceProperty._valueDeserializer);
 assertNull(objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testNewObjectIdReferencePropertyWithJsonDeserializer() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, jsonDeserializerMock);
 assertEquals(settableBeanPropertyMock, objectIdReferenceProperty._forward);
 assertNull(objectIdReferenceProperty._objectIdInfo);
 assertEquals(jsonDeserializerMock, objectIdReferenceProperty._valueDeserializer);
 assertNull(objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testNewObjectIdReferencePropertyWithNullValueProvider() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, jsonDeserializerMock, nullValueProviderMock, objectIdInfoMock);
 assertEquals(settableBeanPropertyMock, objectIdReferenceProperty._forward);
 assertEquals(objectIdInfoMock, objectIdReferenceProperty._objectIdInfo);
 assertEquals(jsonDeserializerMock, objectIdReferenceProperty._valueDeserializer);
 assertEquals(nullValueProviderMock, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testWithName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, null, null);
 ObjectIdReferenceProperty objectIdReferencePropertyWithName = objectIdReferenceProperty.withName(newName);
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithName._forward);
 assertEquals(newName, objectIdReferencePropertyWithName.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 ObjectIdReferenceProperty objectIdReferencePropertyWithDeserializer = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithDeserializer._forward);
 assertEquals(jsonDeserializer, objectIdReferencePropertyWithDeserializer._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 ObjectIdReferenceProperty objectIdReferencePropertyWithNullProvider = objectIdReferenceProperty.withNullProvider(nullValueProvider);
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithNullProvider._forward);
 assertEquals(nullValueProvider, objectIdReferencePropertyWithNullProvider._nullProvider);
 }

 @Test
 public void testFixAccess() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 objectIdReferenceProperty.fixAccess(deserializationContextMock);
 assertTrue(settableBeanPropertyMock.fixAccess(deserializationContextMock) == true);
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = Mockito.mock(Annotation.class);
 when(objectIdInfoMock.hasAnnotation(Mockito.any(Class.class))).thenReturn(true);
 when(settableBeanPropertyMock.getAnnotation(Mockito.any(Class.class))).thenReturn(annotation);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, objectIdInfoMock);
 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(Mockito.any(Class.class)));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMemberMock = Mockito.mock(AnnotatedMember.class);
 when(settableBeanPropertyMock.getMember()).thenReturn(annotatedMemberMock);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 assertEquals(annotatedMemberMock, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 when(settableBeanPropertyMock.getCreatorIndex()).thenReturn(1);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 assertEquals(1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object input = Mockito.mock(Object.class);
 String value = "testValue";
 when(jsonParserMock.getValueAsString()).thenReturn(value);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, pojo);
 assertEquals(value, pojo.getClass().getDeclaredField(settableBeanPropertyMock.getName().getShortName()).get(pojo));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object input = Mockito.mock(Object.class);
 String value = "testValue";
 when(jsonParserMock.getValueAsString()).thenReturn(value);
 when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock, pojo)).thenReturn(value);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, jsonDeserializerMock, nullValueProviderMock, null);
 Object output = objectIdReferenceProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, pojo);
 assertEquals(value, output);
 }

 @Test
 public void testSet() throws IOException {
 Object input = Mockito.mock(Object.class);
 String value = "testValue";
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 objectIdReferenceProperty.set(pojo, value);
 assertEquals(value, pojo.getClass().getDeclaredField(settableBeanPropertyMock.getName().getShortName()).get(pojo));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object input = Mockito.mock(Object.class);
 String value = "testValue";
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, null);
 Object output = objectIdReferenceProperty.setAndReturn(pojo, value);
 assertEquals(value, output);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 Object parentId = Mockito.mock(Object.class);
 Object resolvedValue = "testValue";
 ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, null, nullValueProviderMock, objectIdInfo);
 ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, Object.class, pojo);
 when(jsonParserMock.getValueAsString()).thenReturn("testId");
 when(deserializationContextMock.findIdValue(Mockito.any(), Mockito.eq(Object.class), Mockito.any(), Mockito.eq(parentId))).thenReturn(resolvedValue);
 objectIdReferenceProperty.handleResolvedForwardReference(parentId, pojo);
 assertEquals(resolvedValue, pojo.getClass().getDeclaredField(settableBeanPropertyMock.getName().getShortName()).get(pojo));
 }
}