package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;

import org.junit.Test;

/**
 * Test cases for ObjectIdValueProperty class
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 ObjectIdGenerator idGenerator = new ObjectIdGenerator();
 ObjectIdResolver idResolver = new ObjectIdResolver();
 objectIdReader = new ObjectIdReader("testProperty", idGenerator, idResolver);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new SimplePropertyMetadata());
 }

 // Test constructor
 @Test
 public void testObjectIdValuePropertyConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertEquals(objectIdValueProperty.getName(), obj.getName());
 assertEquals(objectIdValueProperty.getIdType(), obj.getIdType());
 assertEquals(objectIdValueProperty.getMetadata(), obj.getMetadata());
 assertEquals(objectIdValueProperty.getValueDeserializer(), obj.getValueDeserializer());
 assertEquals(objectIdValueProperty._objectIdReader, obj._objectIdReader);
 }

 // Test withName method
 @Test
 public void testWithName() {
 ObjectIdValueProperty newObj = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newObj.getName().asString());
 }

 // Test withValueDeserializer method
 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
 ObjectIdValueProperty newObj = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, newObj.getValueDeserializer());
 }

 // Test deserializeAndSet method
 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testInstance = new Object();
 JsonParser mockParser = new JsonTestParser();
 Object id = "testId";
 mockParser.setValue(JsonParser.Feature.ALLOW_EMPTY_STRING_AS_NULL, true);
 mockParser.setValue("id", id);
 objectIdValueProperty.deserializeAndSet(mockParser, new DeserializationContext(), testInstance);
 ObjectId idObj = new ObjectId(id);
 ObjectIdResolver idResolver = new ObjectIdResolver();
 ObjectIdGenerator idGenerator = new ObjectIdGenerator();
 assertEquals(idObj, idResolver.findObjectId(id, idGenerator, null));
 }

 // Test deserializeSetAndReturn method
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testInstance = new Object();
 JsonParser mockParser = new JsonTestParser();
 Object id = "testId";
 mockParser.setValue(JsonParser.Feature.ALLOW_EMPTY_STRING_AS_NULL, true);
 mockParser.setValue("id", id);
 Object returnedObject = objectIdValueProperty.deserializeSetAndReturn(mockParser, new DeserializationContext(), testInstance);
 ObjectId idObj = new ObjectId(id);
 ObjectIdResolver idResolver = new ObjectIdResolver();
 ObjectIdGenerator idGenerator = new ObjectIdGenerator();
 assertEquals(idObj, idResolver.findObjectId(id, idGenerator, null));
 assertEquals(testInstance, returnedObject);
 }

 // Test set method
 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 Object testInstance = new Object();
 ObjectIdValueProperty newObj = new ObjectIdValueProperty(objectIdValueProperty, null);
 newObj.set(testInstance, null);
 fail("Expected UnsupportedOperationException when calling set() on ObjectIdProperty");
 }

 // Test setAndReturn method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 Object testInstance = new Object();
 ObjectIdValueProperty newObj = new ObjectIdValueProperty(objectIdValueProperty, null);
 Object id = "testId";
 newObj.setAndReturn(testInstance, id);
 fail("Expected UnsupportedOperationException when calling setAndReturn() on ObjectIdProperty");
 }

 // Test getAnnotation method
 @Test
 public void testGetAnnotation() {
 Class<Annotation> annotationType = Annotation.class;
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertNull(obj.getAnnotation(annotationType));
 }

 // Test getMember method
 @Test
 public void testGetMember() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertNull(obj.getMember());
 }
}