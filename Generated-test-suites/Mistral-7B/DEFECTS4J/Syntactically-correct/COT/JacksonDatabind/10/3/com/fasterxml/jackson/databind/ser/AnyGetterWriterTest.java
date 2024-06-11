package com.example.tests;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMethod accessor;
 private MapSerializer mapSerializer;
 private SerializerProvider provider;
 private JsonGenerator gen;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMethod.class);
 mapSerializer = mock(MapSerializer.class);
 provider = mock(SerializerProvider.class);
 gen = mock(JsonGenerator.class);
 when(property.getName()).thenReturn("anyProperty");
 when(accessor.getName()).thenReturn("getAnyProperty");
 when(accessor.getValue(anyObject)).thenReturn(new HashMap<>());
 when(provider.handlePrimaryContextualization(mapSerializer, property)).thenReturn(mapSerializer);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object anyObject = new Object();
 anyGetterWriter.getAndSerialize(anyObject, gen, provider);
 // Verify that MapSerializer was called to serialize the map
 verify(mapSerializer).serializeFields((Map<?,?>) anyValue, eq(gen), eq(provider));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullValue() throws Exception {
 Object anyObject = new Object();
 anyGetterWriter.getAndSerialize(anyObject, gen, provider);
 when(accessor.getValue(anyObject)).thenReturn(null);
 anyGetterWriter.getAndSerialize(anyObject, gen, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object anyObject = new Object();
 anyGetterWriter.getAndSerialize(anyObject, gen, provider);
 when(accessor.getValue(anyObject)).thenReturn(new String("nonMapValue"));
 anyGetterWriter.getAndSerialize(anyObject, gen, provider);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = mock(PropertyFilter.class);
 Object anyObject = new Object();
 anyGetterWriter.getAndFilter(anyObject, gen, provider, filter);
 // Verify that MapSerializer was called to serialize the map with provided filter
 verify(mapSerializer).serializeFilteredFields((Map<?,?>) anyValue, eq(gen), eq(provider), eq(filter), any());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullValue() throws Exception {
 Object anyObject = new Object();
 anyGetterWriter.getAndFilter(anyObject, gen, provider, filter);
 when(accessor.getValue(anyObject)).thenReturn(null);
 anyGetterWriter.getAndFilter(anyObject, gen, provider, filter);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapValue() throws Exception {
 Object anyObject = new Object();
 anyGetterWriter.getAndFilter(anyObject, gen, provider, filter);
 when(accessor.getValue(anyObject)).thenReturn(new String("nonMapValue"));
 anyGetterWriter.getAndFilter(anyObject, gen, provider, filter);
 }

 @Test
 public void testResolve() throws Exception {
 SerializerProvider provider = mock(SerializerProvider.class);
 MapSerializer mapSerializer = mock(MapSerializer.class);
 when(provider.handlePrimaryContextualization(mapSerializer, property)).thenReturn(mapSerializer);
 anyGetterWriter.resolve(provider);
 // Verify that MapSerializer was assigned to the field
 assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
 }

}