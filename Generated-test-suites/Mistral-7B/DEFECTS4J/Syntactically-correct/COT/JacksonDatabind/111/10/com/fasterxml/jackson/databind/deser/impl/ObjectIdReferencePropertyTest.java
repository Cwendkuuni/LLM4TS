package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ObjectIdReferencePropertyTest {

 @Mock
 private JsonDeserializer<?> valueDeserializer;

 @Mock
 private JsonParser jsonParser;

 @Mock
 private DeserializationContext context;

 @Mock
 private Object instance;

 @Mock
 private AnnotatedMember annotatedMember;

 @Mock
 private ObjectIdInfo objectIdInfo;

 @Mock
 private NullValueProvider nullValueProvider;

 @Mock
 private PropertyName newName;

 @Mock
 private SettableBeanProperty settableBeanProperty;

 @Mock
 private ObjectIdReferenceProperty src;

 @Mock
 private PropertyName newPropertyName;

 @Mock
 private UnresolvedForwardReference unresolvedForwardReference;

 @Mock
 private Object roid;

 @Mock
 private Object id;

 @Mock
 private Object value;

 @InjectMocks
 private ObjectIdReferenceProperty objectIdReferenceProperty;

 @Before
 public void setup() {
 when(valueDeserializer.getClass()).thenReturn(JsonDeserializer.class);
 when(context.findValueDeserializer(Object.class)).thenReturn(valueDeserializer);
 when(context.findNullValueProvider()).thenReturn(nullValueProvider);
 when(jsonParser.getText()).thenReturn("testValue");
 }

 @Test
 public void testConstructor1() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, null);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferenceProperty.getClass());
 assertNull(objectIdReferenceProperty._forward);
 assertNull(objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructor2() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferenceProperty.getClass());
 assertEquals(settableBeanProperty, objectIdReferenceProperty._forward);
 assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructor3() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, valueDeserializer, nullValueProvider);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferenceProperty.getClass());
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
 assertEquals(objectIdReferenceProperty._objectIdInfo, objectIdReferenceProperty._forward._objectIdInfo);
 assertEquals(valueDeserializer, objectIdReferenceProperty._valueDeserializer);
 assertEquals(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testConstructor4() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferenceProperty.getClass());
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
 assertEquals(objectIdReferenceProperty._objectIdInfo, objectIdReferenceProperty._forward._objectIdInfo);
 assertEquals(newName, objectIdReferenceProperty.getName());
 }

 @Test
 public void testWithName() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newPropertyName);
 assertEquals(ObjectIdReferenceProperty.class, newObjectIdReferenceProperty.getClass());
 assertEquals(newPropertyName, newObjectIdReferenceProperty.getName());
 assertEquals(objectIdReferenceProperty, newObjectIdReferenceProperty._forward);
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(valueDeserializer);
 assertEquals(ObjectIdReferenceProperty.class, newObjectIdReferenceProperty.getClass());
 assertEquals(valueDeserializer, newObjectIdReferenceProperty._valueDeserializer);
 assertEquals(objectIdReferenceProperty, newObjectIdReferenceProperty._forward);
 }

 @Test
 public void testWithNullProvider() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
 assertEquals(ObjectIdReferenceProperty.class, newObjectIdReferenceProperty.getClass());
 assertEquals(nullValueProvider, newObjectIdReferenceProperty._nullProvider);
 assertEquals(objectIdReferenceProperty, newObjectIdReferenceProperty._forward);
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = mock(Annotation.class);
 when(settableBeanProperty.getAnnotation(Annotation.class)).thenReturn(annotation);
 when(objectIdReferenceProperty._forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 when(settableBeanProperty.getMember()).thenReturn(annotatedMember);
 assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeserializeAndSetWithNullInstance() throws IOException {
 objectIdReferenceProperty.deserializeAndSet(jsonParser, context, null);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode jsonNode = new ObjectMapper().createObjectNode();
 jsonNode.put("testKey", "testValue");
 Object testInstance = new Object();
 when(context.findBeanProperty(jsonParser.getCurrentName(), Object.class)).thenReturn(objectIdReferenceProperty);
 when(objectIdReferenceProperty.deserialize(jsonParser, context)).thenReturn("testValue");
 objectIdReferenceProperty.deserializeAndSet(jsonParser, context, testInstance);
 assertEquals("testValue", testInstance.getClass().getField("testKey").get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturnWithNullInstance() throws IOException {
 Object testInstance = new Object();
 assertEquals(null, objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, context, testInstance));
 }

 @Test
 public void testDeserializeSetAndReturnWithNotNullInstance() throws IOException {
 Object testInstance = new Object();
 when(objectIdReferenceProperty._forward.deserialize(jsonParser, context)).thenReturn("testValue");
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, context, testInstance);
 assertEquals("testValue", testInstance.getClass().getField("testKey").get(testInstance));
 assertEquals("testValue", result);
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = new Object();
 objectIdReferenceProperty.set(testInstance, "testValue");
 assertEquals("testValue", testInstance.getClass().getField("testKey").get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object testInstance = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(testInstance, "testValue");
 assertEquals("testValue", testInstance.getClass().getField("testKey").get(testInstance));
 assertEquals("testValue", result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
 Object testInstance = new Object();
 when(objectIdReferenceProperty._forward.getClass()).thenReturn(UnresolvedForwardReference.class);
 objectIdReferenceProperty.deserializeAndSet(jsonParser, context, testInstance);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 Object idInstance = new Object();
 when(unresolvedForwardReference.getId()).thenReturn(id);
 when(jsonParser.getValueAsToken()).thenReturn(id);
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 objectIdReferenceProperty.handleResolvedForwardReference(id, value);
 assertEquals(value, idInstance.getClass().getField("testKey").get(idInstance));
 }

 @Test
 public void testDeserialize() throws IOException {
 when(jsonParser.getValueAsToken().isValueContainerNode()).thenReturn(true);
 when(jsonParser.getValueAsToken().isNull()).thenReturn(false);
 when(jsonParser.getValueAsToken().isValueNode()).thenReturn(true);
 when(jsonParser.getValueAsToken().getValueAsString()).thenReturn("testValue");
 Object testInstance = new Object();
 when(context.findBeanProperty(jsonParser.getCurrentName(), Object.class)).thenReturn(objectIdReferenceProperty);
 Object result = objectIdReferenceProperty.deserialize(jsonParser, context);
 assertEquals("testValue", result);
 }

 @Test
 public void testDeserializeWithNullValue() throws IOException {
 when(jsonParser.getValueAsToken().isValueContainerNode()).thenReturn(true);
 when(jsonParser.getValueAsToken().isNull()).thenReturn(true);
 when(jsonParser.getValueAsToken().getValueAsString()).thenReturn(null);
 Object testInstance = new Object();
 when(context.findBeanProperty(jsonParser.getCurrentName(), Object.class)).thenReturn(objectIdReferenceProperty);
 Object result = objectIdReferenceProperty.deserialize(jsonParser, context);
 assertNull(result);
 }

 @Test
 public void testSerialize() throws IOException {
 JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = new ObjectMapper().createObjectNode();
 when(jsonSerializer.serializeValues(jsonNode, context)).thenReturn(jsonNode);
 StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class, jsonSerializer);
 when(objectIdReferenceProperty._valueDeserializer.getSerializer()).thenReturn(stdSerializer);
 when(jsonParser.getValueAsToken().isValueNode()).thenReturn(true);
 when(jsonParser.getValueAsToken().getValueAsString()).thenReturn("testValue");
 objectIdReferenceProperty.serialize(jsonParser, context);
 assertEquals("testValue", jsonNode.path("testKey").asText());
 }
}