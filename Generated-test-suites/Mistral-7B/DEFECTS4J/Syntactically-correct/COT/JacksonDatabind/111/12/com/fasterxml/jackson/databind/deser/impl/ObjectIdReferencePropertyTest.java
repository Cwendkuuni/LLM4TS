package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class ObjectIdReferencePropertyTest {

 private SettableBeanProperty _forward;
 private ObjectIdInfo _objectIdInfo;
 private ObjectIdReferenceProperty _property;
 private ObjectNode _rootNode;
 private AnnotationIntrospector _introspector;
 private JsonDeserializer<?> _deserializer;
 private JsonParser _parser;
 private Object _instance;
 private NullValueProvider _nva;

 @Before
 public void setUp() throws Exception {
 _forward = mock(SettableBeanProperty.class);
 _objectIdInfo = mock(ObjectIdInfo.class);
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 _introspector = mock(AnnotationIntrospector.class);
 _deserializer = mock(JsonDeserializer.class);
 _parser = mock(JsonParser.class);
 _instance = mock(Object.class);
 _nva = mock(NullValueProvider.class);
 }

 @Test
 public void test_constructor_with_forward_and_objectIdInfo() throws Exception {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 assertSame(_forward, property._forward);
 assertSame(_objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void test_constructor_with_src_deserializer_and_nva() throws Exception {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, _deserializer, _nva);
 assertSame(_forward, property._forward);
 assertSame(_objectIdInfo, property._objectIdInfo);
 assertSame(_deserializer, property._valueDeserializer);
 assertSame(_nva, property._nullProvider);
 }

 @Test
 public void test_constructor_with_src_and_newName() throws Exception {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, new PropertyName("test"));
 assertSame(_forward, property._forward);
 assertSame(_objectIdInfo, property._objectIdInfo);
 assertSame("test", property.getName());
 }

 @Test
 public void test_withName() throws Exception {
 ObjectIdReferenceProperty property = _property;
 ObjectIdReferenceProperty newProperty = property.withName(new PropertyName("test"));
 assertSame(_forward, newProperty._forward);
 assertSame(_objectIdInfo, newProperty._objectIdInfo);
 assertSame("test", newProperty.getName());
 }

 @Test
 public void test_withValueDeserializer() throws Exception {
 ObjectIdReferenceProperty property = _property;
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);
 assertSame(_forward, newProperty._forward);
 assertSame(_objectIdInfo, newProperty._objectIdInfo);
 assertSame(deserializer, newProperty._valueDeserializer);
 }

 @Test
 public void test_withNullProvider() throws Exception {
 ObjectIdReferenceProperty property = _property;
 NullValueProvider nva = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
 assertSame(_forward, newProperty._forward);
 assertSame(_objectIdInfo, newProperty._objectIdInfo);
 assertSame(nva, newProperty._nullProvider);
 }

 @Test
 public void test_fixAccess() throws Exception {
 ObjectIdReferenceProperty property = _property;
 property.fixAccess(mock(DeserializationConfig.class));
 _forward.fixAccess(mock(DeserializationConfig.class));
 }

 @Test
 public void test_getAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 when(_forward.getAnnotation(AnnotationIntrospector.class)).thenReturn(_introspector);
 when(_introspector.findAnnotation(_property.getName(), Annotation.class)).thenReturn(annotation);
 assertSame(annotation, _property.getAnnotation(Annotation.class));
 }

 @Test
 public void test_getMember() throws Exception {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(_forward.getMember()).thenReturn(member);
 assertSame(member, _property.getMember());
 }

 @Test
 public void test_getCreatorIndex() throws Exception {
 assertSame(-1, _property.getCreatorIndex());
 }

 @Test
 public void test_deserializeAndSet() throws Exception {
 Object value = mock(Object.class);
 when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
 when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(_objectIdInfo.getObjectIdReader()).thenReturn(mock(ObjectIdReader.class));
 _property.deserializeAndSet(_parser, mock(DeserializationContext.class), _instance);
 assertNull(_instance);
 }

 @Test(expected = IOException.class)
 public void test_deserializeAndSet_with_UnresolvedForwardReference() throws Exception {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
 when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(_objectIdInfo.getObjectIdReader()).thenThrow(new IOException());
 _property.deserializeAndSet(_parser, mock(DeserializationContext.class), _instance);
 }

 @Test
 public void test_deserializeSetAndReturn_with_setAndReturn() throws Exception {
 Object value = mock(Object.class);
 when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
 when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(setValueAndReturn(_instance, value)).thenReturn(value);
 when(_property.deserialize(JsonParser.class, mock(DeserializationContext.class))).thenReturn(value);
 Object result = _property.deserializeSetAndReturn(_parser, mock(DeserializationContext.class), _instance);
 assertSame(value, result);
 }

 @Test
 public void test_deserializeSetAndReturn_with_UnresolvedForwardReference() throws Exception {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
 when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 PropertyReferring referring = new PropertyReferring(_property, ref, Object.class, _instance);
 _property.deserializeSetAndReturn(_parser, mock(DeserializationContext.class), _instance);
 assertNull(_instance);
 }

 @Test
 public void test_set() throws Exception {
 Object value = mock(Object.class);
 _property.set(_instance, value);
 assertSame(value, _instance);
 }

 @Test
 public void test_setAndReturn() throws Exception {
 Object value = mock(Object.class);
 Object result = _property.setAndReturn(_instance, value);
 assertSame(value, result);
 }

 private Object setValueAndReturn(Object instance, Object value) throws IOException {
 throw new UnsupportedOperationException();
 }
}