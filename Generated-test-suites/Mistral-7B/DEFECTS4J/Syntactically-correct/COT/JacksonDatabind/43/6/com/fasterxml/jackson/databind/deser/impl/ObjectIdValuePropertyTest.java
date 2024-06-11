package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() throws InstantiationException, IllegalAccessException {
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public ObjectIdGenerator generator() {
 return null; // not used in this test
 }

 @Override
 public ObjectIdResolver resolver() {
 return null; // not used in this test
 }
 };

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertEquals(objectIdValueProperty.getName(), obj.getName());
 assertEquals(objectIdValueProperty.getIdType(), obj.getIdType());
 assertEquals(objectIdValueProperty.getMetadata(), obj.getMetadata());
 assertEquals(objectIdValueProperty.getValueDeserializer(), obj.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(null));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetWithoutIdProperty() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testDeserializeAndSetWithNullId() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), null);
 assertNull(testInstance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithInvalidId() throws IOException {
 Object testInstance = new Object();
 when(mockJsonParser().getValueAsString()).thenReturn("invalidId");
 objectIdValueProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance);
 fail("Should throw IOException");
 }

 @Test
 public void testDeserializeAndSetWithValidId() throws IOException {
 Object testInstance = new Object();
 when(mockJsonParser().getValueAsString()).thenReturn("123");
 ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = mock(ObjectIdResolver.class);
 ObjectId id = mock(ReadableObjectId.class);
 when(id.getIdValue()).thenReturn(123L);
 when(id.bindItem(testInstance)).thenReturn(testInstance);
 when(mockDeserializationContext().findObjectId(eq(123L), eq(generator), eq(resolver))).thenReturn(id);
 Object result = objectIdValueProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance);
 assertEquals(testInstance, result);
 }

 @Test
 public void testDeserializeSetAndReturnWithNullId() throws IOException {
 Object testInstance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser(), mockDeserializationContext(), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithInvalidId() throws IOException {
 Object testInstance = new Object();
 when(mockJsonParser().getValueAsString()).thenReturn("invalidId");
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser(), mockDeserializationContext(), testInstance);
 fail("Should throw IOException");
 }

 @Test
 public void testDeserializeSetAndReturnWithValidId() throws IOException {
 Object testInstance = new Object();
 when(mockJsonParser().getValueAsString()).thenReturn("123");
 ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = mock(ObjectIdResolver.class);
 ObjectId id = mock(ReadableObjectId.class);
 when(id.getIdValue()).thenReturn(123L);
 when(id.bindItem(testInstance)).thenReturn(testInstance);
 when(mockDeserializationContext().findObjectId(eq(123L), eq(generator), eq(resolver))).thenReturn(id);
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser(), mockDeserializationContext(), testInstance);
 assertEquals(testInstance, result);
 }

 @Test
 public void testSetWithNullValue() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.set(testInstance, null);
 fail("Should not set null value");
 }

 @Test
 public void testSetWithValidValue() throws IOException {
 Object testInstance = new Object();
 ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = mock(ObjectIdResolver.class);
 ObjectId id = mock(ReadableObjectId.class);
 when(id.getIdValue()).thenReturn(123L);
 when(mockDeserializationContext().findObjectId(eq(123L), eq(generator), eq(resolver))).thenReturn(id);
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 doAnswer(invocation -> idProperty.setAndReturn(testInstance, null)).when(idProperty).setAndReturn(testInstance, anyObject());
 Object result = objectIdValueProperty.setAndReturn(testInstance, "123");
 assertEquals(testInstance, result);
 }
}