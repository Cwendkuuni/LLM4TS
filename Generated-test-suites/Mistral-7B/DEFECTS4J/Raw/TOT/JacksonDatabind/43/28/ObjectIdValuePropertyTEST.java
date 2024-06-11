package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdGenerator generator = new ObjectIdGenerator();
 private static final ObjectIdResolver resolver = new EmptyObjectIdResolver();

 @SuppressWarnings("unchecked")
 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
  @Override
  public ObjectIdGenerator getObjectIdGenerator() {
   return generator;
  }

  @Override
  public ObjectIdResolver getObjectIdResolver() {
   return resolver;
  }

  @Override
  public String getPropertyName() {
   return "objectId";
  }
 };

 @SuppressWarnings("unchecked")
 private static final SettableBeanProperty idProperty = new StdSerializer<String>(String.class) {
  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
   gen.writeString(value);
  }
 }.withValueDeserializer(new StdDeserializer<String>(String.class, ObjectIdValueProperty.class));

 private static final ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader,
         new PropertyMetadata());

 @Test
 public void testDeserializeAndSet() throws IOException {
  Object testInstance = new Object();
  JsonParser mockParser = createMockJsonParser();
  DeserializationContext mockContext = createMockDeserializationContext();

  when(mockParser.getValueAsToken()).thenReturn(mockContext.readValueAs(JsonToken.VALUE_STRING.getClass()));
  when(mockContext.findObjectId(anyObject(), eq(generator), eq(resolver))).thenReturn(new ReadableObjectId("testId"));

  property.deserializeAndSet(mockParser, mockContext, testInstance);

  verify(testInstance).setId("testId");
 }

 @Test
 public void testDeserializeAndSetNullId() throws IOException {
  Object testInstance = new Object();
  JsonParser mockParser = createMockJsonParser();
  DeserializationContext mockContext = createMockDeserializationContext();

  when(mockParser.getValueAsToken()).thenReturn(mockContext.readValueAs(JsonToken.VALUE_NULL.getClass()));

  property.deserializeAndSet(mockParser, mockContext, testInstance);

  verifyNoMoreInteractions(testInstance);
 }

 @Test
 public void testSetAndReturnNullObject() throws IOException {
  Object testInstance = new Object();
  Object testId = null;

  when(property.setAndReturn(testInstance, testId)).thenReturn(testInstance);

  assertNull(property.setAndReturn(testInstance, testId));
 }

 @Test
 public void testSetAndReturn() throws IOException {
  Object testInstance = new Object();
  Object testId = new Object();

  when(property.setAndReturn(testInstance, testId)).thenReturn(testInstance);

  assertEquals(testInstance, property.setAndReturn(testInstance, testId));
 }

 private JsonParser createMockJsonParser() {
  return mock(JsonParser.class);
 }

 private DeserializationContext createMockDeserializationContext() {
  return mock(DeserializationContext.class);
 }
}