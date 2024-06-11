package com.example.junit.test.anygetterwriter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 property = Mockito.mock(BeanProperty.class);
 accessor = Mockito.mock(AnnotatedMember.class);
 mapSerializer = Mockito.mock(MapSerializer.class);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize_NullValue() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);

 anyGetterWriter.getAndSerialize(bean, gen, provider);
 // Nothing should be serialized since the value is null
 Mockito.verify(gen, Mockito.never()).writeStartObject();
 }

 @Test
 public void testGetAndSerialize_EmptyMap() throws IOException, JsonMappingException {
 Object bean = new HashMap<>();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);

 anyGetterWriter.getAndSerialize(bean, gen, provider);
 // The empty map should be serialized
 Mockito.verify(gen).writeStartObject();
 }

 @Test
 public void testGetAndSerialize_NonMapValue() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 Object nonMapValue = new Object();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);

 Mockito.when(accessor.getValue(bean)).thenReturn(nonMapValue);

 anyGetterWriter.getAndSerialize(bean, gen, provider);
 // JsonMappingException should be thrown since the value is not a Map
 Mockito.verify(provider).reportError(Mockito.isInstanceOf(JsonMappingException.class));
 }

 @Test
 public void testGetAndSerialize_NullMapSerializer() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 Object mapValue = new HashMap<>();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);

 anyGetterWriter.getAndSerialize(bean, gen, provider);
 // JsonMappingException should be thrown since mapSerializer is null
 Mockito.verify(provider).reportError(Mockito.isInstanceOf(JsonMappingException.class));
 }

 @Test
 public void testGetAndFilter_NullValue() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 // Nothing should be serialized since the value is null
 Mockito.verify(gen, Mockito.never()).writeStartObject();
 }

 @Test
 public void testGetAndFilter_EmptyMap() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = new HashMap<>();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 // The empty map should be serialized
 Mockito.verify(gen).writeStartObject();
 }

 @Test
 public void testGetAndFilter_NonMapValue() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 Object nonMapValue = new Object();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);

 Mockito.when(accessor.getValue(bean)).thenReturn(nonMapValue);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 // JsonMappingException should be thrown since the value is not a Map
 Mockito.verify(provider).reportError(Mockito.isInstanceOf(JsonMappingException.class));
 }

 @Test
 public void testGetAndFilter_NullMapSerializer() throws IOException, JsonGenerationException, JsonMappingException {
 Object bean = Mockito.mock(Object.class);
 Object mapValue = new HashMap<>();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 // JsonMappingException should be thrown since mapSerializer is null
 Mockito.verify(provider).reportError(Mockito.isInstanceOf(JsonMappingException.class));
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);

 anyGetterWriter.resolve(provider);
 // mapSerializer should be resolved
 Mockito.verify(provider).handlePrimaryContextualization(Mockito.isInstanceOf(MapSerializer.class), Mockito.eq(property));
 }
}