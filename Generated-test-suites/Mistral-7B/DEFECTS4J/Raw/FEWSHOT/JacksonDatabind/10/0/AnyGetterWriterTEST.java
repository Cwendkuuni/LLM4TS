package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.SerializerImpl;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

 AnnotatedMember accessor;
 BeanProperty property;
 AnyGetterWriter anyGetterWriter;
 ObjectMapper objectMapper;
 MapSerializer mapSerializer;
 SerializerProvider serializerProvider;
 Object testObject;

 @Before
 public void setup() {
 accessor = mock(AnnotatedMember.class);
 property = mock(BeanProperty.class);
 objectMapper = new ObjectMapper();
 mapSerializer = mock(MapSerializer.class);
 serializerProvider = mock(SerializerProvider.class);
 testObject = new Object();
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object value = new Object();
 given(accessor.getValue(testObject)).willReturn(value);
 given(value instanceof Map).willReturn(true);

 JsonGenerator gen = mock(JsonGenerator.class);
 when(mapSerializer.serializeFields((Map<?,?>) value, gen, serializerProvider)).thenCallRealMethod();

 anyGetterWriter.getAndSerialize(testObject, gen, serializerProvider);
 verify(mapSerializer).serializeFields((Map<?,?>) value, gen, serializerProvider);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeValueNotMap() throws Exception {
 Object value = new Object();
 given(accessor.getValue(testObject)).willReturn(value);
 given(value instanceof Map).willReturn(false);

 JsonGenerator gen = mock(JsonGenerator.class);
 anyGetterWriter.getAndSerialize(testObject, gen, serializerProvider);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object value = new ObjectNode(objectMapper.createObjectNode());
 given(accessor.getValue(testObject)).willReturn(value);
 given(value instanceof Map).willReturn(true);
 PropertyFilter filter = mock(PropertyFilter.class);

 JsonGenerator gen = mock(JsonGenerator.class);
 when(mapSerializer.serializeFilteredFields((Map<?,?>) value, gen, serializerProvider, filter, null)).thenCallRealMethod();

 anyGetterWriter.getAndFilter(testObject, gen, serializerProvider, filter);
 verify(mapSerializer).serializeFilteredFields((Map<?,?>) value, gen, serializerProvider, filter, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterValueNotMap() throws Exception {
 Object value = new Object();
 given(accessor.getValue(testObject)).willReturn(value);
 given(value instanceof Map).willReturn(false);
 PropertyFilter filter = mock(PropertyFilter.class);

 JsonGenerator gen = mock(JsonGenerator.class);
 anyGetterWriter.getAndFilter(testObject, gen, serializerProvider, filter);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = new SerializerProvider() {
 @Override
 public <T> T findValueSerializer(Class<T> type) {
 return null;
 }

 @Override
 public <T> T findValueSerializer(Class<?> rawType, Class<?> implementationType) {
 return null;
 }
 };

 given(serializerProvider.handlePrimaryContextualization(anyInstance(), any()))
 .willReturn(mapSerializer);

 anyGetterWriter.resolve(serializerProvider);
 verify(mapSerializer).__init__(objectMapper.getFactory(), property);
 }
}