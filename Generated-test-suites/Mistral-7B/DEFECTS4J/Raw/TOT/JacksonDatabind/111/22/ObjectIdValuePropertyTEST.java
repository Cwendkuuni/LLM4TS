package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectNode testObject = JsonFactory.instance.createObjectNode();
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return PropertyName.constructorFrom("testProperty");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<?> idDeserializer() {
 return null;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return new SimplestObjectIdGenerator();
 }

 @Override
 public ObjectIdResolver idResolver() {
 return new ObjectIdResolver() {
 @Override
 public ObjectId idFor(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new ReadableObjectId(1L);
 }
 };
 }

 @Override
 public SettableBeanProperty idProperty() {
 return new TestSettableBeanProperty();
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata(null));
 }

 @Test
 public void testDeserializeAndSetNullId() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.deserializeAndSet(TestJsonParser.instance, new TestContext(), testInstance);
 assertNull(objectIdValueProperty.getValue(testInstance));
 }

 @Test
 public void testDeserializeAndSetValidId() throws IOException {
 Object testInstance = new Object();
 ObjectNode validIdNode = JsonFactory.instance.createObjectNode();
 validIdNode.put("$id", "1");
 TestJsonParser p = new TestJsonParser(validIdNode.toString());
 objectIdValueProperty.deserializeAndSet(p, new TestContext(), testInstance);
 assertEquals(1L, objectIdValueProperty.getValue(testInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullValue() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.set(testInstance, null);
 fail("Should throw UnsupportedOperationException when setting null value");
 }

 @Test
 public void testSetValidValue() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.set(testInstance, 1L);
 assertEquals(1L, objectIdValueProperty.getValue(testInstance));
 }

 @Test
 public void testGetAnnotations() {
 Annotation[] annotations = objectIdValueProperty.getAnnotation(null);
 assertNull(annotations);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = objectIdValueProperty.getMember();
 assertNull(member);
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public void findValueId(ObjectId id, ObjectIdGenerator generator) throws IOException {
 fail("findValueId should not be called");
 }

 @Override
 public void reportBadInitialValue(String propertyName, Object value, JsonParser p) throws IOException {
 fail("reportBadInitialValue should not be called");
 }

 @Override
 public void reportBadObjectId(ObjectId id, JsonParser p) throws IOException {
 fail("reportBadObjectId should not be called");
 }
}

class TestJsonParser extends JsonParser {
 static TestJsonParser instance = new TestJsonParser("");

 TestJsonParser() {
 super(new StringCharArrayBuilder(new byte[0][], 0, 0));
 }

 @Override
 public void close() throws IOException {
 // noop
 }

 @Override
 public void close() {
 // noop
 }

 @Override
 public void close() throws IOException {
 // noop
 }

 @Override
 public void close() {
 // noop
 }

 @Override
 public void close() throws IOException {
 // noop
 }

 @Override
 public void close() {
 // noop
 }

 @Override
 public JsonToken nextToken() throws IOException {
 return null;
 }

 @Override
 public JsonToken nextValue() throws IOException {
 return null;
 }
}

class TestSettableBeanProperty {
 // empty implementation for IdProperty
}

class SimplestObjectIdGenerator implements ObjectIdGenerator {
 @Override
 public ObjectId generateId(Serializable karma, Object id) {
 return new ReadableObjectId(1L);
 }
}

class ObjectIdResolver implements ObjectIdResolver {
 @Override
 public ObjectId idFor(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new ReadableObjectId(1L);
 }
}

class ReadableObjectId implements ObjectId {
 private final long id;

 ReadableObjectId(long id) {
 this.id = id;
 }

 @Override
 public long idValue() {
 return id;
 }

 @Override
 public ObjectId getOriginalId() {
 return this;
 }

 @Override
 public void bindItem(Object obj) {
 // noop
 }
}