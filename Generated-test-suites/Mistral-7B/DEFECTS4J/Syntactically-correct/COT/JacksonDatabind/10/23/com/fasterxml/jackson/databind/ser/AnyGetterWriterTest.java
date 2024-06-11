package com.yourpackage; // replace with your test package

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class AnyGetterWriterTest extends TestBase {

 private static class TestBean {
 @JsonAnyGetter
 public Map<String, Object> anyGetter() {
 return new HashMap<>();
 }
 }

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 MapSerializer mapSerializer = new MapSerializer(new TypeFactory().constructType(Map.class), JsonMapping.StandardSerializerProvider.instance());
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer);
 TestBase.bind(TestBean.class).registerSerializer(AnyGetterWriter.class);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = new Object();
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer);

 anyGetterWriter.getAndSerialize(bean, gen, provider);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("anyGetter");
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 anyGetterWriter._mapSerializer.serializeFields(map, gen, provider);
 verify(gen).writeEndObject();
 }

 @Test
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = null;
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();

 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).getAndSerialize(bean, gen, provider);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = new int[1];
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();

 try {
 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).getAndSerialize(bean, gen, provider);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Value returned by 'any-getter' (anyGetter()) not java.util.Map but [I", e.getMessage());
 }
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = new Object();
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();
 PropertyFilter filter = createMock(PropertyFilter.class);

 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).getAndFilter(bean, gen, provider, filter);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterWithNullValue() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = null;
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();
 PropertyFilter filter = createMock(PropertyFilter.class);

 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).getAndFilter(bean, gen, provider, filter);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 Object anyInstance = new int[1];
 JsonGenerator gen = createMock(JsonGenerator.class);
 SerializerProvider provider = new TestSerializerProvider();
 PropertyFilter filter = createMock(PropertyFilter.class);

 try {
 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).getAndFilter(bean, gen, provider, filter);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Value returned by 'any-getter' (anyGetter()) not java.util.Map but [I", e.getMessage());
 }
 }

 @Test
 public void testResolveWithNullSerializerProvider() throws Exception {
 Object bean = new TestBean();
 SerializerProvider provider = null;

 try {
 new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer).resolve(provider);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Cannot initialize MapSerializer: null", e.getMessage());
 }
}

 @Test
 public void testResolve() throws Exception {
 Object bean = new TestBean();
 SerializerProvider provider = new TestSerializerProvider();

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(TestBean.class.getDeclaredField("anyGetter"), Object.class.getDeclaredField("anyInstance"), mapSerializer);
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }

}