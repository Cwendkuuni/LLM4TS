package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 ObjectIdReader idReader = new ObjectIdReader() {
 @Override
 public PropertyName getPropertyName() {
 return new PropertyName("id");
 }

 @Override
 public Class<?> getIdType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new None();
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
 public BeanPropertyDefinition idProperty() {
 return null;
 }
 };
 objectIdReader = new ObjectIdReader(new PropertyName("objectId"), idReader);
 }

 @Test
 public void testObjectIdValueProperty() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertEquals("objectId", property.getName());
 assertEquals(Long.class, property.getType());
 assertNull(property.getMember());
 assertNull(property.getValueDeserializer());

 // test getAnnotation
 AnnotatedMember member = AnnotationIntrospector.findPropertyAnnotation(property, ObjectId.class);
 assertNull(member);

 // test deserializeAndSet
 Object id = new Long(1L);
 JsonParser parser = objectMapper.getFactory().createParser("{\"id\": " + id + "}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object instance = new Object();
 property.deserializeAndSet(parser, context, instance);
 assertNotNull(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver));

 // test set and setAndReturn
 Long value = new Long(2L);
 property.setAndReturn(instance, value);
 assertEquals(value, context.findObjectId(value, objectIdReader.generator, objectIdReader.resolver).getId());

 // test deserializeSetAndReturn
 Object deserializedId = property.deserializeSetAndReturn(parser, context, instance);
 assertEquals(id, deserializedId);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetIsNull() throws IOException {
 Object instance = new Object();
 property.set(instance, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetValueDeserializerIsNull() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null, new None());
 property.setValueDeserializer(null);
 }
}