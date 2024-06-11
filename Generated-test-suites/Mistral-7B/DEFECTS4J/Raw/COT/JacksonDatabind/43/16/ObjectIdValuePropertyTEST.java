package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.SettableBeanProperty;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReaderMock;
 private PropertyName propertyNameMock;
 private JsonDeserializer<?> valueDeserializerMock;
 private PropertyMetadata metadataMock;
 private SettableBeanProperty idPropertyMock;

 @Test
 public void testConstructorWithObjectIdReader() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 assertEquals(objectIdReaderMock.propertyName, objectIdValueProperty.getName());
 assertEquals(objectIdReaderMock.getIdType(), objectIdValueProperty.getMemberClass());
 assertNull(objectIdValueProperty.getValueDeserializer());
 assertEquals(objectIdReaderMock, objectIdValueProperty.getMember());
 }

 @Test
 public void testConstructorWithObjectIdValueProperty() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValuePropertyMock, valueDeserializerMock);
 assertEquals(objectIdValuePropertyMock.getName(), objectIdValueProperty.getName());
 assertEquals(objectIdValuePropertyMock.getMemberClass(), objectIdValueProperty.getMemberClass());
 assertEquals(valueDeserializerMock, objectIdValueProperty.getValueDeserializer());
 assertEquals(objectIdValuePropertyMock, objectIdValueProperty.getMember());
 }

 @Test
 public void testConstructorWithObjectIdValuePropertyAndPropertyName() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValuePropertyMock, propertyNameMock);
 assertEquals(propertyNameMock, objectIdValueProperty.getName());
 assertEquals(objectIdValuePropertyMock.getMemberClass(), objectIdValueProperty.getMemberClass());
 assertEquals(objectIdValuePropertyMock, objectIdValueProperty.getMember());
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(propertyNameMock);
 assertEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals(propertyNameMock, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializerMock);
 assertEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals(valueDeserializerMock, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetOnObjectIdPropertyWithNoIdProperty() throws IOException {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 Object id = new Object();
 objectIdValueProperty.setAndReturn(new Object(), id);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 JsonParser jsonParserMock = mock(JsonParser.class);
 DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
 Object instance = new Object();
 when(jsonParserMock.nextValue()).thenReturn(null);

 objectIdValueProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);
 assertNull(instance);
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws IOException {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 JsonParser jsonParserMock = mock(JsonParser.class);
 DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = null;

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 JsonParser jsonParserMock = mock(JsonParser.class);
 DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = new Object();
 ReadableObjectId roidMock = mock(ReadableObjectId.class);
 when(deserializationContextMock.findObjectId(anyObject(), eq(objectIdReaderMock.generator), eq(objectIdReaderMock.resolver))).thenReturn(roidMock);
 when(roidMock.bindItem(instance)).thenReturn(instance);
 when(objectIdReaderMock.idProperty).thenReturn(idPropertyMock);
 when(idPropertyMock.setAndReturn(instance, anyObject())).thenReturn(instance);

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
 Object instance = new Object();
 Object id = new Object();
 SettableBeanProperty idPropertyMock = mock(SettableBeanProperty.class);
 when(idPropertyMock.setAndReturn(instance, anyObject())).thenReturn(instance);
 when(objectIdReaderMock.idProperty).thenReturn(idPropertyMock);

 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(instance, result);
 }
}