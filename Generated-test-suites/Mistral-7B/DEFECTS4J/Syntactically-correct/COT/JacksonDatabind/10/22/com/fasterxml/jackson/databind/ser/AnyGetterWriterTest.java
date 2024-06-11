package com.example.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertyFilterMapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

public class AnyGetterWriterTest {

 private static class TestBean {
 @JsonAnyGetter
 Map<String, Object> getMapProperty() {
 return new HashMap<>();
 }
 }

 @Test
 public void testAnyGetterWriterInstance() {
 BeanProperty property = new BeanProperty(TestBean.class, "getMapProperty");
 AnnotatedMember accessor = new AnnotatedMember(TestBean.class, "getMapProperty");
 MapSerializer mapSerializer = new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 assertNotNull(anyGetterWriter);
 }

 @Test
 public void testGetAndSerializeNullValue() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 assertEquals(0, gen.getOutputSize());
 }

 @Test
 public void testGetAndSerializeEmptyMap() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);

 Map<String, Object> expectedMap = new HashMap<>();
 Map<String, Object> actualMap = mapper.readValue(gen.asString(), Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetAndSerializeMapWithValues() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 bean.getMapProperty().put("key1", "value1");
 bean.getMapProperty().put("key2", "value2");
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);

 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");

 Map<String, Object> actualMap = mapper.readValue(gen.asString(), Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapType() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 bean.getMapProperty().put("key1", "value1");
 bean.getMapProperty().put("key2", new Object());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 }

 @Test
 public void testGetAndFilterNullValue() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null), new PropertyFilterMapSerializer());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndFilter(bean, gen, provider, null);
 assertEquals(0, gen.getOutputSize());
 }

 @Test
 public void testGetAndFilterEmptyMap() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null), new PropertyFilterMapSerializer());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = new JsonFilter() {
 @Override
 public boolean include(SerializerProvider p, BeanProperty property, Object value) {
 return false;
 }
 };
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 Map<String, Object> expectedMap = new HashMap<>();
 Map<String, Object> actualMap = mapper.readValue(gen.asString(), Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetAndFilterMapWithValuesAndFilter() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 bean.getMapProperty().put("key1", "value1");
 bean.getMapProperty().put("key2", "value2");
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null), new PropertyFilterMapSerializer());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = new JsonFilter() {
 @Override
 public boolean include(SerializerProvider p, BeanProperty property, Object value) {
 return "value1".equals(property.getName());
 }
 };
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");

 Map<String, Object> actualMap = mapper.readValue(gen.asString(), Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapType() throws JsonMappingException, JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 bean.getMapProperty().put("key1", "value1");
 bean.getMapProperty().put("key2", new Object());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "getMapProperty"),
 new AnnotatedMember(TestBean.class, "getMapProperty"), new MapSerializer(new PropertyFilterMapSerializer(), null, null, null, null, null, null, null, null), new PropertyFilterMapSerializer());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = new PropertyFilter() {
 @Override
 public boolean include(SerializerProvider p, BeanProperty property, Object value) {
 return true;
 }
 };
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }

}