package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private JsonDeserializer<String> stringDeserializer;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 stringDeserializer = mapper.getFactory().createValueDelegate(String.class);
 objectMapper = new ObjectMapper();
 objectIdReader = new ObjectIdReader(new PropertyName("id"), stringDeserializer);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 // Test for constructor
 @Test
 public void testConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNotNull(objectIdValueProperty);
 }

 // Test for getters
 @Test
 public void testGetters() {
 assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
 }

 // Test for deserializeAndSet method
 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = mapper.getDeserializationContext();
 JsonParser parser = mapper.getFactory().createParser("{\"id\":\"" + id + "\"}");

 objectIdValueProperty.deserializeAndSet(parser, context, testInstance);
 ObjectIdReader objectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 Object idObject = objectIdReader.readValue(parser, ObjectIdReader.LocalObjectId.class);
 assertEquals(id, idObject.getId());

 // Test with null id
 parser = mapper.getFactory().createParser("{}");
 objectIdValueProperty.deserializeAndSet(parser, context, testInstance);
 assertNull(idObject);
 }

 // Test for deserializeSetAndReturn method
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = mapper.getDeserializationContext();
 JsonParser parser = mapper.getFactory().createParser("{\"id\":\"" + id + "\"}");
 ObjectIdReader objectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 Object idObject = objectIdReader.readValue(parser, ObjectIdReader.LocalObjectId.class);

 Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, testInstance);
 ObjectIdReader resultObjectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 Object resultIdObject = resultObjectIdReader.readValue(parser, ObjectIdReader.LocalObjectId.class);
 assertEquals(id, resultIdObject.getId());
 assertEquals(testInstance, result);
 }

 // Test for set method
 @Test
 public void testSet() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = mapper.getDeserializationContext();
 ObjectIdReader objectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 ObjectIdValueProperty settableBeanProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

 settableBeanProperty.set(testInstance, id);
 ObjectIdReader resultObjectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 Object resultIdObject = resultObjectIdReader.readValue(parser, ObjectIdReader.LocalObjectId.class);
 assertEquals(id, resultIdObject.getId());
 }

 // Test for setAndReturn method
 @Test
 public void testSetAndReturn() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = mapper.getDeserializationContext();
 ObjectIdReader objectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 ObjectIdValueProperty settableBeanProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

 assertNull(settableBeanProperty.get(testInstance));
 Object result = settableBeanProperty.setAndReturn(testInstance, id);
 ObjectIdReader resultObjectIdReader = context.findObjectIdReader(ObjectIdValueProperty.class);
 Object resultIdObject = resultObjectIdReader.readValue(parser, ObjectIdReader.LocalObjectId.class);
 assertEquals(id, resultIdObject.getId());
 assertSame(testInstance, result);
 }

 // Test for withName method
 @Test
 public void testWithName() {
 PropertyName newName = new PropertyName("newName");
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
 assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
 assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
 assertSame(newName, newObjectIdValueProperty.getName());
 }

 // Test for withValueDeserializer method
 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> newDeserializer = new JsonDeserializer<String>() {};
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
 assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
 assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
 assertSame(newDeserializer, newObjectIdValueProperty._valueDeserializer);
 }
}