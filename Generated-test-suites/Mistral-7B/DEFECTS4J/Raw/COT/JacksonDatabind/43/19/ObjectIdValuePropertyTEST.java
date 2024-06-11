package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 objectIdReader = mock(ObjectIdReader.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 // Basic constructor test
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty objectIdValuePropertyWithName = objectIdValueProperty.withName(PropertyName.construct("testName"));
 assertEquals("testName", objectIdValuePropertyWithName.getName().getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), jsonDeserializer);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode("123");
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(Mockito.mock(Object.class));
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsToken()).thenReturn(objectIdNode);

 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), Mockito.mock(Object.class));
 assertEquals("123", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetNullId() throws IOException {
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode(null);
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsToken()).thenReturn(objectIdNode);

 objectIdValueProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), Mockito.mock(Object.class));
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws IOException {
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode(null);
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(Mockito.mock(Object.class));
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));

 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), Mockito.mock(Object.class));
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturnId() throws IOException {
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode("123");
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(Mockito.mock(Object.class));
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));

 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), Mockito.mock(Object.class));
 assertEquals("123", result);
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode("123");
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(testInstance);
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));
 when(objectIdReader.idProperty.setAndReturn(testInstance, "123")).thenReturn("123");

 objectIdValueProperty.set(testInstance, objectIdNode);
 assertEquals("123", testInstance.getClass().getDeclaredField("testName").get(testInstance));
 }

 @Test
 public void testSetAndReturnNull() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode(null);
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(testInstance);
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));
 when(objectIdReader.idProperty.setAndReturn(testInstance, null)).thenReturn(null);

 Object result = objectIdValueProperty.setAndReturn(testInstance, objectIdNode);
 assertNull(result);
 }

 @Test
 public void testSetAndReturnId() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdNode objectIdNode = JsonCreationUtils.createObjectIdNode("123");
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator.fromObjectIdNode(objectIdNode)).thenReturn("123");
 when(objectIdReader.resolver.getCurrentObject()).thenReturn(testInstance);
 when(objectIdReader.idProperty.getName()).thenReturn(PropertyName.construct("testName"));
 when(objectIdReader.idProperty.setAndReturn(testInstance, "123")).thenReturn("123");
 when(objectIdReader.idProperty.getType()).thenReturn(String.class);
 when(objectIdReader.getSerializerProvider().findValueSerializer(String.class)).thenReturn(new ToStringSerializer(String.class));

 Object result = objectIdValueProperty.setAndReturn(testInstance, objectIdNode);
 assertEquals("123", result);
 }
}