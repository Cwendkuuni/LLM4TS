package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty _property;
 private SettableBeanProperty _forward;
 private ObjectIdInfo _objectIdInfo;
 private JsonDeserializer<?> _valueDeserializer;
 private Object _pojo;
 private AnnotatedMember _member;
 private JsonParser _parser;
 private DeserializationContext _context;
 private NullValueProvider _nullProvider;

 @Before
 public void setup() {
 _forward = mock(SettableBeanProperty.class);
 _objectIdInfo = mock(ObjectIdInfo.class);
 _valueDeserializer = mock(JsonDeserializer.class);
 _nullProvider = mock(NullValueProvider.class);
 _pojo = mock(Object.class);
 _member = mock(AnnotatedMember.class);
 _parser = mock(JsonParser.class);
 _context = mock(DeserializationContext.class);
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithForward() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 assertSame(_forward, property._forward);
 assertSame(_objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithDeserializer() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, _valueDeserializer, _nullProvider);
 assertSame(_property, property._forward);
 assertSame(_valueDeserializer, property._valueDeserializer);
 assertSame(_nullProvider, property._nullProvider);
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithNewName() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, new PropertyName("newName"));
 assertSame(_property, property);
 assertSame("newName", property.getName());
 }

 @Test
 public void testWithName() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, new PropertyName("newName"));
 assertSame(property, property.withName(new PropertyName("newName2")));
 assertSame("newName", property.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, _valueDeserializer, _nullProvider);
 assertSame(property, property.withValueDeserializer(_valueDeserializer));
 }

 @Test
 public void testWithNullProvider() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(_property, _valueDeserializer, _nullProvider);
 assertSame(property, property.withNullProvider(_nullProvider));
 }

 @Test
 public void testFixAccess() {
 when(_forward.fixAccess(_context)).thenReturn(null);
 _property.fixAccess(_context);
 verify(_forward).fixAccess(_context);
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = mock(Annotation.class);
 when(_forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, _property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 when(_forward.getMember()).thenReturn(_member);
 assertSame(_member, _property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 when(_forward.getCreatorIndex()).thenReturn(0);
 assertEquals(0, _property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode objectNode = mock(ObjectNode.class);
 when(_parser.readValueAsTree()).thenReturn(objectNode);
 when(_valueDeserializer.deserialize(_parser, _context, _pojo)).thenReturn("value");
 _property.deserializeAndSet(_parser, _context, _pojo);
 verify(_forward).set(_pojo, "value");
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 _property.deserializeSetAndReturn(_parser, _context, _pojo);
 assertNull(_property.getObjectId());
 }

 @Test
 public void testDeserializeSetAndReturnObject() throws IOException {
 ObjectNode objectNode = mock(ObjectNode.class);
 when(_parser.readValueAsTree()).thenReturn(objectNode);
 when(_valueDeserializer.deserialize(_parser, _context, _pojo)).thenReturn(new Object());
 when(_forward.setAndReturn(_pojo, _valueDeserializer.deserialize(_parser, _context, _pojo))).thenReturn(new Object());
 Object result = _property.deserializeSetAndReturn(_parser, _context, _pojo);
 assertSame(new Object(), result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws IOException, JsonMappingException {
 when(_parser.readValueAsTree()).thenReturn(mock(ObjectNode.class));
 when(_objectIdInfo != null).thenReturn(true);
 _property.deserializeSetAndReturn(_parser, _context, _pojo);
 }

 @Test
 public void testSet() throws IOException {
 _property.set(_pojo, "value");
 verify(_forward).set(_pojo, "value");
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object result = _property.setAndReturn(_pojo, "value");
 assertSame("value", result);
 }

 @Test
 public void testPropertyReferringConstructor() {
 ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
 UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
 PropertyReferring propertyReferring = new PropertyReferring(parent, reference, _type.getRawClass(), _pojo);
 assertSame(parent, propertyReferring._parent);
 assertSame(_pojo, propertyReferring._pojo);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 Object id = "id";
 Object value = "value";
 when(_parent.set(_pojo, value)).thenReturn(null);
 propertyReferring.handleResolvedForwardReference(id, value);
 }
}