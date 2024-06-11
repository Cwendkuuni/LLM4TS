package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonType.STANDARD_OBJECT);
 private SettableBeanProperty forward = new SimpleValueProperty.Std("forward", JsonType.STANDARD_OBJECT);
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<?> jsonDeserializer = new StdDeserializer.Std(JsonType.STANDARD_OBJECT.getClass());
 private NullValueProvider nullValueProvider = new StandardNullValueProvider();

 @Test
 public void testObjectIdReferencePropertyConstructorWithObjectIdInfo() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Assert.assertNotNull(objectIdReferenceProperty);
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithForward() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Assert.assertNotNull(objectIdReferenceProperty);
 }

 @Test
 public void testObjectIdReferencePropertyWithName() {
 PropertyName newName = PropertyName.construct("newName");
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
 Assert.assertEquals(newName, objectIdReferenceProperty.getName());
 }

 @Test
 public void testObjectIdReferencePropertyWithValueDeserializer() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 Assert.assertEquals(jsonDeserializer, objectIdReferenceProperty.getValueDeserializer());
 }

 @Test
 public void testObjectIdReferencePropertyWithNullProvider() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, null, nullValueProvider);
 Assert.assertEquals(nullValueProvider, objectIdReferenceProperty.getNullProvider());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyWithNullForward() {
 forward = null;
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyWithNullObjectIdInfo() {
 objectIdInfo = null;
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyWithNullJsonDeserializer() {
 jsonDeserializer = null;
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdReferencePropertyWithNullNullValueProvider() {
 nullValueProvider = null;
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 }

 @Test
 public void testFixAccess() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 DeserializationConfig config = new DeserializationConfig();
 objectIdReferenceProperty.fixAccess(config);
 }

 @Test
 public void testGetAnnotation() {
 Object annotation = new Object();
 Annotation annotationType = Object.class;
 Annotation result = objectIdReferenceProperty.getAnnotation(annotationType.getClass());
 Assert.assertEquals(annotation, result);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember result = objectIdReferenceProperty.getMember();
 Assert.assertNotNull(result);
 }

 @Test
 public void testGetCreatorIndex() {
 int result = objectIdReferenceProperty.getCreatorIndex();
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 JsonParser p = new TestJsonParser();
 p.setCodec(new ObjectMapper());
 Object value = new Object();
 objectIdReferenceProperty.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance);
 Assert.assertEquals(value, _forward.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = new Object();
 JsonParser p = new TestJsonParser();
 p.setCodec(new ObjectMapper());
 Object value = new Object();
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, new DeserializationContext(new ObjectMapper()), instance);
 Assert.assertEquals(value, _forward.get(instance));
 Assert.assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
 Object instance = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 JsonParser p = new TestJsonParser();
 p.setCodec(new ObjectMapper());
 objectIdReferenceProperty.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
 Object instance = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 JsonParser p = new TestJsonParser();
 p.setCodec(new ObjectMapper());
 objectIdReferenceProperty.deserializeSetAndReturn(p, new DeserializationContext(new ObjectMapper()), instance);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 objectIdReferenceProperty.set(instance, value);
 Assert.assertEquals(value, _forward.get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(instance, value);
 Assert.assertEquals(value, result);
 }

 @Test
 public void testPropertyReferringConstructor() {
 ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty(forward, objectIdInfo);
 UnresolvedForwardReference ref = new UnresolvedForwardReference();
 Class<?> type = JsonType.STANDARD_OBJECT.getClass();
 Object ob = new Object();
 PropertyReferring referencing = new PropertyReferring(parent, ref, type, ob);
 }

 @Test
 public void testPropertyReferringHandleResolvedForwardReference() {
 ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty(forward, objectIdInfo);
 UnresolvedForwardReference ref = new UnresolvedForwardReference();
 Class<?> type = JsonType.STANDARD_OBJECT.getClass();
 Object ob = new Object();
 PropertyReferring referencing = new PropertyReferring(parent, ref, type, ob);
 Object id = new Object();
 Object value = new Object();
 referencing.handleResolvedForwardReference(id, value);
 parent.set(ob, value);
 }
}