package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StandardSerializer;

public class AnyGetterWriterTest {

 private Object testBean;
 private Object testMap;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 testBean = new TestBean();
 testMap = new HashMap<>();
 testMap.put("key1", "value1");
 testMap.put("key2", "value2");
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_MAP_SERIALIZER));
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 anyGetterWriter.getAndSerialize(testBean, gen, provider);
 String json = gen.getOutputContent();
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json);
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_Map_SERIALIZER));
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 anyGetterWriter.getAndSerialize(testBean, gen, provider); // should not reach here
 Object value = AnnotatedMember.findGetter(TestBean.class, "getAnyMap").getValue(testBean);
 assertNull(value);
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_Map_SERIALIZER));
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 Object nonMapValue = new Object();
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap").setValue(testBean, nonMapValue);
 anyGetterWriter.getAndSerialize(testBean, gen, provider);
 String json = gen.getOutputContent();
 fail("Should have thrown JsonMappingException: Value returned by 'any-getter' (anyMap()) not java.util.Map but "+nonMapValue.getClass().getName());
 gen.close();
 }

 @Test
 public void testGetAndFilter() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_Map_SERIALIZER), PropertyFilter.FILTER_NONE);
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 anyGetterWriter.getAndFilter(testBean, gen, provider, PropertyFilter.FILTER_NONE);
 String json = gen.getOutputContent();
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json);
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_Map_SERIALIZER), PropertyFilter.FILTER_NONE);
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 anyGetterWriter.getAndFilter(testBean, gen, provider, PropertyFilter.FILTER_NONE); // should not reach here
 Object value = AnnotatedMember.findGetter(TestBean.class, "getAnyMap").getValue(testBean);
 assertNull(value);
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), new MapSerializer(ObjectMapper.getDefaultInstance(), StandardSerializer.STANDARD_Map_SERIALIZER), PropertyFilter.FILTER_NONE);
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializationConfig());
 SerializerProvider provider = new StandardSerializerProvider();
 Object nonMapValue = new Object();
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap").setValue(testBean, nonMapValue);
 anyGetterWriter.getAndFilter(testBean, gen, provider, PropertyFilter.FILTER_NONE);
 fail("Should have thrown JsonMappingException: Value returned by 'any-getter' (anyMap()) not java.util.Map but "+nonMapValue.getClass().getName());
 gen.close();
 }

 @Test
 public void testResolve() throws JsonMappingException {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(TestBean.class, "anyMap"),
 AnnotatedMember.findGetter(TestBean.class, "getAnyMap"), null);
 SerializerProvider provider = new StandardSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }
}

class TestBean {
 private Map<String,String> anyMap;

 public Map<String,String> getAnyMap() {
 return anyMap;
 }

 public void setAnyMap(Map<String,String> anyMap) {
 this.anyMap = anyMap;
 }
}