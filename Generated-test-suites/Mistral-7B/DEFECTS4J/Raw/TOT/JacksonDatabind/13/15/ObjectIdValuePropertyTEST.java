package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty property;
 private ObjectIdReader objectIdReader;
 private JsonDeserializer<Object> deserializer;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectIdReader reader = new SimpleObjectIdReader("id");
 objectIdReader = new ObjectIdReader() {
 @Override
 public IdType getIdType() {
 return IdType.INT_VALUE;
 }

 @Override
 public String getPropertyName() {
 return "id";
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return new AlwaysNullObjectIdGenerator();
 }

 @Override
 public ObjectIdResolver idResolver() {
 return new AlwaysNullObjectIdResolver();
 }
 };
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 deserializer = new StdScalarDeserializer(String.class);
 }

 @Test
 public void testObjectIdValuePropertyCreation() {
 assertNotNull(property);
 assertEquals("id", property.getName());
 assertSame(IdType.INT_VALUE, property.getIdType());
 assertNull(property.getValueDeserializer());
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() throws IOException {
 ObjectIdReader reader = new SimpleObjectIdReader("id");
 ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata(), deserializer);
 assertNotNull(property);
 assertSame(reader, property._objectIdReader);
 assertSame(deserializer, property.getValueDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyWithName() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyName("newName"));
 fail("ObjectIdValueProperty should not allow changing name after creation.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyWithValueDeserializerName() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new String("newName"), deserializer);
 fail("ObjectIdValueProperty should not allow changing name after creation.");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String id = "123";
 JsonParser jp = new JsonFactory().createParser(ObjectMaps.CSV_PARSER, "{\"id\":\"" + id + "\"}");
 Object instance = new Object();
 property.deserializeAndSet(jp, new TestContext(), instance);
 assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String id = "123";
 Object instance = new Object();
 Object idValue = property.deserializeSetAndReturn(new TestJsonParser(), new TestContext(), instance);
 assertEquals(id, idValue);
 assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object id = property.setAndReturn(instance, "123");
 assertEquals("123", id);
 assertEquals("123", instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithoutIdProperty() throws IOException {
 Object instance = new Object();
 throw new UnsupportedOperationException(
 "Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }
}

class TestJsonParser extends JsonParser {
 @Override
 public Object deserializeValue(DeserializationContext ctxt, BeanPropertyPropertyType propertyType)
 throws IOException {
 return null;
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectIdGenerator idGenerator() {
 return new AlwaysNullObjectIdGenerator();
 }

 @Override
 public ObjectIdResolver idResolver() {
 return new AlwaysNullObjectIdResolver();
 }
}

class AlwaysNullObjectIdGenerator implements ObjectIdGenerator {
 @Override
 public Object generateId(SerializerProvider serializers) {
 return null;
 }
}

class AlwaysNullObjectIdResolver implements ObjectIdResolver {
 @Override
 public Object resolveId(DeserializationContext ctxt, BeanProperty property, Object id) throws IOException {
 return null;
 }
}