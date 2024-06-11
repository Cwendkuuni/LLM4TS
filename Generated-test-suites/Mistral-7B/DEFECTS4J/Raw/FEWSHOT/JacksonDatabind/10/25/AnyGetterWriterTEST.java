package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest extends TestBase {

 public void testAnyGetterWriter_getAndSerialize_MapInstance() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();

 when(accessor.getValue(bean)).thenReturn(new HashMap<>());
 when(mapSerializer.serializeFields((Map<?,?>) anyValue(), gen, provider)).thenAnswer(i -> {
 // Empty implementation to avoid real serialization
 return null;
 });

 anyGetterWriter.getAndSerialize(bean, gen, provider);

 verify(gen).writeStartObject();
 verify(gen).writeFieldName(_property.getName());
 verify(mapSerializer).serializeFields((Map<?,?>) anyValue(), gen, provider);
 verify(gen).writeEndObject();
 }

 public void testAnyGetterWriter_getAndSerialize_NullValue() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();

 when(accessor.getValue(bean)).thenReturn(null);

 anyGetterWriter.getAndSerialize(bean, gen, provider);

 verify(gen, never()).writeStartObject();
 }

 public void testAnyGetterWriter_getAndSerialize_NonMapValue() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();

 when(accessor.getValue(bean)).thenReturn(Integer.valueOf(42));

 assertThrows(JsonMappingException.class, () -> {
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 });
 }

 public void testAnyGetterWriter_getAndSerialize_NullMapSerializer() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();

 anyGetterWriter.getAndSerialize(bean, gen, provider);

 verify(gen, never()).writeStartObject();
 }

 // test getAndFilter method

 public void testAnyGetterWriter_getAndFilter_MapInstance() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();
 final PropertyFilter filter = mock(PropertyFilter.class);
 final Map<String, Object> value = new HashMap<>();
 value.put("foo", "bar");
 when(accessor.getValue(bean)).thenReturn(value);
 when(mapSerializer.serializeFilteredFields((Map<?,?>) anyValue(), gen, provider, filter, null)).thenAnswer(i -> {
 // Empty implementation to avoid real serialization
 return null;
 });

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 verify(gen).writeStartObject();
 verify(gen).writeFieldName(_property.getName());
 // Verify that filtering is working
 verify(mapSerializer).serializeFilteredFields((Map<?,?>) anyValue(), gen, provider, filter, null);
 verify(gen).writeEndObject();
 }

 public void testAnyGetterWriter_getAndFilter_NullValue() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();
 final PropertyFilter filter = mock(PropertyFilter.class);

 when(accessor.getValue(bean)).thenReturn(null);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 verify(gen, never()).writeStartObject();
 }

 public void testAnyGetterWriter_getAndFilter_NonMapValue() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();
 final PropertyFilter filter = mock(PropertyFilter.class);

 when(accessor.getValue(bean)).thenReturn(Integer.valueOf(42));

 assertThrows(JsonMappingException.class, () -> {
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 });
 }

 public void testAnyGetterWriter_getAndFilter_NullMapSerializer() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
 final Object bean = new Object();
 final JsonGenerator gen = createJsonGenerator();
 final SerializerProvider provider = createSerializerProvider();
 final PropertyFilter filter = mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 verify(gen, never()).writeStartObject();
 }

 // test resolve method

 public void testAnyGetterWriter_resolve_MapSerializer() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final MapSerializer mapSerializer = mock(MapSerializer.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 final SerializerProvider provider = createSerializerProvider();

 anyGetterWriter.resolve(provider);

 verify(provider).handlePrimaryContextualization(mapSerializer, property);
 }

 public void testAnyGetterWriter_resolve_NullMapSerializer() throws Exception {
 final ObjectMapper mapper = new ObjectMapper();
 final BeanProperty property = mock(BeanProperty.class);
 final AnnotatedMember accessor = mock(AnnotatedMember.class);
 final AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
 final SerializerProvider provider = createSerializerProvider();

 anyGetterWriter.resolve(provider);

 verify(provider, never()).handlePrimaryContextualization(anything(), anything());
 }

 private Object anyValue() {
 return Map.of("foo", "bar");
 }

 private SerializerProvider createSerializerProvider() {
 return new SerializerProvider() {
 @Override
 public SerializerFinder getSerializerFinder() {
 return new BaseSerializerFinder();
 }

 @Override
 public BeanDescription getBeanDescription() {
 return new BeanDescription();
 }
 };
 }

 private JsonGenerator createJsonGenerator() throws IOException {
 final ObjectWriter writer = mapper.<StringWriter>writerFor(StringWriter.class);
 return writer.getJsonGenerator();
 }

 private static Object anything() {
 return ClassUtil.constructUsingDefaults(Object.class);
 }
}