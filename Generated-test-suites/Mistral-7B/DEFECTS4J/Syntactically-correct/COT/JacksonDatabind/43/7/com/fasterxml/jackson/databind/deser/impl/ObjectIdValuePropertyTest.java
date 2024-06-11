package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private DeserializationContext deserializationContext;
 private JsonParser jsonParser;
 private TypeDeserializer typeDeserializer;

 @Before
 public void setup() throws IOException {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 jsonParser = Mockito.mock(JsonParser.class);
 typeDeserializer = new StdSerializer<Object>(Object.class) {};
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, null);
 assertEquals(ObjectIdValueProperty.class, obj.getClass());
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = objectIdValueProperty.getAnnotation(null);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMember = objectIdValueProperty.getMember();
 assertNull(annotatedMember);
 }

 @Test
 public void testDeserializeAndSet() throws IOException, JsonProcessingException {
 String id = "123";
 Mockito.when(jsonParser.getValueAsString()).thenReturn(id);
 Object obj = new Object();
 Object idObj = Mockito.mock(Object.class);
 Mockito.when(objectIdReader.getIdType()).thenReturn(Object.class);
 Mockito.when(objectIdReader.idProperty).thenReturn(new StdSerializer<Object>(Object.class) {});
 Object returnedObj = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, obj);
 assertEquals(obj, returnedObj);
 Mockito.verify(deserializationContext, Mockito.times(1)).findObjectId(id, Mockito.any(), Mockito.any());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
 String id = "123";
 Object idObj = Mockito.mock(Object.class);
 Object obj = new Object();
 Object expectedId = "expectedId";
 Mockito.when(jsonParser.getValueAsString()).thenReturn(id);
 Mockito.when(objectIdReader.getIdType()).thenReturn(Object.class);
 Mockito.when(objectIdReader.idProperty).thenReturn(new StdSerializer<Object>(Object.class) {});
 Mockito.when(objectIdValueProperty.deserialize(_jsonParser, deserializationContext)).thenReturn(expectedId);
 Object returnedObj = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj);
 assertEquals(expectedId, returnedObj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object obj = new Object();
 Object value = new Object();
 objectIdValueProperty.set(obj, value);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object obj = new Object();
 Object value = new Object();
 Object id = "123";
 Object expectedId = "expectedId";
 Mockito.when(jsonParser.getValueAsString()).thenReturn(id);
 Mockito.when(objectIdReader.getIdType()).thenReturn(Object.class);
 Mockito.when(objectIdReader.idProperty).thenReturn(new StdSerializer<Object>(Object.class) {});
 Mockito.when(objectIdValueProperty.deserialize(_jsonParser, deserializationContext)).thenReturn(expectedId);
 Object returnedObj = objectIdValueProperty.setAndReturn(obj, value);
 assertEquals(expectedId, returnedObj);
 }

 @Test
 public void testToString() {
 String toString = objectIdValueProperty.toString();
 assertTrue(toString.contains("ObjectIdValueProperty"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 new ObjectIdValueProperty(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithnullMetadata() {
 new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectIdReader() {
 new ObjectIdValueProperty(null, null);
 }

 @Test
 public void testDeserializeWithNullId() throws IOException, JsonProcessingException {
 Object obj = new Object();
 JsonNull jsonNull = new JsonNull();
 Mockito.when(jsonParser.getValueAsToken()).thenReturn(jsonNull);
 Object returnedObj = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, obj);
 assertNull(returnedObj);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 String id = "123";
 Object obj = new Object();
 Mockito.when(jsonParser.getValueAsString()).thenThrow(new IOException());
 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, obj);
 }
}