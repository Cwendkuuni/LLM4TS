package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;

import junit.framework.TestCase;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty _property;
 private ObjectIdInfo _objectIdInfo;
 private SettableBeanProperty _forward;
 private Object _pojo;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 _forward = mock(SettableBeanProperty.class);
 _objectIdInfo = mock(ObjectIdInfo.class);
 _pojo = new Object();
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 }

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 assertEquals(_forward, actual._forward);
 assertEquals(_objectIdInfo, actual._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrc() throws InstantiationException, IllegalAccessException {
 SettableBeanProperty src = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(_property, _valueDeserializer, _nullProvider);
 assertEquals(_property, actual._forward);
 assertEquals(_objectIdInfo, actual._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcAndNewName() throws InstantiationException, IllegalAccessException {
 SettableBeanProperty src = mock(SettableBeanProperty.class);
 PropertyName newName = mock(PropertyName.class);
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(_property, newName);
 assertEquals(_property, actual._forward);
 assertEquals(_objectIdInfo, actual._objectIdInfo);
 assertEquals(newName, actual.getName());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 when(_forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 Annotation result = _property.getAnnotation(Annotation.class);
 assertEquals(annotation, result);
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember actual = _property.getMember();
 assertEquals(_forward.getMember(), actual);
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 assertEquals(_forward.getCreatorIndex(), _property.getCreatorIndex());
 }

 @Test
 public void testFixAccess() throws Exception {
 DeserializationConfig config = mock(DeserializationConfig.class);
 _property.fixAccess(config);
 verify(_forward).fixAccess(config);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object value = new Object();
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(_forward.deserialize(p, ctxt)).thenReturn(value);
 _property.deserializeAndSet(p, ctxt, _pojo);
 assertEquals(value, _pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object value = new Object();
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(_forward.deserialize(p, ctxt)).thenReturn(value);
 Object result = _property.deserializeSetAndReturn(p, ctxt, _pojo);
 assertEquals(null, result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws Exception {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(_forward.deserialize(p, ctxt)).thenThrow(new UnresolvedForwardReference("test"));
 _property.deserializeSetAndReturn(p, ctxt, _pojo);
 fail("Should have thrown UnresolvedForwardReference exception");
 }

 @Test
 public void testSet() throws Exception {
 Object value = new Object();
 _property.set(_pojo, value);
 assertEquals(value, _pojo);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object value = new Object();
 Object result = _property.setAndReturn(_pojo, value);
 assertEquals(null, result);
 }

 @Test
 public void testPropertyReferring() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 Class<?> type = Object.class;
 Object pojo = new Object();
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_forward, _valueDeserializer, _nullProvider);
 ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, ref, type, pojo);
 referring.handleResolvedForwardReference(1L, _pojo);
 assertEquals(_pojo, _pojo); // Check pojo is not changed
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleResolvedForwardReferenceWhenNoPreviousUnresolved() throws Exception {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_forward, _valueDeserializer, _nullProvider);
 ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, refMock(), Object.class, _pojo);
 referring.handleResolvedForwardReference(1L, _pojo);
 fail("Should have thrown IllegalArgumentException");
 }
}