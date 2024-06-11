package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Test;

public class AnyGetterWriterTest {

 private AnyGetterWriter getTestSubject(BeanProperty property, AnnotatedMember accessor, MapSerializer serializer) {
 return new AnyGetterWriter(property, accessor, serializer);
 }

 @Test
 public void testGetAndSerialize_NullBean() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 assertNull(subject.getAndSerialize(null, null, null));
 }

 @Test
 public void testGetAndSerialize_NullValue() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 Object bean = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 subject.getAndSerialize(bean, gen, provider);
 verify(gen, never()).writeStartObject();
 verify(gen, never()).writeFieldName("");
 verify(gen, never()).writeValue(any());
 }

 @Test
 public void testGetAndSerialize_NotInstanceOfMap() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 Object bean = new Object();
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 subject.getAndSerialize(bean, gen, provider);
 String expectedErrorMessage = "Value returned by 'any-getter' () not java.util.Map but ";
 verify(provider, times(1)).handleUnexpectedType(_accessor.getAnnotation(JsonAnyGetter.class), value.getClass(), Object.class);
 assertNotNull(provider.getCurrentException());
 assertEquals(expectedErrorMessage + value.getClass().getName(), provider.getCurrentException().getMessage());
 }

 @Test
 public void testGetAndSerialize_NullMapper() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 Object bean = new Object();
 Object value = new HashMap<>();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 subject.getAndSerialize(bean, gen, provider);
 assertFalse(gen.isWritingValue());
 }

 @Test
 public void testGetAndSerialize_MapSerializerSerializeFields() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, new MapSerializer(new SimpleModule(), null));
 Object bean = new Object();
 Object value = new HashMap<>();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 subject.getAndSerialize(bean, gen, provider);
 verify(gen, times(1)).writeStartObject();
 // Write field names and values based on the actual MapSerializer implementation
 // ...
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilter_NullBean() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 subject.getAndFilter(null, null, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilter_NullValue() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 Object bean = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 subject.getAndFilter(bean, gen, provider, filter);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilter_NotInstanceOfMap() throws Exception {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 Object bean = new Object();
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 subject.getAndFilter(bean, gen, provider, filter);
 String expectedErrorMessage = "Value returned by 'any-getter' () not java.util.Map but ";
 verify(provider, times(1)).handleUnexpectedType(_accessor.getAnnotation(JsonAnyGetter.class), value.getClass(), Object.class);
 assertNotNull(provider.getCurrentException());
 assertEquals(expectedErrorMessage + value.getClass().getName(), provider.getCurrentException().getMessage());
 }

 @Test
 public void testResolve_NullProvider() throws JsonMappingException {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 assertSame(subject._mapSerializer, subject.resolve(null));
 }

 @Test
 public void testResolve_NullMapper() throws JsonMappingException {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 SerializerProvider provider = mock(SerializerProvider.class);
 assertNull(subject.resolve(provider));
 }

 @Test
 public void testResolve_NotPrimaryContextualization() throws JsonMappingException {
 AnyGetterWriter subject = getTestSubject(null, null, null);
 SerializerProvider provider = mock(SerializerProvider.class);
 provider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class));
 assertNull(subject.resolve(provider));
 }

 @Test
 public void testResolve_PrimaryContextualization() throws JsonMappingException {
 AnyGetterWriter subject = getTestSubject(null, null, new MapSerializer(new SimpleModule(), null));
 SerializerProvider provider = mock(SerializerProvider.class);
 provider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class));
 assertEquals(subject._mapSerializer, subject.resolve(provider));
 }

}