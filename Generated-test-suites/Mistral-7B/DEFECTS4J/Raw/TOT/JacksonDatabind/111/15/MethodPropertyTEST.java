package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MethodPropertyTest {

 private ObjectMapper objectMapper;
 private MethodProperty methodProperty;
 private AnnotatedMethod annotatedMethod;
 private Method setterMethod;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 annotatedMethod = mock(AnnotatedMethod.class);
 setterMethod = mock(Method.class);
 when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
 methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testInstance = new Object();
 Object expectedValue = new Object();
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "expectedValue");

 doNothing().when(setterMethod).invoke(testInstance, eq(expectedValue));

 methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode),
 objectMapper.getDeserializationContext(), testInstance);

 verify(setterMethod).invoke(testInstance, eq(expectedValue));
 }

 @Test
 public void testDeserializeAndSetNullValue() throws IOException {
 Object testInstance = new Object();
 methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(null),
 objectMapper.getDeserializationContext(), testInstance);

 Assert.assertNull(methodProperty.getProperty());
 }

 @Test
 public void testDeserializeAndSetNullProvider() throws IOException {
 Object testInstance = new Object();
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 doThrow(new RuntimeException("Null Value Provider Error")).when(nullValueProvider).getNullValue(any());
 methodProperty = new MethodProperty(null, null, null, nullValueProvider, annotatedMethod);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "nullValue");

 methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode),
 objectMapper.getDeserializationContext(), testInstance);

 verifyZeroInteractions(setterMethod);
 }

 @Test
 public void testDeserializeAndSetNullProviderThrowsException() throws IOException {
 Object testInstance = new Object();
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 doThrow(new RuntimeException("Null Value Provider Error")).when(nullValueProvider).getNullValue(any());
 methodProperty = new MethodProperty(null, null, null, nullValueProvider, annotatedMethod);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", null);

 try {
 methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode),
 objectMapper.getDeserializationContext(), testInstance);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeAndSetTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 methodProperty = new MethodProperty(null, null, typeDeserializer, null, annotatedMethod);
 Object testInstance = new Object();
 Object expectedValue = new Object();
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "expectedValue");
 when(typeDeserializer.deserialize(any(), any(), any())).thenReturn(expectedValue);

 methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode),
 objectMapper.getDeserializationContext(), testInstance);

 verify(typeDeserializer).deserialize(any(), any(), eq(methodProperty.getType()));
 verify(setterMethod).invoke(testInstance, eq(expectedValue));
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = new Object();
 methodProperty.set(testInstance, new Object());
 Assert.assertNotNull(methodProperty.getProperty());
 }

 @Test
 public void testSetNullValue() throws IOException {
 Object testInstance = new Object();
 methodProperty.set(testInstance, null);
 Assert.assertNull(methodProperty.getProperty());
 }

 @Test
 public void testSetNullProvider() throws IOException {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 methodProperty = new MethodProperty(null, null, null, nullValueProvider, annotatedMethod);
 methodProperty.set(new Object(), null);
 verifyZeroInteractions(setterMethod);
 }

 @Test
 public void testSetThrowsException() throws IOException {
 methodProperty = spy(MethodProperty.class);
 doThrow(new RuntimeException("Setter Exception")).when(methodProperty).set(any(), any());
 Object testInstance = new Object();
 try {
 methodProperty.set(testInstance, new Object());
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // expected
 }
 }
}