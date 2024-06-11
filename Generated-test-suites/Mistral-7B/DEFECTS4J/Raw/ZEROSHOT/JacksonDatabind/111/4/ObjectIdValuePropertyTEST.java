package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;

 @Before
 public void setUp() throws IOException {
 ObjectIdReader.ObjectIdGenerator generator = new StdObjectIdGenerator();
 ObjectIdReader.ObjectIdResolver resolver = new StdObjectIdResolver();
 objectIdReader = new ObjectIdReader("idProperty", ObjectId.class, null, null, null, generator, resolver);
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(property.getAnnotation(ObjectId.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<ObjectId> deserializer = new StdObjectIdDeserializer();
 ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider nva = new NullProvider();
 ObjectIdValueProperty newProperty = property.withNullProvider(nva);
 assertEquals(nva, newProperty.getNullProvider());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testInstance = new Object();
 JsonParser p = new JsonParser();
 p.setValue("123");
 property.deserializeAndSet(p, new TestContext(), testInstance);
 ObjectIdNode idNode = (ObjectIdNode) p.getCodec().readTree(p);
 assertEquals("123", idNode.getValueAsString());
 ObjectId id = idNode.getObjectIdValue();
 assertNotNull(id);
 // Verify that the id property is set correctly.
 ObjectIdReader idReader = new ObjectIdReader("idProperty", ObjectId.class, null, null, null, generator, resolver);
 SettableBeanProperty idProp = idReader.idProperty;
 assertEquals(id, idProp.get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testInstance = new Object();
 JsonParser p = new JsonParser();
 p.setValue("123");
 Object result = property.deserializeSetAndReturn(p, new TestContext(), testInstance);
 assertEquals(testInstance, result);
 ObjectIdNode idNode = (ObjectIdNode) p.getCodec().readTree(p);
 assertEquals("123", idNode.getValueAsString());
 ObjectId id = idNode.getObjectIdValue();
 assertNotNull(id);
 // Verify that the id property is set correctly.
 ObjectIdReader idReader = new ObjectIdReader("idProperty", ObjectId.class, null, null, null, generator, resolver);
 SettableBeanProperty idProp = idReader.idProperty;
 assertEquals(id, idProp.get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object testInstance = new Object();
 ObjectId id = new ObjectId("123");
 property.setAndReturn(testInstance, id);
 ObjectIdNode idNode = (ObjectIdNode) new ObjectMapper().writeValueAsTree(id);
 assertEquals(idNode.ValueAsString, property.getName().getShortName());
 ObjectIdReader idReader = new ObjectIdReader("idProperty", ObjectId.class, null, null, null, generator, resolver);
 SettableBeanProperty idProp = idReader.idProperty;
 assertEquals(id, idProp.get(testInstance));
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectIdReader _findObjectIdReader(Class<?> cls, String name) {
 return objectIdReader;
 }
}

class StdObjectIdGenerator implements ObjectIdGenerator {
 @Override
 public ObjectId generateId(SerializerProvider serializerProvider, JsonGenerator jgen, Object idValue) throws IOException {
 return new ObjectId("" + idValue);
 }
}

class StdObjectIdResolver implements ObjectIdResolver {
 @Override
 public ObjectId resolveId(JsonParser p, ObjectId id) throws IOException {
 return id;
 }
}

class StdObjectIdDeserializer extends JsonDeseralizere<ObjectId> {
 @Override
 public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new ObjectId(p.getValueAsString());
 }
}

class NullProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
}

class ObjectId {
 private final String value;

 ObjectId(String value) {
 this.value = value;
 }
}