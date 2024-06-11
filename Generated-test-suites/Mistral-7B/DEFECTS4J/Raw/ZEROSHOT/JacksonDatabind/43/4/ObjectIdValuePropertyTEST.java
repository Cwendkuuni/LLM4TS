package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws IOException {
 ObjectIdReader.Builder objectIdReaderBuilder = new ObjectIdReader.Std("id");
 objectIdReader = objectIdReaderBuilder.build();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Mockito.mock(ObjectId.class)));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = ObjectIdNode.valueOf("123").asText();
 Object idInstance = new Object();
 JsonParser jsonParser = new JsonFactory().createParser("{\"id\":\"" + id + "\"}");

 objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), idInstance);

 assertEquals(id, objectIdReader.getIdType().valueFromString("123"));

 JsonFactory jsonFactory = new JsonFactory();
 Object idReader = jsonFactory.createParser("{\"id\": null}");
 objectIdValueProperty.deserializeAndSet(idReader, new TestContext(), idInstance);
 assertNull(objectIdReader.getIdType().valueFromString(null));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = ObjectIdNode.valueOf("123").asText();
 Object idInstance = new Object();
 Object returnValue = new Object();
 JsonParser jsonParser = new JsonFactory().createParser("{\"id\":\"" + id + "\"}");

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), idInstance);

 assertEquals(id, result);

 Object idReader = new JsonFactory().createParser("{\"id\": null}");
 result = objectIdValueProperty.deserializeSetAndReturn(idReader, new TestContext(), idInstance);
 assertNull(result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = ObjectIdNode.valueOf("123").asText();
 Object idInstance = new Object();

 objectIdValueProperty.set(idInstance, id);

 assertEquals(id, objectIdReader.getIdType().valueFromString("123"));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = ObjectIdNode.valueOf("123").asText();
 Object idInstance = new Object();
 Object returnValue = new Object();

 Object result = objectIdValueProperty.setAndReturn(idInstance, id);

 assertEquals(id, result);
 }
}

class TestContext extends DeserializationContext {

 @Override
 public ObjectIdGenerator getObjectIdGenerator() {
 return new AlwaysStandardObjectIdGenerator();
 }

 @Override
 public ObjectIdResolver getObjectIdResolver() {
 return new AlwaysStandardObjectIdResolver();
 }
}

class AlwaysStandardObjectIdGenerator implements ObjectIdGenerator {

 @Override
 public ObjectId idFromString(String idStr) {
 return new ObjectId(idStr);
 }
}

class AlwaysStandardObjectIdResolver implements ObjectIdResolver {

 @Override
 public ObjectId idFromValue(Object id) {
 return new ObjectId(id.toString());
 }
}

class ObjectId {
 public static String valueFromString(ObjectId id) {
 return id.toString();
 }
}