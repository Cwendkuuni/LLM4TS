package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdIdentifier;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {};
 private static final ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

 @Test
 public void testConstructor() {
 ObjectIdValueProperty actual = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(property.getName(), actual.getName());
 assertEquals(property.getIdType(), actual.getIdType());
 assertNull(actual.getValueDeserializer());
 assertNull(actual.getNullProvider());
 assertSame(objectIdReader, actual._objectIdReader);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty actual = property.withName(new PropertyName("newName"));
 assertEquals("newName", actual.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> deserializer = new StandardDeserializer<>();
 ObjectIdValueProperty actual = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, actual.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = new StandardNullValueProvider();
 ObjectIdValueProperty actual = property.withNullProvider(nva);
 assertEquals(nva, actual.getNullProvider());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetAnnotationThrowsException() {
 Class<Annotation> annotationType = ObjectIdValueProperty.class;
 ObjectIdValueProperty actual = property;
 try {
 Annotation actualAnnotation = actual.getAnnotation(annotationType);
 fail("Should not be able to get annotation from ObjectIdValueProperty");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMemberThrowsException() {
 ObjectIdValueProperty actual = property;
 AnnotatedMember actualMember = actual.getMember();
 fail("Should not be able to get member from ObjectIdValueProperty");
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public ObjectIdValueProperty withNullProvider(NullValueProvider nva) {
 return new ObjectIdValueProperty(super.withNullProvider(nva), null);
 }
 };
 JsonParser p = mock(JsonParser.class);
 when(p.hasToken(eq(JsonToken.VALUE_NULL))).thenReturn(true);
 property.deserializeAndSet(p, new DeserializationContext() {}, instance);
 assertNull(instance);
 }

 @Test
 public void testDeserializeAndSetNonNull() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public ObjectIdValueProperty withNullProvider(NullValueProvider nva) {
 return new ObjectIdValueProperty(super.withNullProvider(nva), null);
 }
 };
 ObjectIdIdentifier id = new ObjectIdIdentifier("123");
 JsonParser p = mock(JsonParser.class);
 when(p.hasToken(eq(JsonToken.VALUE_STRING))).thenReturn(true);
 when(p.getValueAsString()).thenReturn(id.toString());
 property.deserializeAndSet(p, new DeserializationContext() {}, instance);
 ObjectIdIdentifier expectedId = new ObjectIdIdentifier("123");
 ObjectIdIdentifier actualId = ctxt.findObjectId(expectedId, objectIdReader.generator, objectIdReader.resolver);
 assertEquals(expectedId, actualId);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetThrowsException() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 throw new UnsupportedOperationException("Should not be able to call idProperty() on ObjectIdValueProperty");
 }
 };
 property.set(instance, new Object());
 fail("Should throw UnsupportedOperationException when calling set() on ObjectIdValueProperty");
 }

 @Test
 public void testSetAndReturnNull() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return new StdSetterBeanProperty(new PropertyName("id"), null, new PropertyMetadata(), null);
 }
 };
 property.setAndReturn(instance, null);
 assertNull(instance);
 }

 @Test
 public void testSetAndReturnNotNull() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return new StdSetterBeanProperty(new PropertyName("id"), null, new PropertyMetadata(), null);
 }
 };
 ObjectIdIdentifier id = new ObjectIdIdentifier("123");
 property.setAndReturn(instance, id);
 ObjectIdIdentifier expectedId = new ObjectIdIdentifier("123");
 ObjectIdIdentifier actualId = ctxt.findObjectId(expectedId, objectIdReader.generator, objectIdReader.resolver);
 assertEquals(expectedId, actualId);
 }
}