package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<Object> jsonDeserializer;
 private NullValueProvider nullValueProvider;
 private Object pojo;

 @Before
 public void setup() {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 forward = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 pojo = Mockito.mock(Object.class);
 }

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertNotNull(objectIdReferenceProperty);
 assertEquals(forward, objectIdReferenceProperty._forward);
 assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcDeserializerAndNullValueProvider() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 assertNotNull(objectIdReferenceProperty);
 assertEquals(forward, objectIdReferenceProperty._forward);
 assertEquals(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
 assertEquals(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testConstructorWithSrcPropertyName() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, new PropertyName("testPropertyName"));
 assertNotNull(objectIdReferenceProperty);
 assertEquals(forward, objectIdReferenceProperty._forward);
 assertEquals("testPropertyName", objectIdReferenceProperty.getName());
 }

 @Test
 public void testWithName() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, new PropertyName("testPropertyName"));
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(new PropertyName("newTestPropertyName"));
 assertNotSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertEquals(newPropertyName("newTestPropertyName"), newObjectIdReferenceProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(Mockito.mock(JsonDeserializer.class));
 assertNotSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertEquals(Mockito.mock(JsonDeserializer.class), newObjectIdReferenceProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(Mockito.mock(NullValueProvider.class));
 assertNotSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertEquals(Mockito.mock(NullValueProvider.class), newObjectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 objectIdReferenceProperty.fixAccess(Mockito.mock(DeserializationConfig.class));
 forward.fixAccess(Mockito.mock(DeserializationConfig.class));
 verify(forward).fixAccess(Mockito.mock(DeserializationConfig.class));
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = Mockito.mock(Annotation.class);
 when(forward.getAnnotation(Mockito.any(Class.class))).thenReturn(annotation);
 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(Mockito.any(Class.class)));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 when(forward.getMember()).thenReturn(annotatedMember);
 assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 int creatorIndex = 0;
 when(forward.getCreatorIndex()).thenReturn(creatorIndex);
 assertEquals(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object value = Mockito.mock(Object.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 Object instance = pojo;
 objectIdReferenceProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), instance);
 verify(forward).set(instance, value);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object value = Mockito.mock(Object.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 Object instance = pojo;
 ObjectNode objectNode = Mockito.mock(ObjectNode.class);
 when(jsonParser.readValueAsTree()).thenReturn(objectNode);
 when(objectNode.get("testPropertyName")).thenReturn(value);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, Mockito.mock(DeserializationContext.class), instance);
 assertEquals(value, result);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object value = Mockito.mock(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 Object result = objectIdReferenceProperty.setAndReturn(pojo, value);
 assertEquals(value, result);
 }

 @Test
 public void testPropertyReferring() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 UnresolvedForwardReference unresolvedForwardReference = Mockito.mock(UnresolvedForwardReference.class);
 Object id = new Object();
 Object referringValue = new Object();
 when(unresolvedForwardReference.getId()).thenReturn(id);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, unresolvedForwardReference, Object.class, pojo);
 propertyReferring.handleResolvedForwardReference(id, referringValue);
 assertEquals(pojo, objectIdReferenceProperty.getPojo());
 assertEquals(referringValue, _forward.get(pojo));
 }
}