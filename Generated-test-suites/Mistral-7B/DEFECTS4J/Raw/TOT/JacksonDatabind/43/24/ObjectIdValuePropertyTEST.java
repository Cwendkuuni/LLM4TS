package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
  @Override
  public IdType getIdType() {
   return IdType.LONG;
  }

  @Override
  public ObjectIdGenerator getIdGenerator() {
   return new SimpleObjectIdGenerator();
  }

  @Override
  public ObjectIdResolver getIdResolver() {
   return new SimpleObjectIdResolver();
  }

  @Override
  public PropertyName getPropertyName() {
   return PropertyName.construct("id");
  }
 };

 @SuppressWarnings("unchecked")
 private ObjectIdValueProperty createObjectIdProperty(JsonDeserializer<?> deserializer) {
  return new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);
 }

 @Test
 public void testObjectIdValuePropertyCreation() {
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(nullDeserializer);
  assertNotNull(objectIdProperty);
 }

 @Test
 public void testObjectIdValuePropertySerialization() throws JsonProcessingException {
  JsonDeserializer<String> stringDeserializer = new StringDeserializer();
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(stringDeserializer);

  ObjectMapper mapper = new ObjectMapper();
  String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectIdProperty);
  ObjectIdValueProperty deserializedObjectIdProperty = mapper.readValue(json, ObjectIdValueProperty.class);
  assertEquals(objectIdProperty, deserializedObjectIdProperty);
 }

 @Test
 public void testNullIdDeserialization() throws IOException {
  JsonDeserializer<String> stringDeserializer = new StringDeserializer();
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(stringDeserializer);

  Object id = null;
  Object target = new Object();

  objectIdProperty.deserializeAndSet(mockParser(), mockContext(), target);
  assertNull(objectIdProperty.getMember().get(target));
 }

 @Test
 public void testValidIdDeserialization() throws IOException {
  JsonDeserializer<String> stringDeserializer = new StringDeserializer();
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(stringDeserializer);

  Object id = "123";
  Object target = new Object();

  objectIdProperty.deserializeAndSet(mockParser(), mockContext(), target);
  assertEquals(id, objectIdProperty.getMember().get(target));
 }

 @Test
 public void testInvalidIdDeserialization() throws IOException {
  JsonDeserializer<String> stringDeserializer = new StringDeserializer();
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(stringDeserializer);

  ObjectParser parser = mockParser();
  parser.nextValue("invalidId");
  doThrow(new IOException("Invalid Id")).when(parser).getValueAsLong();

  Object target = new Object();

  try {
   objectIdProperty.deserializeAndSet(parser, mockContext(), target);
   fail("Expected IOException");
  } catch (IOException e) {
   assertEquals("Invalid Id", e.getMessage());
  }
 }

 @Test
 public void testIdPropertySetAndGet() throws IOException {
  JsonDeserializer<String> stringDeserializer = new StringDeserializer();
  ObjectIdValueProperty objectIdProperty = createObjectIdProperty(stringDeserializer);
  ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdProperty, PropertyName.construct("idProperty"));

  Object target = new Object();
  String id = "456";
  idProperty.setAndReturn(target, id);
  assertEquals(id, objectIdProperty.getMember().get(target));
 }

 @Test
 public void testIdPropertySetAndGetWithNullDeserializer() throws IOException {
  ObjectIdValueProperty idProperty = new ObjectIdValueProperty(null, PropertyName.construct("idProperty"));

  Object target = new Object();
  String id = "789";
  idProperty.setAndReturn(target, id);
  assertNull(objectIdProperty.getMember().get(target));
 }

 private JsonParser mockParser() {
  ObjectMocker mocker = new ObjectMocker();
  return mocker.createMock(JsonParser.class);
 }

 private DeserializationContext mockContext() {
  ObjectMocker mocker = new ObjectMocker();
  return mocker.createMock(DeserializationContext.class);
 }
}