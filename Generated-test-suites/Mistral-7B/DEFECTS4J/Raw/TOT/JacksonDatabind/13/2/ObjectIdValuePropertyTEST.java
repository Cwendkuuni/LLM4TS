package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;
 private JsonDeserializer<Object> objectDeserializer;
 private JsonFactory jsonFactory;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectNode node = JsonFactory.instance.createObjectNode();
 objectIdReader = new ObjectIdReaderForString();
 AnnotatedMember member = new AnnotatedMember(node.fields().findValue("id"), new Annotation[0]);
 ObjectIdValueProperty.PropertyMetadata metadata = new ObjectIdValueProperty.PropertyMetadata();
 property = new ObjectIdValueProperty(objectIdReader, metadata);
 property.setName("id");
 objectDeserializer = new StandardDeserializer(String.class);
 jsonFactory = new JsonFactory();
 }

 @Test
 public void testDefaultConstruction() {
 ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
 assertNull(prop.getIdType());
 assertNull(prop.getDeserializer());
 assertNull(prop.getMember());
 assertNull(prop.getName());
 assertNull(prop._objectIdReader);
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", 1L);
 ObjectReader reader = jsonFactory.getObjectReader();
 ObjectIdValueProperty prop = reader.findProperty("id", ObjectIdValueProperty.class);
 Object id = prop.getValue();
 assertEquals(1L, id);
 }

 @Test
 public void testDeserialization() throws JsonProcessingException {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, property);
 Object deserializedId = prop.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
assertEquals("123", deserializedId.toString());
}

 @Test
 public void testNullDeserialization() throws JsonProcessingException {
 ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, property);
 Object deserializedId = prop.deserializeAndSet(jsonFactory.createParser("null"), null, new Object());
 assertNull(deserializedId);
}

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 Object id = new Object();
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", id);
 ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, property);
 Object deserializedId = prop.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertSame(id, deserializedId);
 Object deserializedInstance = prop.setAndReturn(new Object(), id);
 assertSame(id, deserializedInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws JsonProcessingException {
 ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, property);
 prop.set(new Object(), new Object());
 fail("Should not be able to call set() on ObjectIdValueProperty without an idProperty");
 }

 @Test
 public void testAnnotation() {
 assertNull(property.getAnnotation(ObjectIdAnnotation.class));
 }

 @Test
 public void testSetValueDeserializer() {
 Object valueDeserializer = new StdScalarValueDeserializer(String.class);
 property.setValueDeserializer(valueDeserializer);
 assertEquals(valueDeserializer, property.getValueDeserializer());
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
 assertEquals("newName", newProperty.getName().getName());
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testGetAnnotation() {
 AnnotationIntrospector introspector = new StandardAnnotationIntrospector();
 ObjectIdAnnotation annotation = introspector.findAnnotation(property, ObjectIdAnnotation.class);
 assertNotNull(annotation);
 }

 @Test
 public void testIdPropertySet() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 assertEquals(idProperty, property.getIdProperty());
 }

 @Test
 public void testDeserializeAndSetWithIdProperty() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 Object deserializedId = property.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertEquals("123", deserializedId.toString());
 Object deserializedInstance = property.setAndReturn(new Object(), deserializedId);
 assertSame(deserializedId, deserializedInstance);
 }

 @Test
 public void testDeserializeAndSetWithNullId() throws JsonProcessingException {
 ObjectNode node = jsonFactory.createObjectNode();
 Object deserializedInstance = property.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertNull(deserializedInstance);
 }

 @Test
 public void testSetAndReturnWithNullId() throws JsonProcessingException {
 Object deserializedInstance = property.setAndReturn(new Object(), null);
 assertNull(deserializedInstance);
 }

 @Test
 public void testSetAndReturnWithIdProperty() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 Object id = new Object();
 Object deserializedInstance = property.setAndReturn(new Object(), id);
 assertEquals(id, deserializedInstance);
 }

 @Test
 public void testDeserializeAndSetWithIdPropertyAndValueDeserializer() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 ObjectDeserializer valueDeserializer = new StdScalarValueDeserializer(String.class);
 property.setValueDeserializer(valueDeserializer);
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 Object deserializedId = property.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertEquals("123", deserializedId.toString());
 Object deserializedInstance = property.setAndReturn(new Object(), deserializedId);
 assertSame(deserializedId, deserializedInstance);
 }

 @Test
 public void testSetAndReturnWithIdPropertyAndValueDeserializerAndTypeDeserializer() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 ObjectDeserializer valueDeserializer = new StdScalarValueDeserializer(String.class);
 property.setValueDeserializer(valueDeserializer);
 TypeDeserializer typeDeserializer = new StdClassTypeDeserializer(LongTypeDescriptor.instance, valueDeserializer);
 property.setTypeDeserializer(typeDeserializer);
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 Object deserializedId = property.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertEquals("123", deserializedId.toString());
 Object deserializedInstance = property.setAndReturn(new Object(), deserializedId);
 assertEquals(123L, deserializedInstance.hashCode());
 }

 @Test
 public void testDeserializeAndSetWithIdPropertyAndValueDeserializerAndTypeDeserializerAndAnnotation() throws JsonProcessingException {
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, property);
 property.setIdProperty(idProperty);
 ObjectDeserializer valueDeserializer = new StdScalarValueDeserializer(String.class);
 property.setValueDeserializer(valueDeserializer);
 TypeDeserializer typeDeserializer = new StdClassTypeDeserializer(LongTypeDescriptor.instance, valueDeserializer);
 property.setTypeDeserializer(typeDeserializer);
 ObjectIdAnnotation annotation = new ObjectIdAnnotation();
 property.setAnnotation(annotation);
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 Object deserializedId = property.deserializeAndSet(jsonFactory.createParser(node.textValue()), null, new Object());
 assertEquals("123", deserializedId.toString());
 Object deserializedInstance = property.setAndReturn(new Object(), deserializedId);
 assertEquals(123L, deserializedInstance.hashCode());
 }
}