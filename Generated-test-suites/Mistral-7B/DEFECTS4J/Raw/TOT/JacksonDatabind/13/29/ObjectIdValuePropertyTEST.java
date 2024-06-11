package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdProperty;
 private Object idInstance;

 @Before
 public void setup() throws Exception {
 ObjectIdReaderForString idReader = new ObjectIdReaderForString();
 objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return ObjectIdReader.IdType.LONG;
 }

 @Override
 public ObjectIdGenerator<?> getObjectIdGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver<?> getObjectIdResolver() {
 return null;
 }

 @Override
 public BeanPropertyDefinition getPropertyDefinition() {
 return null;
 }

 @Override
 public ObjectIdReader.IdReader getIdReader() {
 return idReader;
 }
 };
 objectIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 mapper = new ObjectMapper();
 idInstance = new Object();
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonNode node = mapper.readTree("{\"id\":1}");
 ObjectIdValueProperty property = mapper.findValueToAccess(node, ObjectIdValueProperty.class);
 property.deserializeAndSet(mapper.parserForTest(), mapper.deserializationContext(), idInstance);
 assertEquals(1, idInstance.getClass().getDeclaredField("id").get(idInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectIdValueProperty property = mapper.findValueToAccess(idInstance, ObjectIdValueProperty.class);
 JsonNode node = mapper.createObjectNode();
 node.put("id", "missingId");
 property.deserializeSetAndReturn(mapper.parserForTest(), mapper.deserializationContext(), idInstance);
 assertNull(idInstance.getClass().getDeclaredField("id").get(idInstance));

 node.put("id", null);
 property.deserializeSetAndReturn(mapper.parserForTest(), mapper.deserializationContext(), idInstance);
 assertNull(idInstance.getClass().getDeclaredField("id").get(idInstance));

 node.put("id", new Long(1));
 property.deserializeSetAndReturn(mapper.parserForTest(), mapper.deserializationContext(), idInstance);
 assertEquals(1, idInstance.getClass().getDeclaredField("id").get(idInstance));
 }

 @Test
 public void testSet() throws Exception {
 idInstance.getClass().getDeclaredField("id").set(idInstance, 1);
 ObjectIdValueProperty property = mapper.findValueToAccess(idInstance, ObjectIdValueProperty.class);
 property.set(idInstance, null);
 assertNull(idInstance.getClass().getDeclaredField("id").get(idInstance));
 }
}