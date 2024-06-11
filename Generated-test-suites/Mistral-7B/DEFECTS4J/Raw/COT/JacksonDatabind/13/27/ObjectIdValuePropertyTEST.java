package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private SettableBeanProperty mockIdProperty;

 @Before
 public void setUp() {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockIdProperty = mock(SettableBeanProperty.class);
 }

 // 1. Test constructor
 @Test
 public void testObjectIdValuePropertyConstruction() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 assertEquals(mockObjectIdReader.propertyName, objectIdValueProperty.getName());
 assertEquals(mockObjectIdReader.getIdType(), objectIdValueProperty.getType());
 assertEquals(mockObjectIdReader, objectIdValueProperty._objectIdReader);
 }

 // 2. Test getters and setters
 @Test
 public void testGettersAndSetters() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 objectIdValueProperty.setName(new PropertyName("testProperty"));
 assertEquals("testProperty", objectIdValueProperty.getName().getName());
 objectIdValueProperty.setIdProperty(mockIdProperty);
 assertNull(objectIdValueProperty.getIdProperty());
 objectIdValueProperty.setIdProperty(mockIdProperty);
 assertEquals(mockIdProperty, objectIdValueProperty.getIdProperty());
 }

 // 3. Test deserialization methods
 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 JsonParser mockJsonParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object instance = new Object();
 ObjectIdNode idNode = ObjectIdNode.getInstance("testId");
 when(mockJsonParser.getValueAsToken()).thenReturn(idNode.asToken());
 when(mockObjectIdReader.getIdProperty()).thenReturn(mockIdProperty);

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, instance);
 verify(mockIdProperty).setAndReturn(instance, idNode.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullId() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 Object instance = new Object();
 objectIdValueProperty.set(instance, null);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 // 4. Edge cases and exception handling
 @Test(expected = IOException.class)
 public void testDeserializationFailure() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 JsonParser mockJsonParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object instance = new Object();

 when(mockJsonParser.getValueAsToken()).thenThrow(new IOException("Mock error"));

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, instance);
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectIdReader() {
 try {
 new ObjectIdValueProperty(null, null);
 fail("Should not allow null ObjectIdReader");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}