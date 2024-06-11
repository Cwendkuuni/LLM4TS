package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Test;

/**
 * Test class for AnyGetterWriter
 */
public class AnyGetterWriterTest {

 private static class TestBean {
 private Map<String, String> map;

 @JsonAnyGetter
 public Map<String, String> getMap() {
 return map;
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConstructor() throws Exception {
 BeanProperty property = new BeanProperty("map", Map.class, null);
 AnnotatedMember accessor = new AnnotatedMember(TestBean.class.getDeclaredField("map"), null);
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, new MapSerializer(new StandardSerializer(Map.class), null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndSerializeNullBean() throws Exception {
 Object bean = null;
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 assertTrue(gen.getOutputBuffer().toString().isEmpty());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndSerializeEmptyMap() throws Exception {
 TestBean bean = new TestBean();
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 String expected = "{}";
 String actual = gen.getOutputBuffer().toString();
 assertEquals(expected, actual);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndSerializeNonMapValue() throws Exception {
 TestBean bean = new TestBean();
 bean.map = new Integer(42);
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 throw new JsonMappingException("Unexpected value in JSON: 42");
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndSerializeNullMapValue() throws Exception {
 TestBean bean = new TestBean();
 bean.map = null;
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 assertTrue(gen.getOutputBuffer().toString().isEmpty());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndFilterNullBean() throws Exception {
 Object bean = null;
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndFilter(bean, gen, provider, new FilterProvider());
 assertTrue(gen.getOutputBuffer().toString().isEmpty());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndFilterEmptyMap() throws Exception {
 TestBean bean = new TestBean();Map<String, String> map = new HashMap<>();
 bean.map = map;
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 PropertyFilter filter = new FilterProvider().filter("map", new EmptyFilter());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 String expected = "{}";
 String actual = gen.getOutputBuffer().toString();
 assertEquals(expected, actual);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndFilterNonMapValue() throws Exception {
 TestBean bean = new TestBean();
 bean.map = new Integer(42);
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 PropertyFilter filter = new FilterProvider().filter("map", new EmptyFilter());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 throw new JsonMappingException("Unexpected value in JSON: 42");
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndFilterNullMapValueFiltered() throws Exception {
 TestBean bean = new TestBean();
 bean.map = null;
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 PropertyFilter filter = new FilterProvider().filter("map", new EmptyFilter());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 assertTrue(gen.getOutputBuffer().toString().isEmpty());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapValueNotFiltered() throws Exception {
 TestBean bean = new TestBean();
 bean.map = new Integer(42);
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 anyGetterWriter.getAndFilter(bean, gen, provider, null);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testResolveNullProvider() throws Exception {
 SerializerProvider provider = null;
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 assertNull(anyGetterWriter._mapSerializer);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testResolveNotNullProvider() throws Exception {
 SerializerProvider provider = new SerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty("map", Map.class, null), new AnnotatedMember(TestBean.class.getDeclaredField("map"), null), new MapSerializer(new StandardSerializer(Map.class), null));
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }
}