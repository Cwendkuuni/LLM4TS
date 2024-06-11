package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() {
 mockObjectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return new PropertyName("id");
 }

 @Override
 public Class<?> idType() {
 return ObjectIdNode.class;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null; // not used in this test
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null; // not used in this test
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return null; // not used in this test
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null; // not used in this test
 }
 };

 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 }

 // Test for constructor
 @Test
 public void testObjectIdValuePropertyConstructor() {
 ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertEquals(objectIdValueProperty.getName(), newObjectIdValueProperty.getName());
 assertEquals(objectIdValueProperty.getIdType(), newObjectIdValueProperty.getIdType());
 assertEquals(objectIdValueProperty.getMetadata(), newObjectIdValueProperty.getMetadata());
 assertEquals(objectIdValueProperty.getValueDeserializer(), newObjectIdValueProperty.getValueDeserializer());
 assertEquals(objectIdValueProperty._objectIdReader, newObjectIdValueProperty._objectIdReader);
 }

 // Test for getters
 @Test
 public void testGetName() {
 assertEquals(mockObjectIdReader.propertyName().getName(), objectIdValueProperty.getName().getName());
 }

 @Test
 public void testGetIdType() {
 assertEquals(mockObjectIdReader.idType(), objectIdValueProperty.getIdType());
 }

 @Test
 public void testGetMetadata() {
 assertNull(objectIdValueProperty.getMetadata());
 }

 @Test
 public void testGetValueDeserializer() {
 assertNull(objectIdValueProperty.getValueDeserializer());
 }

 // Test for deprecated constructors and methods
 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyDeprecatedConstructorWithNewName() {
 ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, "newName");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyDeprecatedConstructorWithNewNameAndDeserializer() {
 ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, "newName", null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyDeprecatedWithName() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName("newName");
 }

 // Test for methods that call idProperty
 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetWithoutIdProperty() throws JsonProcessingException {
 JsonParser mockJsonParser = createMockJsonParser();
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(mockJsonParser, null, instance);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeSetAndReturnWithoutIdProperty() throws JsonProcessingException {
 JsonParser mockJsonParser = createMockJsonParser();
 Object instance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, null, instance);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object instance = new Object();
 objectIdValueProperty.set(instance, null);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithoutIdProperty() throws IOException {
 Object instance = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, null);
 fail("Should throw UnsupportedOperationException");
 }

 // Test for deserialization methods with idProperty
 @Test
 public void testDeserializeAndSetWithIdProperty() throws JsonProcessingException {
 Object instance = new Object();
 ObjectIdNode idNode = ObjectIdNode.valueOf("123");
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 objectIdValueProperty.deserializeAndSet(mockJsonParser, null, instance);
 assertEquals(idNode.getValue(), objectIdValueProperty.getObjectId(instance));
 }

 @Test
 public void testDeserializeSetAndReturnWithIdProperty() throws JsonProcessingException {
 Object instance = new Object();
 ObjectIdNode idNode = ObjectIdNode.valueOf("123");
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, null, instance);
 assertEquals(idNode.getValue(), objectIdValueProperty.getObjectId(instance));
 assertEquals(instance, result);
 }

 // Test for set method with idProperty
 @Test
 public void testSetWithIdProperty() throws IOException {
 Object instance = new Object();
 ObjectIdNode idNode = ObjectIdNode.valueOf("123");
 objectIdValueProperty.setAndReturn(instance, idNode);
 assertEquals(idNode.getValue(), objectIdValueProperty.getObjectId(instance));
 }

 @Test
 public void testSetAndReturnWithIdProperty() throws IOException {
 Object instance = new Object();
 ObjectIdNode idNode = ObjectIdNode.valueOf("123");
 Object result = objectIdValueProperty.setAndReturn(instance, idNode);
 assertEquals(idNode.getValue(), objectIdValueProperty.getObjectId(instance));
 assertEquals(instance, result);
 }

 // Test for exception handling
 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullObject() throws JsonProcessingException {
 Object idNode = ObjectIdNode.valueOf("123");
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 objectIdValueProperty.deserializeAndSet(mockJsonParser, null, null);
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNullObject() throws JsonProcessingException {
 Object idNode = ObjectIdNode.valueOf("123");
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, null, null);
 fail("Should throw IOException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetWithNonObjectId() throws JsonProcessingException {
 Object idNode = new Object();
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(mockJsonParser, null, instance);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeSetAndReturnWithNonObjectId() throws JsonProcessingException {
 Object idNode = new Object();
 JsonParser mockJsonParser = createMockJsonParser(idNode.toString());
 Object instance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, null, instance);
 fail("Should throw UnsupportedOperationException");
 }
}