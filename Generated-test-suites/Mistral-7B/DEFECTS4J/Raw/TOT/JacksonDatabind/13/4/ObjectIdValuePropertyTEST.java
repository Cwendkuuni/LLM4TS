package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest extends TestBase {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> objectIdDeserializer;

 @Before
 public void setUp() throws Exception {
 ObjectIdGenerator generator = Mockito.mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = Mockito.mock(ObjectIdResolver.class);
 objectIdReader = Mockito.spy(new ObjectIdReader("idProperty", ObjectIdType.LONG, generator, resolver));
 objectIdDeserializer = Mockito.mock(JsonDeserializer.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new SimplePropertyMetadata());
 }

 @Test
 public void testEmptyJsonDeserialization() throws Exception {
 JsonParser jsonParser = createParser("");
 Object instance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);

 when(objectIdReader.idProperty).thenReturn(null);
 when(objectIdDeserializer.deserialize(jsonParser, context)).thenReturn(null);

 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 assertNull(getInstancePropertyValue(instance, "idProperty"));
 }

 @Test
 public void testJsonDeserializationWithId() throws Exception {
 JsonParser jsonParser = createParser("123");
 Object instance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);

 when(objectIdReader.idProperty).thenReturn(new PropertyName("idProperty"));
 when(objectIdDeserializer.deserialize(jsonParser, context)).thenReturn(123L);

 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 assertEquals(123L, getInstancePropertyValue(instance, "idProperty"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object instance = Mockito.mock(Object.class);
 Object id = Mockito.mock(Object.class);
 objectIdValueProperty.setAndReturn(instance, id);
 fail("Should not be able to set value without idProperty");
 }

 @Test
 public void testSetWithIdProperty() throws Exception {
 Object instance = Mockito.mock(Object.class);
 Object id = Mockito.mock(Object.class);
 ObjectIdProperty idProperty = Mockito.mock(ObjectIdProperty.class);
 when(objectIdReader.idProperty).thenReturn(idProperty);
 when(idProperty.setAndReturn(instance, id)).thenReturn("returnValue");

 objectIdValueProperty.setAndReturn(instance, id);
 assertEquals("returnValue", getInstancePropertyValue(instance, "idProperty"));
 }

 @Test
 public void testJsonDeserializationWithNullId() throws Exception {
 JsonParser jsonParser = createParser("null");
 Object instance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);

 when(objectIdReader.idProperty).thenReturn(new PropertyName("idProperty"));
 when(objectIdDeserializer.deserialize(jsonParser, context)).thenReturn(null);

 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 assertNull(getInstancePropertyValue(instance, "idProperty"));
 }

 @Test
 public void testJsonDeserializationWithInvalidId() throws Exception {
 JsonParser jsonParser = createParser("invalidId");
 Object instance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);

 when(objectIdReader.idProperty).thenReturn(new PropertyName("idProperty"));
 when(objectIdDeserializer.deserialize(jsonParser, context)).thenThrow(new IOException());

 try {
 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 fail("Should throw an IOException");
 } catch (IOException e) {
 // expected
 }
 }
}