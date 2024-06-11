package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private JsonDeserializer<Object> mockValueDeserializer;
 private ObjectIdValueProperty objectIdValueProperty;

 @Test
 public void testObjectIdValuePropertyConstruction() {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);

 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 assertNotNull(actualObjectIdValueProperty);
 }

 @Test
 public void testObjectIdValuePropertyCopyConstruction() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty1 = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 ObjectIdValueProperty objectIdValueProperty2 = new ObjectIdValueProperty(objectIdValueProperty1, mockValueDeserializer);
 assertEquals(objectIdValueProperty1, objectIdValueProperty2);
 }

 @Test
 public void testObjectIdValuePropertyWithNameConstruction() {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 String newName = "newName";
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyName(newName), mockValueDeserializer);
 assertEquals(newName, actualObjectIdValueProperty.getName());
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializerConstruction() {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 assertEquals(mockValueDeserializer, actualObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), new PropertyMetadata(), Mockito.mock(JsonDeserializer.class));
 assertNull(objectIdValueProperty.getAnnotation(Mockito.mock(Annotation.class).getClass()));
 }

 @Test
 public void testGetMember() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), new PropertyMetadata(), Mockito.mock(JsonDeserializer.class));
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 Object instance = Mockito.mock(Object.class);
 JsonParser jp = Mockito.mock(JsonParser.class);
 DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
 Object id = Mockito.mock(Object.class);
 ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

 Mockito.when(mockObjectIdReader.idProperty).thenReturn(null);
 Mockito.when(mockObjectIdReader.generator).thenReturn(Mockito.mock(ObjectIdGenerator.class));
 Mockito.when(mockObjectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
 Mockito.when(mockObjectIdReader.getIdType()).thenReturn(Void.TYPE);
 Mockito.when(mockValueDeserializer.deserialize(Mockito.eq(jp), Mockito.eq(ctxt))).thenReturn(id);
 Mockito.when(mockObjectIdReader.getObjectIdReader()).thenReturn(mockObjectIdReader);
 Mockito.when(ctxt.findObjectId(Mockito.eq(id), Mockito.eq(mockObjectIdReader), Mockito.eq(mockObjectIdReader)))
         .thenReturn(roid);
 Mockito.when(roid.bindItem(Mockito.eq(instance))).thenReturn(instance);

 objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
 assertEquals(instance, objectIdValueProperty.getBean());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 Object instance = Mockito.mock(Object.class);
 JsonParser jp = Mockito.mock(JsonParser.class);
 DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
 Object id = Mockito.mock(Object.class);
 ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

 Mockito.when(mockObjectIdReader.idProperty).thenReturn(null);
 Mockito.when(mockObjectIdReader.generator).thenReturn(Mockito.mock(ObjectIdGenerator.class));
 Mockito.when(mockObjectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
 Mockito.when(mockObjectIdReader.getIdType()).thenReturn(Void.TYPE);
 Mockito.when(mockValueDeserializer.deserialize(Mockito.eq(jp), Mockito.eq(ctxt))).thenReturn(id);
 Mockito.when(mockObjectIdReader.getObjectIdReader()).thenReturn(mockObjectIdReader);
 Mockito.when(ctxt.findObjectId(Mockito.eq(id), Mockito.eq(mockObjectIdReader), Mockito.eq(mockObjectIdReader)))
         .thenReturn(roid);
 Mockito.when(roid.bindItem(Mockito.eq(instance))).thenReturn(instance);
 Mockito.when(mockObjectIdReader.getIdProperty()).thenReturn(Mockito.mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 Object instance = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);

 objectIdValueProperty.set(instance, value);
 // We cannot directly assert on the setter since ObjectIdValueProperty does not extend SettableBeanProperty.
 // Instead we will verify that the SettableBeanProperty (idProp) has been called.
 SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);
 Mockito.when(mockObjectIdReader.getIdProperty()).thenReturn(idProp);
 verify(idProp).set(instance, value);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
 mockValueDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), mockValueDeserializer);
 Object instance = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);
 Object result = Mockito.mock(Object.class);

 Mockito.when(mockObjectIdReader.getIdProperty()).thenReturn(Mockito.mock(SettableBeanProperty.class));
 when(mockObjectIdReader.getIdProperty().setAndReturn(instance, value)).thenReturn(result);

 Object objectResult = objectIdValueProperty.setAndReturn(instance, value);
 assertEquals(result, objectResult);
 }
}