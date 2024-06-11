package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, null);
 private SettableBeanProperty forward = new SimpleBeanProperty("forward", String.class);
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonParser jsonParser = new Mapper().getFactory().createParser("{}");
 private DeserializationContext context = new TestContext();
 private Object instance = new Object();
 private JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
 private NullValueProvider nullValueProvider = new StandardNullValueProvider();

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertNotNull(actual);
 assertEquals(forward, actual.getForward());
 assertEquals(objectIdInfo, actual.getObjectIdInfo());
 }

 @Test
 public void testConstructorWithSrcDeserAndNva() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(objectIdReferenceProperty, deserializer, nullValueProvider);
 assertNotNull(actual);
 assertEquals(objectIdReferenceProperty, actual.getForward());
 assertEquals(objectIdReferenceProperty.getObjectIdInfo(), actual.getObjectIdInfo());
 }

 @Test
 public void testConstructorWithSrcAndNewName() {
 ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(objectIdReferenceProperty, new PropertyName("newName"));
 assertNotNull(actual);
 assertEquals(objectIdReferenceProperty, actual.getForward());
 assertEquals(objectIdReferenceProperty.getName(), actual.getName());
 }

 @Test
 public void testGetAnnotation() {
 assertEquals(forward.getAnnotation(Annotation.class), objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(forward.getMember(), objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(forward.getCreatorIndex(), objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testFixAccess() {
 objectIdReferenceProperty.fixAccess(context.getConfig());
 assertEquals(forward.getName(), objectIdReferenceProperty.getName());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String value = "value";
 ObjectNode node = ObjectMapper.parseObjectNode(new ObjectMapper().writeValueAsString(value));
 Object id = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference(null, id.getClass(), id);
 reference.setRoid(new PropertyReferring(objectIdReferenceProperty, reference, id.getClass(), instance));
 objectIdReferenceProperty.deserializeAndSet(jsonParser, context, instance);
 assertEquals(value, instance.getClass().getDeclaredField("forward").get(instance));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 ObjectNode node = ObjectMapper.parseObjectNode(new ObjectMapper().writeValueAsString("null"));
 Object id = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference(null, id.getClass(), id);
 reference.setRoid(new PropertyReferring(objectIdReferenceProperty, reference, id.getClass(), instance));
 Object actual = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertTrue(actual == null);
 }

 @Test
 public void testDeserializeSetAndReturnWithResolvedReference() throws IOException {
 ObjectNode node = ObjectMapper.parseObjectNode(new ObjectMapper().writeValueAsString("{\"id\":1,\"value\":\"value\"}"));
 Object id = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference(null, id.getClass(), id);
 PropertyReferring propertyReferring = new PropertyReferring(objectIdReferenceProperty, reference, id.getClass(), instance);
 Object value = "value";
 reference.setRoid(propertyReferring);
 Object actual = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertEquals(value, instance.getClass().getDeclaredField("forward").get(instance));
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnUnresolvedReference() throws IOException {
 ObjectNode node = ObjectMapper.parseObjectNode(new ObjectMapper().writeValueAsString("{\"id\":1,\"value\":\"value\"}"));
 Object id = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference(null, id.getClass(), id);
 Object actual = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, context, instance);
 fail("Should throw UnresolvedForwardReference exception");
 }

 @Test
 public void testSet() throws IOException {
 String value = "value";
 objectIdReferenceProperty.set(instance, value);
 assertEquals(value, instance.getClass().getDeclaredField("forward").get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 String value = "value";
 Object actual = objectIdReferenceProperty.setAndReturn(instance, value);
 assertEquals(value, actual);
 assertEquals(value, instance.getClass().getDeclaredField("forward").get(instance));
 }

}