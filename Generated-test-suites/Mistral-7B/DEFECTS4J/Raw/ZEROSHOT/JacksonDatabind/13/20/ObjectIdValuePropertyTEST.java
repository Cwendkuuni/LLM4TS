package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() {
 objectIdReader = Mockito.spy(new ObjectIdReader() {
 // override methods as needed
 @Override
 public PropertyName propertyName() {
 return new PropertyName("objectId");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public BeanProperty.ValueDeserializer getIdDeserializer() {
 return new StandardDeserializer<>(Long.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return Mockito.mock(ObjectIdGenerator.class);
 }

 @Override
 public ObjectIdResolver idResolver() {
 return Mockito.mock(ObjectIdResolver.class);
 }

 @Override
 public SettableBeanProperty idProperty() {
 return Mockito.mock(SettableBeanProperty.class);
 }
 });
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 // add assertions as needed
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Long id = 1L;
 ObjectParser parser = Mockito.mock(ObjectParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object instance = new Object();
 when(parser.getCurrentName()).thenReturn("objectId");
 when(parser.getValueAsLong()).thenReturn(id);
 ObjectIdReader objectIdReaderSpy = Mockito.spy(objectIdReader);
 ObjectIdValueProperty objectIdValuePropertySpy = Mockito.spy(objectIdValueProperty);
 when(objectIdValuePropertySpy.deserializeSetAndReturn(eq(parser), eq(context), eq(instance))).thenCallRealMethod();
 when(objectIdReaderSpy.getIdDeserializer()).thenReturn(new StandardDeserializer<>(Long.class) {});
 when(objectIdReaderSpy.idGenerator()).thenReturn(Mockito.mock(ObjectIdGenerator.class));
 when(objectIdReaderSpy.idResolver()).thenReturn(Mockito.mock(ObjectIdResolver.class));
 when(objectIdReaderSpy.idProperty()).thenReturn(Mockito.mock(SettableBeanProperty.class));
 Object result = objectIdValuePropertySpy.deserializeSetAndReturn(parser, context, instance);
 assertEquals(id, result);
 // add assertions on mocks as needed
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 objectIdValueProperty.set(instance, value);
 // add assertions as needed
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);
 when(idProp.setAndReturn(eq(instance), eq(value))).thenReturn(instance);
 ObjectIdReader objectIdReaderSpy = Mockito.spy(objectIdReader);
 ObjectIdValueProperty objectIdValuePropertySpy = Mockito.spy(objectIdValueProperty);
 when(objectIdValuePropertySpy.setAndReturn(instance, value)).thenCallRealMethod();
 when(objectIdReaderSpy.idProperty()).thenReturn(idProp);
 Object result = objectIdValuePropertySpy.setAndReturn(instance, value);
 assertEquals(instance, result);
 // add assertions on mocks as needed
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = new PropertyName("newName");
 ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
 assertEquals(newName, result.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, result.getValueDeserializer());
 }
}