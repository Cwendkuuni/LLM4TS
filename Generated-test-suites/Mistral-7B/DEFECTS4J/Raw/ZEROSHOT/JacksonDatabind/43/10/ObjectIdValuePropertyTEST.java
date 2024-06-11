package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> jsonDeserializer;
 private JsonDeserializer<Object> idPropertyDeserializer;

 @Before
 public void setUp() {
 objectIdReader = mock(ObjectIdReader.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 idPropertyDeserializer = mock(JsonDeserializer.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), jsonDeserializer);
 }

 @Test
 public void testConstructorWithObjectIdReader() {
 assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertEquals(jsonDeserializer, objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getIdProperty());
 }

 @Test
 public void testCopyConstructor() {
 ObjectIdValueProperty copy = new ObjectIdValueProperty(objectIdValueProperty, idPropertyDeserializer);
 assertEquals(objectIdValueProperty, copy);
 assertEquals(objectIdReader, copy._objectIdReader);
 }

 @Test
 public void testWithName() {
 PropertyName newName = propertyName("newName");
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
 assertEquals(newName, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(idPropertyDeserializer);
 assertEquals(idPropertyDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object id = mock(Object.class);
 Object instance = mock(Object.class);
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
 verify(instance).setAndReturn(any(), any());
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 Object id = mock(Object.class);
 Object instance = mock(Object.class);
 ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
 when(objectIdReader.reader).thenReturn(mock(JsonParser.class));
 when(objectIdReader.generator).thenReturn(mock(ObjectIdentifierGenerator.class));
 when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(objectIdReader.findObjectId(any(), any(), any())).thenReturn(readableObjectId);

 Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws JsonProcessingException {
 Object id = mock(Object.class);
 Object instance = mock(Object.class);
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 objectIdValueProperty.set(instance, id);
 verify(instance).setAndReturn(any(), any());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullId() throws JsonProcessingException {
 Object instance = mock(Object.class);
 objectIdValueProperty.set(instance, null);
 }
}