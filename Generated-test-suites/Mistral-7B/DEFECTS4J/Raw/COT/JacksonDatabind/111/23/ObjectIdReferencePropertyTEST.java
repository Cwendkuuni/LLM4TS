package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty property;
 private JsonDeserializer<?> valueDeserializer;
 private NullValueProvider nva;
 private PropertyName newName;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object instance;
 private Object referenceId;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectIdInfo = new ObjectIdInfo(new Annotation() {}, "testId", null);
 forward = new SettableBeanProperty(new AnnotatedMemberMock(), null);
 valueDeserializer = new JsonDeserializerMock();
 nva = new NullValueProvider();
 newName = new PropertyName("testProperty");
 jsonParser = new JsonTestParser();
 deserializationContext = new DeserializationTestContext();
 instance = new Object();
 property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyConstructor() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(property, valueDeserializer, nva);
 assertEquals(property, objectIdReferenceProperty.getBackReference());
 assertEquals(valueDeserializer, objectIdReferenceProperty.getValueDeserializer());
 assertEquals(nva, objectIdReferenceProperty.getNullProvider());
 assertEquals(objectIdInfo, objectIdReferenceProperty.getObjectIdInfo());
 }

 @Test
 public void testObjectIdReferencePropertyConstructor2() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(property, newName);
 assertEquals(property, objectIdReferenceProperty.getBackReference());
 assertEquals(property.getName(), objectIdReferenceProperty.getName());
 }

 @Test
 public void testObjectIdReferencePropertyConstructor3() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(property, deserializationContext);
 assertEquals(property, objectIdReferenceProperty.getBackReference());
 assertEquals(deserializationContext, objectIdReferenceProperty.getDeserializationContext());
 }

 @Test
 public void testSettableBeanPropertyWithName() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
 ObjectIdReferenceProperty objectIdReferenceProperty2 = objectIdReferenceProperty1.withName(newName);
 assertEquals(objectIdReferenceProperty1, objectIdReferenceProperty2.getBackReference());
 assertEquals(newName, objectIdReferenceProperty2.getName());
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializer() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
 ObjectIdReferenceProperty objectIdReferenceProperty2 = objectIdReferenceProperty1.withValueDeserializer(valueDeserializer);
 assertEquals(objectIdReferenceProperty1, objectIdReferenceProperty2.getBackReference());
 assertEquals(objectIdReferenceProperty2, objectIdReferenceProperty1.withValueDeserializer(valueDeserializer));
 assertEquals(valueDeserializer, objectIdReferenceProperty2.getValueDeserializer());
 }

 @Test
 public void testSettableBeanPropertyWithNullProvider() throws Exception {
 ObjectIdReferenceProperty objectIdReferenceProperty1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
 ObjectIdReferenceProperty objectIdReferenceProperty2 = objectIdReferenceProperty1.withNullProvider(nva);
 assertEquals(objectIdReferenceProperty1, objectIdReferenceProperty2.getBackReference());
 assertEquals(nva, objectIdReferenceProperty2.getNullProvider());
 }

 @Test
 public void testFixAccess() throws Exception {
 property.fixAccess(deserializationContext);
 forward.fixAccess(deserializationContext);
 assertEquals(deserializationContext, property.getDeserializationContext());
 assertEquals(deserializationContext, forward.getDeserializationContext());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertEquals(new Annotation() {}, property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertEquals(forward.getMember(), property.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 assertEquals(-1, property.getCreatorIndex());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetThrowsUnsupportedOperationException() throws Exception {
 property.deserializeAndSet(jsonParser, deserializationContext, instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeSetAndReturnThrowsUnsupportedOperationException() throws Exception {
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 }

 @Test
 public void testSet() throws Exception {
 property.set(instance, null);
 assertNull(instance);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 property.setAndReturn(instance, null);
 assertNull(instance);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertNull(instance);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws Exception {
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 reference.getRoid().appendReferring(new PropertyReferring(property, reference, Object.class, instance));
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 fail("Expected UnresolvedForwardReference exception");
 }

 @Test
 public void testHasId() throws Exception {
 ObjectNode jsonNode = jsonParser.readValueAsTree(ObjectMap.parse("{ 'id': 1 }"));
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertTrue(property.hasId(1));
 }

 @Test
 public void testHandleResolvedForwardReference() throws Exception {
 Object referenceObject = new Object();
 ObjectNode jsonNode = jsonParser.readValueAsTree(ObjectMap.parse("{ '@ref': { 'id': 1, '_class': 'com.fasterxml.jackson.databind.deser.impl.ObjectIdReferencePropertyTest$ObjectRef' } }"));
 property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 PropertyReferring referencing = new PropertyReferencing(property, null, Object.class, instance);
 referencing.handleResolvedForwardReference(1, referenceObject);
 assertEquals(referenceObject, instance);
 }
}