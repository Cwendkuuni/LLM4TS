package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdReader.IdInfo;
import com.fasterxml.jackson.databind.util.ResolverBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 ObjectIdReader.IdInfo idInfo = new ObjectIdReader.IdInfo();
 idInfo.generator = new ObjectIdGenerator() {
 @Override
 public long generateId() {
 return System.currentTimeMillis();
 }
 };
 idInfo.resolver = new ResolverBuilder().build();
 objectIdReader = new ObjectIdReader("id", IdInfo.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(objectIdReader.propertyName, property.getName());
 assertEquals(objectIdReader.getIdType(), property.getNullValue());
 assertEquals(objectIdReader, property._objectIdReader);
 }

 @Test
 public void testWithName() throws InstantiationException, IllegalAccessException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
 assertEquals("newName", newProperty.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() throws InstantiationException, IllegalAccessException {
 JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, newProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() throws InstantiationException, IllegalAccessException {
 NullValueProvider nullValueProvider = new NullValueProvider() {};
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, newProperty._nullProvider);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testInstance = new Object();
 JsonParser parser = new JsonParser();
 parser.setValue("123");
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property.deserializeAndSet(parser, new TestContext(), testInstance);
 assertEquals("123", testInstance.getClass().getDeclaredField("id").get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object id = new Long(123);
 Object deserializedId = ClassUtil.construct(property._valueDeserializer.getValueClass(), id);
 Object result = property.deserializeSetAndReturn(parser, new TestContext(), testInstance);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property.set(testInstance, new Long(123));
 assertEquals(123, testInstance.getClass().getDeclaredField("id").get(testInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object testInstance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 property.set(testInstance, new Long(123));
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeWithNullReader() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
 JsonParser parser = new JsonParser();
 parser.setValue("123");
 property.deserializeAndSet(parser, new TestContext(), new Object());
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeWithEmptyName() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader("", IdInfo.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonParser parser = new JsonParser();
 parser.setValue("123");
 property.deserializeAndSet(parser, new TestContext(), new Object());
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeWithNonStringName() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader(1, IdInfo.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonParser parser = new JsonParser();
 parser.setValue("123");
 property.deserializeAndSet(parser, new TestContext(), new Object());
 fail("Should throw IllegalArgumentException");
 }
}