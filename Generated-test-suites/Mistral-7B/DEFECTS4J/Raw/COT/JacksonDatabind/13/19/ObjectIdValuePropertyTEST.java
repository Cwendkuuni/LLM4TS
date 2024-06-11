package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader.Std objectIdReaderStd = new ObjectIdReader.Std();
 objectIdReader = new ObjectIdReader(new PropertyName("id"), objectIdReaderStd, null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testObjectIdValuePropertyConstructor() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, null);
 assertEquals(objectIdValueProperty.getName(), obj.getName());
 assertEquals(objectIdValueProperty.getIdType(), obj.getIdType());
 assertEquals(objectIdValueProperty.getMetadata(), obj.getMetadata());
 assertEquals(objectIdValueProperty._objectIdReader, obj._objectIdReader);
 }

 @Test
 public void testDeprecatedConstructors() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
 assertEquals(objectIdValueProperty.getName(), obj.getName());
 assertEquals(objectIdValueProperty.getIdType(), obj.getIdType());
 assertEquals(objectIdValueProperty.getMetadata(), obj.getMetadata());
 assertEquals(objectIdValueProperty._objectIdReader, obj._objectIdReader);
 }

 @Test
 public void testWithName() throws Exception {
 ObjectIdValueProperty obj = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals(new PropertyName("newName"), obj.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = new StdScalarTypeDeserializer(new ClassType(Integer.class));
 ObjectIdValueProperty obj = objectIdValueProperty.withValueDeserializer(typeDeserializer);
 assertEquals(typeDeserializer, obj.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser jsonParser = createMockJsonParser();
 Object id = new Integer(1);
 objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), new Object());
 assertEquals(id, getIdValue(new Object()));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = new Integer(1);
 JsonParser jsonParser = createMockJsonParser();
 jsonParser.setValue(id.toString());
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), new Object());
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws Exception {
 Object id = new Integer(1);
 objectIdValueProperty.set(new Object(), id);
 assertEquals(id, getIdValue(new Object()));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object id = new Integer(1);
 Object result = objectIdValueProperty.setAndReturn(new Object(), id);
 assertEquals(id, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object id = new Integer(1);
 ObjectIdValueProperty objWithoutIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty;
 };
 objWithoutIdProperty.set(new Object(), id);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNull() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonParser jsonParser = createMockJsonParser();
 jsonParser.setValue(null);
 obj.deserializeAndSet(jsonParser, new TestContext(), new Object());
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNull() throws Exception {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object result = obj.deserializeSetAndReturn(createMockJsonParser(), new TestContext(), new Object());
 }

 private JsonParser createMockJsonParser() {
 return mock(JsonParser.class);
 }

 private Object getIdValue(Object obj) throws IOException {
 Object result = objectIdValueProperty.get(obj);
 return (_result instanceof Number) ? ((Number) result).longValue() : result;
 }
}