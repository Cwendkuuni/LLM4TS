package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.INT, new StandardSerializer(ObjectIdNode.class));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 // Testing constructor
 @Test
 public void testObjectIdValuePropertyConstructor() {
 // Check if constructor is called correctly
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals("idPropertyName", objectIdValueProperty.getName());
 assertEquals(ObjectIdReader.IdType.INT, objectIdValueProperty.getIdType());
 }

 // Testing getter methods
 @Test
 public void testGetters() {
 assertEquals("idPropertyName", objectIdValueProperty.getName());
 assertEquals(ObjectIdReader.IdType.INT, objectIdValueProperty.getIdType());
 assertNull(objectIdValueProperty.getAnnotation(JsonSerializer.class));
 assertNull(objectIdValueProperty.getMember());
 }

 // Testing deserialization methods
 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = new Integer(1);
 ObjectIdNode idNode = ObjectIdNode.from(id);
 JsonParser p = new TestJsonFactory().createParser(idNode.toString());
 DeserializationContext ctxt = new TestContext();
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
 ObjectIdNode actualIdNode = ctxt.getObjectId(instance);
 assertEquals(id, actualIdNode.asInt());
 }

 // Testing deserializeSetAndReturn
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = new Integer(1);
 ObjectIdNode idNode = ObjectIdNode.from(id);
 JsonParser p = new TestJsonFactory().createParser(idNode.toString());
 DeserializationContext ctxt = new TestContext();
 Object instance = new Object();
 Object returnValue = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
 assertSame(instance, returnValue);
 }

 // Testing set method
 @Test
 public void testSet() throws Exception {
 Object id = new Integer(1);
 Object instance = new Object();
 objectIdValueProperty.set(instance, id);
 ObjectIdNode actualIdNode = ctxt.getObjectId(instance);
 assertEquals(id, actualIdNode.asInt());
 }

 // Testing setAndReturn method
 @Test
 public void testSetAndReturn() throws Exception {
 Object id = new Integer(1);
 Object instance = new Object();
 Object returnValue = objectIdValueProperty.setAndReturn(instance, id);
 assertSame(instance, returnValue);
 }

 // Testing edge cases and exception handling
 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object instance = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty getIdProperty() {
 return null;
 }
 };
 objectIdValueProperty.set(instance, new Object());
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = IOException.class)
 public void testNullId() throws Exception {
 Object id = null;
 JsonParser p = new TestJsonFactory().createParser(ObjectIdNode.class.getName());
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
 fail("Should throw IOException for null id");
 }

 @Test(expected = IOException.class)
 public void testNonObjectIdType() throws Exception {
 ObjectIdReader nonObjectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, new StandardSerializer(String.class));
 ObjectIdValueProperty nonObjectIdValueProperty = new ObjectIdValueProperty(nonObjectIdReader, new PropertyMetadata());
 Object id = new Integer(1);
 ObjectIdNode idNode = ObjectIdNode.from(id);
 JsonParser p = new TestJsonFactory().createParser(idNode.toString());
 DeserializationContext ctxt = new TestContext();
 Object instance = new Object();
 try {
 nonObjectIdValueProperty.deserializeAndSet(p, ctxt, instance);
 fail("Should throw IOException for non-ObjectIdType");
 } catch (IOException e) {
 // expected
 }
 }
}