package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;
import com.fasterxml.jackson.databind.ser.test.TestBean;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;

 @Before
 public void setUp() throws Exception {
 super.setUp();

 JavaType mapType = mapSerializer(Map.class, String.class, String.class);
 property = new BeanProperty("mapProperty", accessor(TestBean.class, "anyMap"), mapType);
 accessor = super.accessor("anyMap", TestBean.class);
 mapSerializer = new MapSerializer(objectMapper.getConfig(), mapType);

 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws JsonProcessingException, JsonMappingException, IOException {
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<>());
 bean.getAnyMap().put("key1", "value1");
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 actualBytes = gen.getOutputBuffer().getBytes();
 } finally {
 gen.close();
 }

 String expectedJson = "{\"key1\":\"value1\"}";
 assertArrayEquals(expectedJson.getBytes(), actualBytes);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonProcessingException, JsonMappingException, IOException {
 Object bean = new TestBean();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }
 // Test should fail here
 }

 @Test
 public void testGetAndSerializeWithNullMapSerializer() throws JsonProcessingException, JsonMappingException, IOException {
 AnyGetterWriter anyGetterWriterWithNullMapSerializer = new AnyGetterWriter(property, accessor, null);
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<>());
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriterWithNullMapSerializer.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }
 assertNull(actualBytes);
 }

 @Test
 public void testGetAndFilter() throws JsonProcessingException, JsonMappingException, IOException {
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<>());
 bean.getAnyMap().put("key1", "value1");
 PropertyFilter filter = new Filter();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 actualBytes = gen.getOutputBuffer().getBytes();
 } finally {
 gen.close();
 }

 String expectedJson = "{\"key1\":\"value1\"}";
 assertArrayEquals(expectedJson.getBytes(), actualBytes);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws JsonProcessingException, JsonMappingException, IOException {
 Object bean = new TestBean();
 PropertyFilter filter = new Filter();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 } finally {
 gen.close();
 }
 // Test should fail here
 }

 @Test
 public void testGetAndFilterWithEmptyMap() throws JsonProcessingException, JsonMappingException, IOException {
 Object bean = new TestBean();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), new Filter());
 actualBytes = gen.getOutputBuffer().getBytes();
 } finally {
 gen.close();
 }
 assertArrayEquals(new byte[0], actualBytes);
 }

 @Test
 public void testGetAndFilterWithNullMapSerializer() throws JsonProcessingException, JsonMappingException, IOException {
 AnyGetterWriter anyGetterWriterWithNullMapSerializer = new AnyGetterWriter(property, accessor, null);
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<>());
 PropertyFilter filter = new Filter();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriterWithNullMapSerializer.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 } finally {
 gen.close();
 }
 assertNull(actualBytes);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 Assert.assertNotNull(anyGetterWriter.getMapSerializer());
 }

 @Test
 public void testGetAndSerializeWithSerializationFeatureDisabled() throws JsonProcessingException, JsonMappingException, IOException {
 ObjectMapper objectMapperWithDisabledSerializationFeature = copyObjectMapper();
 objectMapperWithDisabledSerializationFeature.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 AnyGetterWriter anyGetterWriterWithDisabledSerializationFeature = new AnyGetterWriter(property, accessor, mapSerializer);
 Object bean = new TestBean();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator(objectMapperWithDisabledSerializationFeature);
 try {
 anyGetterWriterWithDisabledSerializationFeature.getAndSerialize(bean, gen, objectMapperWithDisabledSerializationFeature.getSerializerProvider());
 actualBytes = gen.getOutputBuffer().getBytes();
 } finally {
 gen.close();
 }
 assertArrayEquals(new byte[0], actualBytes);
 }

 @Test
 public void testGetAndFilterWithSerializationFeatureDisabled() throws JsonProcessingException, JsonMappingException, IOException {
 ObjectMapper objectMapperWithDisabledSerializationFeature = copyObjectMapper();
 objectMapperWithDisabledSerializationFeature.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 AnyGetterWriter anyGetterWriterWithDisabledSerializationFeature = new AnyGetterWriter(property, accessor, mapSerializer);
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<>());
 PropertyFilter filter = new Filter();
 byte[] actualBytes = null;
 JsonGenerator gen = createJsonGenerator(objectMapperWithDisabledSerializationFeature);
 try {
 anyGetterWriterWithDisabledSerializationFeature.getAndFilter(bean, gen, objectMapperWithDisabledSerializationFeature.getSerializerProvider(), filter);
 actualBytes = gen.getOutputBuffer().getBytes();
 } finally {
 gen.close();
 }
 assertArrayEquals(new byte[0], actualBytes);
 }
}

 class Filter implements PropertyFilter {
 @Override
 public void serializeValue(Map.Entry<String, Object> entry, JsonGenerator gen, SerializerProvider serializers)
 throws IOException, JsonProcessingException {
 // Do nothing
 }

 @Override
 public boolean include(Map.Entry<String, Object> entry) {
 return true;
 }
}