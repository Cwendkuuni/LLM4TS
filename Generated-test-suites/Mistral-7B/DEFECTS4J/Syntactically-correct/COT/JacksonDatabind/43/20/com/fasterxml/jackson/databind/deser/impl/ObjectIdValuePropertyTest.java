package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = new ObjectIdReader("idPropertyName", ObjectId.class, null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty constructorTest = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(objectIdReader.propertyName, constructorTest.getName());
 assertEquals(objectIdReader.getIdType(), constructorTest.getDeserializer().getType());
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty withNameTest = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals("newName", withNameTest.getName().getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdValueProperty withValueDeserializerTest = objectIdValueProperty.withValueDeserializer(new StubJsonDeserializer());
 Assert.assertNotNull(withValueDeserializerTest.getValueDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullInstance() throws IOException {
 Object instance = new Object();
 objectIdValueProperty.set(instance, null);
 fail("Should throw UnsupportedOperationException when setting null instance");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new ObjectId(1L);
 JsonParser mockParser = new JsonTestParser();
 mockParser.setValue(id.toString());
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(mockParser, new DeserializationContext(), instance);
 ObjectId idFromInstance = instance.getClass().getDeclaredField("id").get(instance);
 assertEquals(id, idFromInstance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnNullId() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty.deserializeSetAndReturn(null, new DeserializationContext(), instance);
 fail("Should throw IOException when id is null");
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object id = new ObjectId(1L);
 Object expectedInstance = new Object();
 SettableBeanProperty idProp = new StubSettableBeanProperty();
 expectedInstance.getClass().getDeclaredField("id").set(expectedInstance, id);
 when(idProp.setAndReturn(instance, id)).thenReturn(expectedInstance);
 ObjectIdValueProperty objectIdPropertySpy = spy(objectIdValueProperty);
 doReturn(idProp).when(objectIdPropertySpy).getObjectIdReader().idProperty;
 Object result = objectIdPropertySpy.setAndReturn(instance, id);
 assertEquals(expectedInstance, result);
 }

 @Test
 public void testSetAndReturnWithNullIdProperty() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty objectIdValuePropertySpy = spy(objectIdValueProperty);
 doThrow(new UnsupportedOperationException("")).when(objectIdValuePropertySpy)
 .getObjectIdReader().idProperty;
 try {
 objectIdValuePropertySpy.setAndReturn(instance, null);
 fail("Should throw UnsupportedOperationException when idProperty is null");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }
}