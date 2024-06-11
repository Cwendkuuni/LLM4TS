package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private JsonDeserializer<ObjectIdNode> mockValueDeserializer;
 private SettableBeanProperty mockIdProperty;
 private ObjectIdValueProperty objectIdValueProperty;
 private Object id;

 @Before
 public void setUp() {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 mockIdProperty = Mockito.mock(SettableBeanProperty.class);
 id = new ObjectIdNode("123");
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertEquals(mockObjectIdReader.propertyName, obj.getName());
 assertEquals(mockObjectIdReader.getIdType(), obj.getType());
 assertNull(obj.getDeserializer());
 assertNull(obj.getNullProvider());
 assertNull(obj.getIdProperty());
 }

 @Test
 public void testConstructorWithDeserializer() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, mockValueDeserializer, null);
 assertEquals(mockObjectIdReader.propertyName, obj.getName());
 assertEquals(mockObjectIdReader.getIdType(), obj.getType());
 assertEquals(mockValueDeserializer, obj.getDeserializer());
 assertNull(obj.getNullProvider());
 assertNull(obj.getIdProperty());
 }

 @Test
 public void testConstructorWithNullProvider() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null, Mockito.mock(NullValueProvider.class));
 assertEquals(mockObjectIdReader.propertyName, obj.getName());
 assertEquals(mockObjectIdReader.getIdType(), obj.getType());
 assertNull(obj.getDeserializer());
 assertEquals(Mockito.mock(NullValueProvider.class), obj.getNullProvider());
 assertNull(obj.getIdProperty());
 }

 @Test
 public void testConstructorWithIdProperty() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null, null, mockIdProperty);
 assertEquals(mockObjectIdReader.propertyName, obj.getName());
 assertEquals(mockObjectIdReader.getIdType(), obj.getType());
 assertNull(obj.getDeserializer());
 assertNull(obj.getNullProvider());
 assertEquals(mockIdProperty, obj.getIdProperty());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 JsonParser p = Mockito.mock(JsonParser.class);
 DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();

 Mockito.when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(p.getValueAsString()).thenReturn("123");

 Mockito.when(ctxt.findObjectId(eq(id), eq(Mockito.any(ObjectIdGenerator.class)), eq(Mockito.any(ObjectIdResolver.class))))
         .thenReturn(Mockito.mock(ReadableObjectId.class));

 objectIdValueProperty.deserializeAndSet(p, ctxt, instance);

 assertEquals(id, mockIdProperty.get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNull() throws Exception {
 Object instance = new Object();
 objectIdValueProperty.set(instance, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullWithIdProperty() throws Exception {
 Object instance = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null, null, mockIdProperty);
 objectIdValueProperty.set(instance, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullIdProperty() throws Exception {
 Object instance = new Object();
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 obj.setIdProperty(null);
 obj.set(instance, id);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullValueDeserializer() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null, null, mockIdProperty);
 obj.setDeserializer(null);
 obj.deserializeAndSet(p, ctxt, instance);
 }

 @Test(expected = NullPointerException.class)
 public void testSetWithNullInstance() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 obj.set(null, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidIdType() throws Exception {
 when(mockObjectIdReader.getIdType()).thenReturn(Integer.class);
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 obj.set(new Object(), new ObjectIdNode("123"));
 }

 @Test
 public void testGetAnnotation() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertNull(obj.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertNull(obj.getMember());
 }
}