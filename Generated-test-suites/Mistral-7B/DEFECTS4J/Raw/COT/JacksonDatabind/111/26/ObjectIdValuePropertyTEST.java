package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseExpectedException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty property;
 private ObjectIdReader objectIdReader;
 private JsonDeserializer<Object> valueDeserializer;
 private JsonDeserializer<Object> nullValueProvider;
 private PropertyName propertyName;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 objectIdReader = mapper.readValue(getClass().getResource("/ObjectIdReader.json"), ObjectIdReader.class);
 propertyName = PropertyName.construct("id");
 valueDeserializer = mapper.readValue(getClass().getResource("/IdDeserializer.json"), JsonDeserializer.class);
 nullValueProvider = mapper.getFactory().findNullValueProvider(Object.class, null);
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(propertyName, null, null, null, null, null, null, null, null, null, null));
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(propertyName, null, null, valueDeserializer, nullValueProvider, null, null, null, null, null, null));
 assertNotNull(objectIdValueProperty);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty newObjectIdValueProperty = property.withName(newName);
 assertEquals(newName, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Object> newValueDeserializer = mapper.readValue(getClass().getResource("/NewIdDeserializer.json"), JsonDeserializer.class);
 ObjectIdValueProperty newObjectIdValueProperty = property.withValueDeserializer(newValueDeserializer);
 assertEquals(newValueDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 JsonDeserializer<Object> newNullValueProvider = mapper.getFactory().findNullValueDeserializer(Object.class, null);
 ObjectIdValueProperty newObjectIdValueProperty = property.withNullProvider(newNullValueProvider);
 assertEquals(newNullValueProvider, newObjectIdValueProperty.getNullProvider());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullProperty() throws Exception {
 Object instance = new Object();
 try {
 property.set(instance, null);
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // pass
 }
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectParser parser = mapper.parser();
 parser.setCodec(mapper);
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 1L);
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken();
 property.deserializeAndSet(jsonParser, mapper.getConfiguration(), new Object());
 assertEquals(1L, property.getValue(new Object()));
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithInvalidJsonToken() throws Exception {
 ObjectParser parser = mapper.parser();
 ObjectNode node = mapper.createObjectNode();
 parser.setCodec(mapper);
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken(JsonToken.START_ARRAY);
 property.deserializeAndSet(jsonParser, mapper.getConfiguration(), new Object());
 fail("Should throw IOException for invalid JsonToken");
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithNullValue() throws Exception {
 ObjectParser parser = mapper.parser();
 ObjectNode node = mapper.createObjectNode();
 parser.setCodec(mapper);
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken(JsonToken.VALUE_NULL);
 try {
 property.deserializeSetAndReturn(jsonParser, mapper.getConfiguration(), new Object());
 fail("Should throw IOException for null value");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectParser parser = mapper.parser();
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 1L);
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken();
 Object id = property.deserializeSetAndReturn(jsonParser, mapper.getConfiguration(), new Object());
 assertEquals(1L, id);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Object id = 1L;
 Object newInstance = property.setAndReturn(instance, id);
 assertEquals(instance, newInstance);
 assertEquals(id, property.getValue(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithNullInstance() throws Exception {
 Object id = 1L;
 try {
 property.setAndReturn(null, id);
 fail("Should throw UnsupportedOperationException for null instance");
 } catch (UnsupportedOperationException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidValue() throws Exception {
 Object instance = new Object();
 try {
 property.set(instance, new Object());
 fail("Should throw IllegalArgumentException for invalid value");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = property.getAnnotation(Id.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember member = property.getMember();
 assertNull(member);
 }

 @Test(expected = BaseExpectedException.class)
 public void testDeserializeAndSetWithFailedDeserializer() throws Exception {
 ObjectParser parser = mapper.parser();
 ObjectNode node = mapper.createObjectNode();
 node.put("id", "invalidJson");
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken();
 try {
 property.deserializeAndSet(jsonParser, mapper.getConfiguration(), new Object());
 fail("Should throw BaseExpectedException for failed deserializer");
 } catch (BaseExpectedException e) {
 // pass
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeSetAndReturnWithFailedDeserializer() throws Exception {
 ObjectParser parser = mapper.parser();
 ObjectNode node = mapper.createObjectNode();
 node.put("id", "invalidJson");
 JsonParser jsonParser = parser.readValueAsTree(node);
 jsonParser.nextToken();
 try {
 property.deserializeSetAndReturn(jsonParser, mapper.getConfiguration(), new Object());
 fail("Should throw JsonProcessingException for failed deserializer");
 } catch (JsonProcessingException e) {
 // pass
 }
 }
}