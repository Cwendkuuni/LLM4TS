package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnyGetterWriterTest {

 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private Object bean;
 private JsonGenerator jsonGenerator;
 private SerializerProvider serializerProvider;
 private PropertyFilter propertyFilter;

 @Before
 public void setup() {
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 mapSerializer = mock(MapSerializer.class);
 bean = mock(Object.class);
 jsonGenerator = mock(JsonGenerator.class);
 serializerProvider = mock(SerializerProvider.class);
 propertyFilter = mock(PropertyFilter.class);

 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object value = Mockito.mock(Map.class);
 when(accessor.getValue(bean)).thenReturn(value);

 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeStartObject();
 _mapSerializer.serializeFields((Map<?,?>) value, jsonGenerator, serializerProvider);
 verify(jsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithInvalidValue() throws Exception {
 when(accessor.getValue(bean)).thenReturn(new Object());

 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object value = Mockito.mock(Map.class);
 when(accessor.getValue(bean)).thenReturn(value);

 anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

 verify(jsonGenerator).writeStartObject();
 _mapSerializer.serializeFilteredFields((Map<?,?>) value, jsonGenerator, serializerProvider, propertyFilter, null);
 verify(jsonGenerator).writeEndObject();
 }

 @Test
 public void testResolve() throws JsonMappingException {
 anyGetterWriter.resolve(serializerProvider);

 verify(serializerProvider, Mockito.times(1)).handlePrimaryContextualization(_mapSerializer, property);
 }
}