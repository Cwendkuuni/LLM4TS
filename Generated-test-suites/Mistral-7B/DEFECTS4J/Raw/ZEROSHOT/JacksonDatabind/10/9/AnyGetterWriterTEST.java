package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanProperty beanProperty;
 private AnnotatedMember annotatedMember;
 private AnyGetterWriter anyGetterWriter;
 private MapSerializer mapSerializer;
 private SerializerProvider serializerProvider;
 private JsonGenerator jsonGenerator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 beanProperty = mock(BeanProperty.class);
 annotatedMember = mock(AnnotatedMember.class);
 when(beanProperty.getName()).thenReturn(new PropertyName("anyProperty"));
 when(annotatedMember.getValue(anyObject)).thenReturn(new HashMap<>());
 when(annotatedMember.getName()).thenReturn(new PropertyName("anyProperty"));
 when(annotatedMember.getMember()).thenReturn(null);
 mapSerializer = new StdSerializer<Map<String, Object>>(Map.class) {};
 mapSerializer.setEmptyValueSerializationEnabled(true);
 serializerProvider = mock(SerializerProvider.class);
 serializerProvider.findValueSerializer(Map.class, null, null);
 when(serializerProvider.findValueSerializer(Map.class, null, null)).thenReturn(mapSerializer);
 jsonGenerator = mock(JsonGenerator.class);
 }

 @Test
 public void testGetAndSerialize() throws JsonMappingException, IOException {
 // Given
 Object anyObject = new Object();
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);

 // When
 anyGetterWriter.getAndSerialize(anyObject, jsonGenerator, serializerProvider);

 // Then
 assertNotNull(jsonGenerator);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonMappingException, IOException {
 // Given
 Object anyObject = new Object();
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
 when(annotatedMember.getValue(anyObject)).thenReturn(null);

 // When
 anyGetterWriter.getAndSerialize(anyObject, jsonGenerator, serializerProvider);

 // Then
 // JsonMappingException should be thrown
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws JsonMappingException, IOException {
 // Given
 Object anyObject = new Object();
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
 when(annotatedMember.getValue(anyObject)).thenReturn("non-map-value");

 // When
 anyGetterWriter.getAndSerialize(anyObject, jsonGenerator, serializerProvider);

 // Then
 // JsonMappingException should be thrown
 }

 @Test
 public void testGetAndFilter() throws JsonMappingException, IOException {
 // Given
 Object anyObject = new Object();
 PropertyFilter filter = mock(PropertyFilter.class);
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);

 // When
 anyGetterWriter.getAndFilter(anyObject, jsonGenerator, serializerProvider, filter);

 // Then
 // No exception should be thrown
 }

 @Test
 public void testResolve() throws JsonMappingException {
 // Given
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);

 // When
 anyGetterWriter.resolve(serializerProvider);

 // Then
 // No exception should be thrown
 }
}