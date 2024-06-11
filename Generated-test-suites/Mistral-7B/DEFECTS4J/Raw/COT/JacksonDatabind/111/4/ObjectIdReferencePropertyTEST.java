package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema.Shape;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objIdReferenceProperty;
 private SettableBeanProperty forward;
 private ObjectIdInfo objectIdInfo;
 private JsonDeserializer<?> valueDeserializer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private NullValueProvider nullValueProvider;
 private Object pojo;

 @Before
 public void setup() throws Exception {
 objectIdInfo = mock(ObjectIdInfo.class);
 valueDeserializer = mock(JsonDeserializer.class);
 forward = mock(SettableBeanProperty.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 nullValueProvider = mock(NullValueProvider.class);
 objIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyConstructor() {
 // Default constructor
 assertNotNull(objIdReferenceProperty);
 // Constructor with SettableBeanProperty
 ObjectIdReferenceProperty objIdReferenceProperty1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertNotNull(objIdReferenceProperty1);
 assertSame(forward, objIdReferenceProperty1._forward);
 assertSame(objectIdInfo, objIdReferenceProperty1._objectIdInfo);
 // Constructor with ObjectIdReferenceProperty
 ObjectIdReferenceProperty objIdReferenceProperty2 = new ObjectIdReferenceProperty(objIdReferenceProperty, valueDeserializer, nullValueProvider);
 assertNotNull(objIdReferenceProperty2);
 assertSame(forward, objIdReferenceProperty2._forward);
 assertSame(objectIdInfo, objIdReferenceProperty2._objectIdInfo);
 assertSame(valueDeserializer, objIdReferenceProperty2._valueDeserializer);
 assertSame(nullValueProvider, objIdReferenceProperty2._nullProvider);
 // Constructor with ObjectIdReferenceProperty and new name
 ObjectIdReferenceProperty objIdReferenceProperty3 = new ObjectIdReferenceProperty(objIdReferenceProperty, new PropertyName("testName"));
 assertNotNull(objIdReferenceProperty3);
 assertSame(forward, objIdReferenceProperty3._forward);
 assertSame(objectIdInfo, objIdReferenceProperty3._objectIdInfo);
 assertSame(objIdReferenceProperty, objIdReferenceProperty3);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objIdReferenceProperty.getAnnotation(JsonDeserialize.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertSame(forward.getMember(), objIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 assertSame(forward.getCreatorIndex(), objIdReferenceProperty.getCreatorIndex());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithName() throws Exception {
 objIdReferenceProperty.withName(new PropertyName("testName"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueDeserializer() throws Exception {
 objIdReferenceProperty.withValueDeserializer(valueDeserializer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithNullProvider() throws Exception {
 objIdReferenceProperty.withNullProvider(nullValueProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 // No implementation needed
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = new Object();
 when(valueDeserializer.deserialize(jsonParser, deserializationContext, pojo)).thenReturn(id);
 when(forward.set(pojo, id)).thenReturn(id);
 objIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, pojo);
 assertSame(id, pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = new Object();
 when(valueDeserializer.deserialize(jsonParser, deserializationContext, pojo)).thenReturn(id);
 when(forward.setAndReturn(pojo, id)).thenReturn(id);
 Object result = objIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 assertSame(id, result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws Exception {
 when(valueDeserializer.deserialize(jsonParser, deserializationContext, pojo)).thenThrow(new UnresolvedForwardReference());
 objIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 }

 @Test
 public void testSet() throws Exception {
 Object id = new Object();
 objIdReferenceProperty.set(pojo, id);
 assertSame(id, pojo);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object id = new Object();
 Object result = objIdReferenceProperty.setAndReturn(pojo, id);
 assertSame(id, result);
 }

 @Test
 public void testPropertyReferringConstructor() {
 ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 Class<?> type = Object.class;
 Object ob = new Object();
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
 assertNotNull(propertyReferring);
 assertSame(parent, propertyReferring._parent);
 assertSame(ref, propertyReferring._referring);
 assertSame(type, propertyReferring._type);
 assertSame(ob, propertyReferring._pojo);
 }

 @Test
 public void testPropertyReferringHandleResolvedForwardReference() {
 Object id = new Object();
 Object value = new Object();
 when(parent.set(ob, value)).thenReturn(value);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objIdReferenceProperty, ref, Object.class, ob);
 propertyReferring.handleResolvedForwardReference(id, value);
 assertSame(value, ob);
 }
}