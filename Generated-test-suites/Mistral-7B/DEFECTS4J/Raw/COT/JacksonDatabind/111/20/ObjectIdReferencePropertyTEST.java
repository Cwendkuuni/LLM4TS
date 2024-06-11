package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ObjectIdInfo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private static final ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 private static final SettableBeanProperty forward = mock(SettableBeanProperty.class);
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
 private NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 private ObjectIdReferenceProperty srcObjectIdReferenceProperty = mock(ObjectIdReferenceProperty.class);
 private PropertyName newName = mock(PropertyName.class);
 private Object pojo = new Object() {};
 private JsonParser jsonParser = mock(JsonParser.class);
 private DeserializationContext deserializationContext = mock(DeserializationContext.class);
 private Object id;
 private Object value;
 private UnresolvedForwardReference reference;

 @Before
 public void setup() {
 when(forward.getObjectIdInfo()).thenReturn(objectIdInfo);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 when(srcObjectIdReferenceProperty._forward).thenReturn(forward);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(srcObjectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(srcObjectIdReferenceProperty, newName);
 }

 @Test
 public void testConstructorWithForward() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertSame(forward, objectIdReferenceProperty._forward);
 assertSame(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructorWithDeserializer() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(srcObjectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 assertSame(srcObjectIdReferenceProperty, objectIdReferenceProperty);
 assertSame(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
 assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testConstructorWithNewName() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(srcObjectIdReferenceProperty, newName);
 assertSame(srcObjectIdReferenceProperty, objectIdReferenceProperty);
 assertSame(newName, objectIdReferenceProperty._name);
 }

 @Test
 public void testWithName() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
 assertSame(objectIdReferenceProperty, objectIdReferenceProperty._delegate);
 assertSame(newName, objectIdReferenceProperty._name);
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 assertSame(objectIdReferenceProperty, objectIdReferenceProperty._delegate);
 assertSame(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
 assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testWithNullProvider() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, nullValueProvider);
 assertSame(objectIdReferenceProperty, objectIdReferenceProperty._delegate);
 assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testFixAccess() throws IOException {
 objectIdReferenceProperty.fixAccess(mock(DeserializationConfig.class));
 forward.fixAccess(mock(DeserializationConfig.class));
 }

 @Test
 public void testGetAnnotation() throws IOException {
 Annotation annotation = mock(Annotation.class);
 when(forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws IOException {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(forward.getMember()).thenReturn(member);
 assertSame(member, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws IOException {
 when(forward.getCreatorIndex()).thenReturn(1);
 assertEquals(1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext, pojo)).thenReturn(id);
 objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, pojo);
 assertSame(id, pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 when(objectIdReferenceProperty.setAndReturn(pojo, id)).thenReturn(value);
 assertSame(value, objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, pojo));
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 ObjectIdReferenceProperty.PropertyReferring referencing = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, _type.getRawClass(), pojo);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
 objectIdReferenceProperty.handleResolvedForwardReference(id, value);
 assertSame(value, pojo);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testUnresolvedForwardReference() throws IOException {
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new UnresolvedForwardReference());
 objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 }

 @Test
 public void testSet() throws IOException {
 when(forward.set(pojo, value)).thenReturn(pojo);
 objectIdReferenceProperty.set(pojo, value);
 assertSame(pojo, objectIdReferenceProperty._bean);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 when(forward.setAndReturn(pojo, value)).thenReturn(value);
 assertSame(value, objectIdReferenceProperty.setAndReturn(pojo, value));
 }

 @Test
 public void testNullValue() throws IOException {
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);
 assertNull(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, pojo));
 }

 @Test
 public void testTypeNameFiltering() throws IOException {
 SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter();
 filter.setInclude("field1");
 when(forward.getName()).thenReturn(new PropertyName("field1"));
 assertFalse(objectIdReferenceProperty.isCandidate(filter));

 filter.setInclude("*");
 assertTrue(objectIdReferenceProperty.isCandidate(filter));
 }
}