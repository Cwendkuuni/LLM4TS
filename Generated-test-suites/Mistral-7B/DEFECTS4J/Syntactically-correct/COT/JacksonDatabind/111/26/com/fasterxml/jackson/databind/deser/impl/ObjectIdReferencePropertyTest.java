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

 private ObjectIdReferenceProperty createObjectIdReferenceProperty(ObjectIdInfo objectIdInfo) {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 return objectIdReferenceProperty;
 }

 @Test
 public void testObjectIdReferencePropertyConstructor() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 assertNotNull(objectIdReferenceProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyNullObjectIdInfo() {
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(null);
 }

 @Test
 public void testObjectIdReferencePropertyGetAnnotation() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 assertNull(objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testObjectIdReferencePropertyGetMember() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 assertNull(objectIdReferenceProperty.getMember());
 }

 @Test
 public void testObjectIdReferencePropertyGetCreatorIndex() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testObjectIdReferencePropertyFixAccess() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 DeserializationConfig config = new DeserializationConfig();
 objectIdReferenceProperty.fixAccess(config);
 }

 @Test
 public void testObjectIdReferencePropertyWithName() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
 assertEquals(newName, newObjectIdReferenceProperty.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyWithNameNullName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 PropertyName newName = null;
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
 }

 @Test
 public void testObjectIdReferencePropertyWithValueDeserializer() throws IOException {
 JsonDeserializer<String> deserializer = new StandardDeserializer<>();
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(deserializer);
 assertSame(deserializer, newObjectIdReferenceProperty.getValueDeserializer());
 }

 @Test
 public void testObjectIdReferencePropertyWithNullProvider() throws IOException {
 NullValueProvider nva = new StandardNullValueProvider();
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nva);
 assertSame(nva, newObjectIdReferenceProperty.getNullProvider());
 }

 @Test
 public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 JsonParser p = new JsonParser();
 Object instance = new Object();
 String value = "value";
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", value);
 p.setCodec(new ObjectMapper());
 p.setValueAsTree(node);
 Object deserializedValue = new Object();
 objectIdReferenceProperty.deserializeAndSet(p, new DeserializationContext(), instance);
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 JsonParser p = new JsonParser();
 Object instance = new Object();
 String value = "value";
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", value);
 p.setCodec(new ObjectMapper());
 p.setValueAsTree(node);
 Object deserializedValue = new Object();
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, new DeserializationContext(), instance);
 assertNull(result);
 assertEquals(value, instance.getClass().getDeclaredField("value").get(instance));
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testObjectIdReferencePropertyDeserializeSetAndReturnUnresolvedForwardReferenceException() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 JsonParser p = new JsonParser();
 Object instance = new Object();
 String value = "value";
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", value);
 p.setCodec(new ObjectMapper());
 p.setValueAsTree(node);
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 throw new UnresolvedForwardReference(reference);
 }

 @Test
 public void testObjectIdReferencePropertySet() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 Object instance = new Object();
 Object value = new Object();
 objectIdReferenceProperty.set(instance, value);
 assertEquals(value, instance.getClass().getDeclaredField("value").get(instance));
 }

 @Test
 public void testObjectIdReferencePropertySetAndReturn() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 Object instance = new Object();
 Object value = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(instance, value);
 assertSame(value, result);
 assertEquals(value, instance.getClass().getDeclaredField("value").get(instance));
 }

 @Test
 public void testObjectIdReferencePropertyPropertyReferring() throws IOException {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class);
 ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(objectIdInfo);
 Object instance = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, Object.class, instance);
 assertEquals(instance, propertyReferring._pojo);
 }
}