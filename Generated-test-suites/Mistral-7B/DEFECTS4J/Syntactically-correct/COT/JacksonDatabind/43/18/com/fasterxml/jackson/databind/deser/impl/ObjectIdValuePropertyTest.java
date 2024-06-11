package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws JsonProcessingException {
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public ObjectIdReader.IdType idType() {
 return ObjectIdReader.IdType.INT;
 }

 @Override
 public JsonDeserializer<ObjectId> getIdDeserializer() {
 return null;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, new JsonDeserializer<>());
 assertEquals(objectIdValueProperty.propertyName(), obj.propertyName());
 assertEquals(objectIdValueProperty._objectIdReader, obj._objectIdReader);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty obj = objectIdValueProperty.withName(PropertyName.construct("newName"));
 assertEquals(PropertyName.construct("newName"), obj.propertyName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
 ObjectIdValueProperty obj = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, obj.getValueDeserializer());
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
 Object id = 1L;
 Object idInstance = new Object();
 JsonParser parser = new MockJsonParser();
 parser.setValue(id.toString());
 Object result = objectIdValueProperty.deserializeAndSet(parser, new TestContext(), idInstance);
 assertEquals(id, result);
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 Object id = 1L;
 Object idInstance = new Object();
 JsonParser parser = new MockJsonParser();
 parser.setValue(id.toString());
 Object result = objectIdValueProperty.deserializeSetAndReturn(parser, new TestContext(), idInstance);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws JsonProcessingException {
 Object id = 1L;
 Object idInstance = new Object();
 objectIdValueProperty.set(idInstance, id);
 assertEquals(id, idInstance);
 }

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 Object id = 1L;
 Object idInstance = new Object();
 Object result = objectIdValueProperty.setAndReturn(idInstance, id);
 assertEquals(id, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws JsonProcessingException {
 Object idInstance = new Object();
 objectIdValueProperty.set(idInstance, " test string ");
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }
}