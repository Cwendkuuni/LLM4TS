package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.StdObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static final ObjectIdReader objectIdReader = new StdObjectIdReader() {
 @Override public Class<Object> getIdType() { return Object.class; }
 @Override public JsonDeserializer<Object> getIdDeserializer() { return null; }
 @Override public ObjectIdGenerator idGenerator() { return null; }
 @Override public ObjectIdResolver idResolver() { return null; }
 @Override public String propertyName() { return "objectId"; }
 @Override public SettableBeanProperty idProperty() { return null; }
 };

 @SuppressWarnings("unchecked")
 private static final ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null) {
 @SuppressWarnings("rawtypes")
 @Override public JsonDeserializer deserializer() { return new StdSerializer<>(Integer.class) {}; }
 };

 @SuppressWarnings("rawtypes")
 private static final SettableBeanProperty idProperty = new StdSettableBeanProperty("id", Integer.class);

 @org.junit.Test
 public void testDeserializeAndSetNullId() throws IOException, JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 JsonParser parser = mapper.getFactory().createParser(node.getOutputStream());
 Object testInstance = new Object();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), testInstance);
 assertNull(property.get(testInstance));
 }

 @org.junit.Test
 public void testDeserializeAndSetValidId() throws IOException, JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("objectId", 123L);
 JsonParser parser = mapper.getFactory().createParser(node.getOutputStream());
 Object testInstance = new Object();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), testInstance);
 assertEquals(123L, property.get(testInstance));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetNullValue() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 property.set(testInstance, null);
 fail("Should not be able to set null value to ObjectIdValueProperty");
 }

 @org.junit.Test
 public void testSetValidValue() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 property.set(testInstance, 123L);
 assertEquals(123L, property.get(testInstance));
 }

 @org.junit.Test
 public void testSetValueWithIdProperty() throws IOException, JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("objectId", 123L);
 JsonParser parser = mapper.getFactory().createParser(node.getOutputStream());
 Object testInstance = new Object();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 ObjectIdValueProperty idPropertyTest = new ObjectIdValueProperty(property, null) {
 @Override public JsonDeserializer deserializer() { return ObjectIdValuePropertyTest.idProperty.deserializer(); }
 };
 idPropertyTest.set(testInstance, 456L);
 assertEquals(456L, property.get(testInstance));
 }

 @org.junit.Test
 public void testAnnotation() {
 AnnotationIntrospector introspector = new AnnotationIntrospector();
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 Annotation annotation = introspector.findAnnotation(property, ObjectIdReader.class);
 assertNull(annotation);
 }

 @org.junit.Test
 public void testToString() {
 ObjectIdValueProperty property = ObjectIdValuePropertyTest.objectIdValueProperty;
 assertEquals("ObjectIdValueProperty[name='objectId', getter=[class com.fasterxml.jackson.databind.ser.std.StdSerializer$1 at com.fasterxml.jackson.databind.ser.std.StdSerializer.java:336], setter=[class com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty at com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty.java:50]]", property.toString());
 }
}