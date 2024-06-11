package com.example.junit.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

public class SettableAnyPropertyTest {

 private ObjectIdReader idReader = mock(ObjectIdReader.class);
 private JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
 private TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
 private KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.Test
 public void testConstructor() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertNotNull(settableAnyProperty);
 }

 @org.junit.Test
 public void testConstructorWithoutKeyDeserializer() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), valueDeserializer, valueTypeDeserializer);
 assertNotNull(settableAnyProperty);
 }

 @org.junit.Test
 public void testWithValueDeserializer() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer);
 SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);
 assertNotNull(settableAnyPropertyWithValueDeserializer);
 assertNotSame(settableAnyProperty, settableAnyPropertyWithValueDeserializer);
 }

 @org.junit.Test
 public void testFixAccess() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 settableAnyProperty.fixAccess(mock(DeserializationConfig.class));
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testReadResolveNullSetter() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
 }

 @org.junit.Test
 public void testGetProperty() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 BeanProperty actual = settableAnyProperty.getProperty();
 assertSame(_property, actual);
 }

 @org.junit.Test
 public void testHasValueDeserializer() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertTrue(settableAnyProperty.hasValueDeserializer());
 }

 @org.junit.Test
 public void testHasNoValueDeserializer() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer);
 assertFalse(settableAnyProperty.hasValueDeserializer());
 }

 @org.junit.Test
 public void testGetType() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertSame(_type, settableAnyProperty.getType());
 }

 @org.junit.Test
 public void testDeserializeAndSetNullParser() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object instance = mock(Object.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object propName = "propName";
 settableAnyProperty.deserializeAndSet(null, ctxt, instance, propName);
 assertNull(instance);
 }

 @org.junit.Test
 public void testDeserializeAndSetNotEmptyParser() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object instance = mock(Object.class);
 JsonParser p = mock(JsonParser.class);
 when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 String propName = "propName";
 when(p.getText()).thenReturn("testValue");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object key = propName;
 Object value = "testValue";
 settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
 assertEquals(value, instance);
 }

 @org.junit.Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeAndSetUnresolvedForwardReference() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object instance = mock(Object.class);
 JsonParser p = mock(JsonParser.class);
 Object id = mock(Object.class);
 when(idReader.deserializeKey(propName, ctxt)).thenReturn(id);
 when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 AnySetterReferring referring = mock(AnySetterReferring.class);
 when(id.getReferring()).thenReturn(referring);
 when(_valueDeserializer.getObjectIdReader()).thenReturn(idReader);
 Object propNameValue = "propName";
 when(p.nextValue()).thenReturn(propNameValue);
 Object value = "testValue";
 settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
 // should not reach here as test expects UnresolvedForwardReference exception
 fail();
 }

 @org.junit.Test
 public void testDeserialize() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object value = "testValue";
 when(p.getText()).thenReturn(value);
 Object result = settableAnyProperty.deserialize(p, ctxt);
 assertEquals(value, result);
 }

 @org.junit.Test
 public void testSetMap() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedField.class), mock(JavaType.class), null);
 Object instance = mock(Map.class);
 Object propName = "propName";
 Object value = "testValue";
 Map<Object,Object> map = (Map<Object,Object>) instance;
 map.put(propName, value);
 settableAnyProperty.set(instance, propName, value);
 assertEquals(value, map.get(propName));
 }

 @org.junit.Test(expected = IOException.class)
 public void testSetNullInstance() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object propName = "propName";
 Object value = "testValue";
 Object instance = null;
 settableAnyProperty.set(instance, propName, value);
 }

 @org.junit.Test(expected = IllegalAccessException.class)
 public void testSetNotAccessibleField() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, mock(AnnotatedField.class), mock(JavaType.class), null);
 Object propName = "propName";
 Object value = "testValue";
 Object instance = mock(Object.class);
 doThrow(new IllegalAccessException("Test Exception")).when(instance).setField(propName, value);
 settableAnyProperty.set(instance, propName, value);
 }

 @org.junit.Test
 public void testSetNotAccessibleMethod() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, mock(AnnotatedMethod.class), mock(JavaType.class), null);
 Object propName = "propName";
 Object value = "testValue";
 Object instance = mock(Object.class);
 doThrow(new IllegalAccessException("Test Exception")).when(instance).invokeMethod(propName, value);
 settableAnyProperty.set(instance, propName, value);
 }

 @org.junit.Test
 public void testThrowAsIOE() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object propName = "propName";
 Object value = "testValue";
 IOException e = new IOException("Test Exception");
 when(valueDeserializer.deserialize(p, ctxt)).thenThrow(e);
 settableAnyProperty.set(instance, propName, value);
 fail();
 }

 @org.junit.Test
 public void testThrowAsRTE() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object propName = "propName";
 Object value = "testValue";
 RuntimeException e = new RuntimeException("Test Exception");
 when(valueDeserializer.deserialize(p, ctxt)).thenThrow(e);
 settableAnyProperty.set(instance, propName, value);
 fail();
 }

 private static class AnySetterReferring extends Referring {
 private final SettableAnyProperty _parent;
 private final Object _pojo;
 private final String _propName;

 public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type, Object instance, String propName) {
 super(reference, type);
 _parent = parent;
 _pojo = instance;
 _propName = propName;
 }

 @Override
 public void handleResolvedForwardReference(Object id, Object value) throws IOException {
 super.handleResolvedForwardReference(id, value);
 _parent.set(_pojo, _propName, value);
 }
 }
}