package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.NullValueProvider;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.test.BaseTest;

public class ObjectIdValuePropertyTest extends BaseTest {

 private ObjectIdReader mockObjectIdReader;
 private SettableBeanProperty mockIdProperty;

 @Before
 public void setUp() throws Exception {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockIdProperty = mock(SettableBeanProperty.class);
 }

 // Test constructor and getters
 @Test
 public void testObjectIdValuePropertyConstruction() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertSame(mockObjectIdReader.propertyName, property.getName());
 assertSame(mockObjectIdReader.getIdType(), property.getType());
 assertSame(property.getMetadata(), mockObjectIdReader.getMetadata());
 assertSame(mockObjectIdReader.getDeserializer(), property.getValueDeserializer());
 assertSame(mockObjectIdReader, property._objectIdReader);
 }

 @Test
 public void testObjectIdValuePropertySerialization() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertNull(property.getAnnotation(JsonValue.class));
 assertNull(property.getMember());
 }

 // Test deserializeAndSet method
 @Test
 public void testDeserializeAndSet_NullValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 DeserializationContext context = createContext();
 Object instance = new Object();
 property.deserializeAndSet(mockParser(new ObjectName("nullValue"), JsonToken.VALUE_NULL), context, instance);
 assertEquals(instance, context.getCurrentValue());
 assertNull(instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testDeserializeAndSet_ValidValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 DeserializationContext context = createContext();
 Object instance = new Object();
 property.deserializeAndSet(mockParser(new ObjectName("validValue"), JsonToken.VALUE_STRING), context, instance);
 String expectedId = "validValue";
 assertEquals(expectedId, context.getCurrentValue());
 assertSame(expectedId, instance.getClass().getDeclaredField("id").get(instance));
 }

 // Test deserializeSetAndReturn method
 @Test
 public void testDeserializeSetAndReturn_NullValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 JsonParser parser = mockParser(new ObjectName("nullValue"), JsonToken.VALUE_NULL);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, createContext(), instance);
 assertNull(result);
 assertNull(instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn_ValidValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 String expectedId = "validId";
 JsonParser parser = mockParser(new ObjectName("validValue"), JsonToken.VALUE_STRING);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, createContext(), instance);
 assertEquals(expectedId, result);
 assertSame(expectedId, instance.getClass().getDeclaredField("id").get(instance));
 }

 // Test set method
 @Test
 public void testSet_NullValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 Object instance = new Object();
 property.set(instance, null);
 assertNull(instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testSet_ValidValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 String expectedId = "validId";
 Object instance = new Object();
 property.set(instance, expectedId);
 assertEquals(expectedId, instance.getClass().getDeclaredField("id").get(instance));
 }

 // Test setAndReturn method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn_NoIdProperty() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 Object instance = new Object();
 Object result = property.setAndReturn(instance, new Object());
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testSetAndReturn_ValidValue() throws IOException {
 when(mockObjectIdReader.getIdType()).thenReturn(String.class);
 when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 String expectedId = "validId";
 Object instance = new Object();
 Object result = property.setAndReturn(instance, expectedId);
 assertEquals(expectedId, result);
 assertEquals(expectedId, instance.getClass().getDeclaredField("id").get(instance));
 }

 // Test withName method
 @Test
 public void testWithName() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 PropertyName newName = new PropertyName("newName");
 ObjectIdValueProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 // Test withValueDeserializer method
 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
 assertSame(deserializer, newProperty.getValueDeserializer());
 }

 // Test withNullProvider method
 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = mock(NullValueProvider.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null);
 ObjectIdValueProperty newProperty = property.withNullProvider(nva);
 assertSame(nva, newProperty._nullProvider);
 }

}