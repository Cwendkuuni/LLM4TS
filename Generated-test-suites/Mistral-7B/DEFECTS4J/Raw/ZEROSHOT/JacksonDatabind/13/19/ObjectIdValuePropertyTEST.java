package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader.Builder builder = new ObjectIdReader.StdBuilder();
 objectIdReader = builder.build(null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test
 public void testConstructor() {
 assertNotNull(objectIdValueProperty);
 assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertNull(objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getMember());
 assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty property = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals("newName", property.getName().getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
 ObjectIdValueProperty property = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = ObjectIdNode.VALUE_STRING.getValueAsString();
 Object testInstance = new Object();
 JsonParser jsonParser = new TestJsonParser();
 jsonParser.setValue(id);
 objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), testInstance);
 assertNotNull(testInstance);
 // Check if the object id has been set
 ObjectId idPropValue = testInstance.getClass().getDeclaredField("id").get(testInstance);
 assertEquals(id, idPropValue);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object testInstance = new Object();
 Object id = ObjectIdNode.VALUE_STRING.getValueAsString();
 ObjectIdValueProperty.Setter setter = new ObjectIdValueProperty.Setter(testInstance, "id");
 setter.setAndReturn(id);
 assertNotNull(testInstance);
 // Check if the object id has been set
 ObjectId idPropValue = testInstance.getClass().getDeclaredField("id").get(testInstance);
 assertEquals(id, idPropValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullValue() throws Exception {
 Object testInstance = new Object();
 objectIdValueProperty.set(testInstance, null);
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = objectIdValueProperty.getAnnotation(ObjectId.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = objectIdValueProperty.getMember();
 assertNull(member);
 }
}