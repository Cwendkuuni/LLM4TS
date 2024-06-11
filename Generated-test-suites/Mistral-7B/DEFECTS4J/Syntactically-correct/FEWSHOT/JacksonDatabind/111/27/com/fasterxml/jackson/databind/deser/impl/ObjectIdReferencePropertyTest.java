package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

 private static final JsonDeserializer<Object> NO_OP_DESERIALIZER = new StdDeserializer<Object>() {};
 private static final NullValueProvider NULL_VALUE_PROVIDER = new StdNullValueProvider() {};

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 SettableBeanProperty forward = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals(forward, property._forward);
 assertEquals(objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcAndDeserializer() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, NO_OP_DESERIALIZER, NULL_VALUE_PROVIDER);
 assertEquals(src, property._forward);
 assertNull(property._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcAndNewName() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 PropertyName newName = PropertyName.construct("bar");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
 assertEquals(src, property);
 assertEquals(newName, property.getName());
 }

 @Test
 public void testSettableBeanPropertyMethods() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);

 assertEquals(src, property._forward);
 assertEquals(objectIdInfo, property._objectIdInfo);

 ObjectIdReferenceProperty propertyWithName = property.withName(PropertyName.construct("bar"));
 assertEquals(property, propertyWithName._forward);
 assertEquals(PropertyName.construct("bar"), propertyWithName.getName());

 ObjectIdReferenceProperty propertyWithDeserializer = property.withValueDeserializer(NO_OP_DESERIALIZER);
 assertEquals(property, propertyWithDeserializer._forward);
 assertEquals(NO_OP_DESERIALIZER, propertyWithDeserializer._valueDeserializer);

 ObjectIdReferenceProperty propertyWithNullProvider = property.withNullProvider(NULL_VALUE_PROVIDER);
 assertEquals(property, propertyWithNullProvider._forward);
 assertEquals(NULL_VALUE_PROVIDER, propertyWithNullProvider._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 Object obj = new Object();
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 src.set(obj, "bar");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 PropertyName propertyName = PropertyName.construct("bar");
 property = property.withName(propertyName);

 ObjectIdReferenceProperty propertyWithForward = new ObjectIdReferenceProperty(property, NO_OP_DESERIALIZER, NULL_VALUE_PROVIDER);
 assertSame(property, propertyWithForward._forward);
 propertyWithForward.fixAccess(new DeserializationConfig());

 Field accessorField = ObjectIdReferenceProperty.class.getDeclaredField("_forward");
 accessorField.setAccessible(true);
 Object forward = accessorField.get(propertyWithForward);
 assertSame(src, forward);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Class<Annotation> annotationType = Mockito.mock(Annotation.class);
 assertEquals(src.getAnnotation(annotationType), property.getAnnotation(annotationType));
 }

 @Test
 public void testGetMember() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 assertEquals(src.getMember(), property.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 assertEquals(src.getCreatorIndex(), property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Object obj = new Object();

 JsonParser p = new TestJsonParser();
 DeserializationContext ctxt = new TestDeserializationContext();
 property.deserializeAndSet(p, ctxt, obj);
 assertEquals("bar", src.get(obj));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Object obj = new Object();

 String value = "bar";
 ObjectNode node = new ObjectMapper().readTree(value);
 JsonParser p = new TestJsonParser(node);
 DeserializationContext ctxt = new TestDeserializationContext();

 Object result = property.deserializeSetAndReturn(p, ctxt, obj);
 assertEquals(value, src.get(obj));
 assertEquals(value, result);
 }

 @Test
 public void testSet() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Object obj = new Object();

 String value = "bar";
 property.set(obj, value);
 assertEquals(value, src.get(obj));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Object obj = new Object();

 String value = "bar";
 Object result = property.setAndReturn(obj, value);
 assertEquals(value, result);
 assertEquals(value, src.get(obj));
 }

 @Test
 public void testHandleResolvedForwardReference() throws Exception {
 SettableBeanProperty src = new StdSettableBeanProperty(Object.class, "foo");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.TYPE, "id");
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, objectIdInfo);
 Object obj = new Object();
 Object refId = new Object();

 Object value = "bar";
 UnresolvedForwardReference reference = new UnresolvedForwardReference(refId, property.getType().getRawClass(), value);
 PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, property.getType().getRawClass(), obj);
 property.handleResolvedForwardReference(refId, value);
 assertEquals(value, src.get(obj));
 }
}