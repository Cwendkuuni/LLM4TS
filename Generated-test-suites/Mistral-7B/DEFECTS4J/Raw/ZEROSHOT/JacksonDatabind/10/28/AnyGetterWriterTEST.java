package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Test class for AnyGetterWriter
 */
public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanProperty beanProperty;
 private AnnotatedMember annotatedMember;
 private MapSerializer mapSerializer;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 beanProperty = mock(BeanProperty.class);
 annotatedMember = mock(AnnotatedMember.class);
 mapSerializer = mock(MapSerializer.class);
 when(beanProperty.getName()).thenReturn("anyProperty");
 when(beanProperty.getMember()).thenReturn(null);
 when(beanProperty.getAnnotations()).thenReturn(new JsonAnyGetter());
 when(annotatedMember.getAnnotation(JsonAnyGetter.class)).thenReturn(new JsonAnyGetter());
 when(annotatedMember.getName()).thenReturn("getValue");
 givenAnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new Object();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 when(annotatedMember.getValue(bean)).thenReturn(map);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("anyProperty");
 verify(jsonGenerator).writeStartObject();
 // verify serialization of map fields
 Map<String, Object> serializedMap = objectMapper.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toByteArray()), Map.class);
 assertEquals(map, serializedMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerialize_ValueNotMap() throws Exception {
 Object bean = new Object();
 when(annotatedMember.getValue(bean)).thenReturn("not a map");

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new Object();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 when(annotatedMember.getValue(bean)).thenReturn(map);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 PropertyFilter propertyFilter = mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("anyProperty");
 verify(jsonGenerator).writeStartObject();
 // verify serialization of filtered map fields
 Map<String, Object> serializedMap = objectMapper.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toByteArray()), Map.class);
 assertEquals(map, serializedMap);
 }

 @Test
 public void testResolve() throws Exception {
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 anyGetterWriter.resolve(serializerProvider);

 SerializerProvider serializerProviderResolved = serializerProvider;
 if (mapSerializer != null) {
 serializerProviderResolved = objectMapper.getSerializerProvider();
 when(serializerProviderResolved.handlePrimaryContextualization(mapSerializer, beanProperty)).thenReturn(mapSerializer);
 }

 assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
 }

 private void givenAnyGetterWriter(BeanProperty beanProperty, AnnotatedMember annotatedMember, MapSerializer mapSerializer) {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
 this.anyGetterWriter = anyGetterWriter;
 }
}