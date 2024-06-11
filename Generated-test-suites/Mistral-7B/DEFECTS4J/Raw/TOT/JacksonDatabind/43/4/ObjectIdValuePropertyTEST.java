package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectIdReader = new SimpleObjectIdReader("idProperty");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testDeserializeAndSet() throws IOException, JsonMappingException {
 Object id = 1L;
 Object idInstance = new Object();
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("id", id);

 // Test deserialization and setting
 Object result = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), idInstance);
 assertEquals(idInstance, result);
 assertEquals(id, objectIdValueProperty.get(idInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonMappingException {
 Object id = 1L;
 Object idInstance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(objectMapper.writeValueAsString(new ObjectNode().put("id", id))), objectMapper.getDeserializationContext(), idInstance);
 assertEquals(idInstance, result);
 assertEquals(id, objectIdValueProperty.get(idInstance));
 }

 @Test
 public void testNullId() throws IOException, JsonMappingException {
 Object idInstance = new Object();
 Object result = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(objectMapper.writeValueAsString(new ObjectNode())), objectMapper.getDeserializationContext(), idInstance);
 assertNull(result);
 assertNull(objectIdValueProperty.get(idInstance));
 }

 @Test
 public void testIdPropertyIsSet() throws IOException, JsonMappingException {
 Object id = 1L;
 Object idInstance = new Object();
 Object idProperty = new SimpleIdProperty();
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule().addDeserializer(Object.class, new SimpleObjectIdDeserializer("idProperty")));
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("id", id);
 jsonNode.put("idProperty", id);

 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, objectIdReader);
 objectIdValueProperty.setIdProperty(idProperty);

 Object result = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), idInstance);
 assertEquals(idInstance, result);
 assertEquals(id, objectIdValueProperty.get(idInstance));
 assertEquals(id, idProperty.get(idInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException, JsonMappingException {
 Object idInstance = new Object();
 Object id = 1L;
 objectIdValueProperty.set(idInstance, id);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 static class SimpleObjectIdReader implements ObjectIdReader {
 private final String propertyName;

 public SimpleObjectIdReader(String propertyName) {
 this.propertyName = propertyName;
 }

 @Override
 public String getPropertyName() {
 return propertyName;
 }

 @Override
 public Class<?> getIdType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<?> getDeserializer() {
 return null;
 }

 @Override
 public ObjectIdGenerator getIdGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver getIdResolver() {
 return null;
 }
 }

 static class SimpleObjectIdDeserializer implements JsonDeserializer<Object> {
 private final String propertyName;

 public SimpleObjectIdDeserializer(String propertyName) {
 this.propertyName = propertyName;
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 Object id = p.getValueAsLong();
 return id;
 }

 @Override
 public BeanDescription getBeanDescription() {
 return null;
 }

 @Override
 public String getPropertyName() {
 return propertyName;
 }
}

 static class SimpleIdProperty extends SettableBeanProperty {
 private static final long serialVersionUID = 1L;

 public SimpleIdProperty() {
 super("idProperty", Long.class, PropertyMetadata.STD_OBJ, null);
 }

 @Override
 public Object getValue(Object bean) {
 return null;
 }

 @Override
 public void setValue(Object bean, Object value) throws IllegalAccessException {
 fail("setValue() not supported");
 }

 @Override
 public Object get(Object bean) {
 return null;
 }

 @Override
 public void set(Object bean, Object value) throws IllegalAccessException {
 fail("set() not supported");
 }
}

}