package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName getPropertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<?> getIdType() {
 return ObjectIdNode.class;
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {
 @Override
 public ObjectIdNode deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return ObjectIdNode.valueOf(p.getValueAsString());
 }
 };
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 throw new UnsupportedOperationException();
 }

 @Override
 public ObjectIdResolver idResolver() {
 throw new UnsupportedOperationException();
 }

 @Override
 public SettableBeanProperty idProperty() {
 throw new UnsupportedOperationException();
 }
 };

 objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(ObjectIdNode.class, objectIdReader);
 }});
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object idObject = new ObjectIdValueProperty(objectIdReader, null).deserializeAndSet(objectMapper.getFactory().createParser("1"), objectMapper, new Object());
 assertEquals(1, idObject);
 }

 @Test
 public void testNullId() throws JsonProcessingException {
 Object idObject = new ObjectIdValueProperty(objectIdReader, null).deserializeAndSet(objectMapper.getFactory().createParser("null"), objectMapper, new Object());
 assertNull(idObject);
 }

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 Object idObject = new Object();
 Object idValue = "2";
 Object idSetObject = new ObjectIdValueProperty(objectIdReader, null).setAndReturn(idObject, idValue);
 assertEquals(idValue, idSetObject);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetAnnotation() throws JsonProcessingException {
 new ObjectIdValueProperty(objectIdReader, null).getAnnotation(MyAnnotation.class);
 }

 @Test
 public void testGetMember() throws JsonProcessingException {
 new ObjectIdValueProperty(objectIdReader, null).getMember();
 }
}