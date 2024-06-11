package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 ObjectIdGenerator generator = Mockito.mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = Mockito.mock(ObjectIdResolver.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = "testId";
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();
 Mockito.when(mockParser.getValueAsString()).thenReturn(id);

 objectIdValueProperty.deserializeAndSet(mockParser, mockContext, instance);

 Object expectedId = id;
 Object actualId = Mockito.when(objectIdReader.resolveId(eq(instance), anyObject()))
 .thenReturn(expectedId).thenReturn(expectedId).thenReturn(expectedId)
 .getMock().get(0);
 Mockito.verify(objectIdReader).resolveId(instance, mockContext.getCurrentContextValue());

 assertEquals(expectedId, instance.getClass().getDeclaredField("objectId")
 .get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = "testId";
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();
 JsonDeserializer<Object> mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 StdSerializer<Object> mockNullProvider = Mockito.mock(StdSerializer.class);
 Object expectedId = id;
 Object actualId = Mockito.when(objectIdReader.resolveId(eq(instance), anyObject()))
 .thenReturn(expectedId).thenReturn(expectedId).thenReturn(expectedId)
 .getMock().get(0);
 Mockito.when(mockParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 Mockito.when(mockParser.getValueAsString()).thenReturn(id);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockParser.getCurrentName()).thenReturn("objectId");
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(mockParser.getValueAsString()).thenReturn(id);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 Object actualResult = objectIdValueProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

 assertEquals(expectedId, actualResult);
 }

 @Test
 public void testSet() throws IOException {
 Object id = "testId";
 Object instance = new Object();
 instance.getClass().getDeclaredField("objectId").setAccessible(true);
 instance.getClass().getDeclaredField("objectId").set(instance, null);

 objectIdValueProperty.set(instance, id);

 Object expectedId = id;
 Object actualId = Mockito.when(objectIdReader.resolveId(eq(instance), anyObject()))
 .thenReturn(expectedId).thenReturn(expectedId).thenReturn(expectedId)
 .getMock().get(0);
 Mockito.verify(objectIdReader).resolveId(instance, Mockito.any());

 assertEquals(expectedId, instance.getClass().getDeclaredField("objectId")
 .get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = "testId";
 Object instance = new Object();
 instance.getClass().getDeclaredField("objectId").setAccessible(true);
 instance.getClass().getDeclaredField("objectId").set(instance, null);

 Object actualResult = objectIdValueProperty.setAndReturn(instance, id);

 Object expectedId = id;
 Object actualId = Mockito.when(objectIdReader.resolveId(eq(instance), anyObject()))
 .thenReturn(expectedId).thenReturn(expectedId).thenReturn(expectedId)
 .getMock().get(0);
 Mockito.verify(objectIdReader).resolveId(instance, Mockito.any());

 assertEquals(expectedId, actualResult);
 }
}