package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

 private static ObjectIdValueProperty createTestObjectIdValueProperty(ObjectIdReader objectIdReader) {
 return new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @org.junit.Test
 public void testConstructor() {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 assertNotNull(objectIdValueProperty);
 }

 @org.junit.Test
 public void testGetAnnotation() {
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(null);
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @org.junit.Test
 public void testGetMember() {
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(null);
 assertNull(objectIdValueProperty.getMember());
 }

 @org.junit.Test
 public void testDeserializeAndSet() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public void bindItem(Object instance) {
 BeanUtil.setFieldValue(instance, "id", "testId");
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 Object instance = new Object();
 JsonParser jsonParser = createMockJsonParser();
 jsonParser.nextToken(); // should not cause exception
 Object id = objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), instance);
 assertEquals("testId", id);
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public void bindItem(Object instance) {
 BeanUtil.setFieldValue(instance, "id", "testId");
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 Object instance = new Object();
 JsonParser jsonParser = createMockJsonParser();
 jsonParser.nextValue("testId");
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), instance);
 assertEquals(instance, result);
 assertEquals("testId", BeanUtil.getFieldValue(instance, "id"));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 Object instance = new Object();
 Object id = new Object();
 objectIdValueProperty.set(instance, id);
 fail("Should not reach here");
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() { return null; }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 Object instance = new Object();
 Object id = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 fail("Should not reach here");
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testWithName() {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
 fail("Should not reach here");
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testWithValueDeserializer() {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public JsonDeserializer<?> getValueDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 JsonDeserializer<?> deser = new StdSerializer<String>(String.class) {};
 ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deser);
 fail("Should not reach here");
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testWithNullProvider() {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<String>(String.class) {};
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public NullValueProvider getNullProvider() {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValueProperty = createTestObjectIdValueProperty(objectIdReader);
 NullValueProvider nva = new MockNullValueProvider();
 ObjectIdValueProperty result = objectIdValueProperty.withNullProvider(nva);
 fail("Should not reach here");
 }

 private static JsonParser createMockJsonParser() {
 return jsonParser(new JsonFactory().getParser());
 }

 private static JsonParser jsonParser(JsonParser p) {
 AnnotationIntrospector introspector = new StandardAnnotationIntrospector();
 p.setCodec(new ObjectMapper());
 p.setAnnotationIntrospector(introspector);
 return p;
 }
}

class MockNullValueProvider implements NullValueProvider {
 @Override
 public Object getNullValue(BeanProperty property) {
 return null;
 }
}