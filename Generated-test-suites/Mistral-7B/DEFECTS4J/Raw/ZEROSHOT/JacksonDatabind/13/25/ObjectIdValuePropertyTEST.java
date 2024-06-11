package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdResolver;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> mockValueDeserializer;
 private JsonDeserializer<Object> mockIdDeserializer;
 private JsonDeserializer<Object> mockIdPropertyDeserializer;
 private ObjectIdReader.IdType idType;
 private ObjectIdReader.IdResolver idResolver;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private Object instance;

 @Before
 public void setup() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 idType = JsonId.IdType.INT_ID;
 idResolver = Mockito.mock(IdResolver.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 mockIdDeserializer = Mockito.mock(JsonDeserializer.class);
 mockIdPropertyDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty.SettableBeanProperty idProperty = Mockito.mock(ObjectIdValueProperty.SettableBeanProperty.class);
 TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
 ObjectIdValueProperty.ObjectIdReader objectIdReader = new ObjectIdReader(idType, idResolver, mockObjectIdReader);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), typeFactory);
 objectIdValueProperty.setIdProperty(idProperty);
 objectIdValueProperty.setValueDeserializer(mockValueDeserializer);
 mockJsonParser = Mockito.mock(JsonParser.class);
 mockContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 }

 @Test
 public void testInstance() {
 assertEquals(ObjectIdValueProperty.class, objectIdValueProperty.getClass());
 assertEquals(mockObjectIdReader, objectIdValueProperty._objectIdReader);
 }

 @Test
 public void testConstructor() throws IOException {
 ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(
 objectIdValueProperty, mockIdDeserializer);
 assertEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals(mockIdDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeprecatedConstructors() throws IOException {
 ObjectIdValueProperty newObjectIdValueProperty1 = new ObjectIdValueProperty(
 objectIdValueProperty, new PropertyName("testName"));
 ObjectIdValueProperty newObjectIdValueProperty2 = new ObjectIdValueProperty(
 objectIdValueProperty, new PropertyName("testName"), new PropertyName("testName2"));
 }

 @Test
 public void testWithName() throws IOException {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
 .withName(new PropertyName("testName"));
 assertEquals(new PropertyName("testName"), newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
 .withValueDeserializer(mockIdPropertyDeserializer);
 assertEquals(mockIdPropertyDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() throws IOException {
 assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
 }

 @Test
 public void testGetMember() throws IOException {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = Mockito.mock(Object.class);
 Mockito.when(mockObjectIdReader.resolveId(Mockito.eq(id), Mockito.eq(idType), Mockito.eq(idResolver)))
 .thenReturn(id);
 Mockito.when(mockIdDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext)))
 .thenReturn(id);
 Mockito.when(mockIdPropertyDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext), Mockito.eq(instance)))
 .thenReturn(id);

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, instance);
 assertEquals(id, instance);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = Mockito.mock(Object.class);
 Mockito.when(mockObjectIdReader.resolveId(Mockito.eq(id), Mockito.eq(idType), Mockito.eq(idResolver)))
 .thenReturn(id);
 Mockito.when(mockValueDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext)))
 .thenReturn(id);
 Mockito.when(mockIdDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext)))
 .thenReturn(id);
 Mockito.when(mockIdPropertyDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext), Mockito.eq(instance)))
 .thenReturn(id);
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext, instance);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = Mockito.mock(Object.class);
 Mockito.when(mockIdPropertyDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext), Mockito.eq(instance)))
 .thenReturn(id);
 objectIdValueProperty.set(instance, id);
 assertEquals(id, instance);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = Mockito.mock(Object.class);
 Mockito.when(mockIdPropertyDeserializer.deserialize(Mockito.eq(mockJsonParser), Mockito.eq(mockContext), Mockito.eq(instance)))
 .thenReturn(id);
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(id, result);
 }

}