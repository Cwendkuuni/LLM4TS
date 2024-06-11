package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;
import com.fasterxml.jackson.databind.util.TestObjectMapper.MyBean;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private MyBean myBean;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 myBean = new MyBean();
 }

 @Test
 public void testGetAndSerialize() throws JsonProcessingException, JsonMappingException, IOException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 myBean.map = map;

 Object value = myBean.getClass().getMethod("getMap").invoke(myBean);
 AnyGetterWriter testee = new AnyGetterWriter(context.finder.findProperty("map", MyBean.class, null), context.finder.findAnnotatedMember(MyBean.class, "getMap"), _mapSerializer);

 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 testee.getAndSerialize(myBean, gen, provider);
 String actual = gen.getOutputFactory().createStringBuilder().toString();
 String expected = "[\"key1\",\"value1\"][\"key2\",\"value2\"]";
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeThrowsExceptionWhenValueIsNotInstanceOfMap() throws JsonProcessingException, JsonMappingException, IOException {
 myBean.map = new Object();
 AnyGetterWriter testee = new AnyGetterWriter(context.finder.findProperty("map", MyBean.class, null), context.finder.findAnnotatedMember(MyBean.class, "getMap"), _mapSerializer);

 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 testee.getAndSerialize(myBean, gen, provider);
 }

 @Test
 public void testGetAndFilter() throws JsonProcessingException, JsonMappingException, IOException {
 PropertyFilter filter = context.propertyFilterInstance("filter1", JsonInclude.Include.NON_NULL);
 MyBean myBeanWithFilteredMap = new MyBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", null);
 map.put("key2", "value2");
 myBeanWithFilteredMap.map = map;

 ObjectGenerator generator = mock(ObjectGenerator.class);
 when(generator.getFilteredObjectWriter(any(Class.class), any(JsonSerializer.class), any(PropertyFilter.class))).thenReturn(null);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getObjectWriter(any(Class.class))).thenReturn(generator);
 SerializerProvider provider = createSerializerProvider();
 provider.setRootValueSerializer(new StdSerializer<>(Map.class));

 AnyGetterWriter testee = new AnyGetterWriter(context.finder.findProperty("map", MyBean.class, null), context.finder.findAnnotatedMember(MyBean.class, "getMap"), _mapSerializer);
 testee.getAndFilter(myBeanWithFilteredMap, gen, provider, filter);
 String actual = gen.getOutputFactory().createStringBuilder().toString();
 String expected = "[\"key2\",\"value2\"]";
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterThrowsExceptionWhenValueIsNotInstanceOfMap() throws JsonProcessingException, JsonMappingException, IOException {
 MyBean myBeanWithFilteredMap = new MyBean();
 MyBean.MyInnerClass innerClass = new MyBean.MyInnerClass();
 myBeanWithFilteredMap.map = innerClass;
 PropertyFilter filter = context.propertyFilterInstance("filter1", JsonInclude.Include.NON_NULL);
 AnyGetterWriter testee = new AnyGetterWriter(context.finder.findProperty("map", MyBean.class, null), context.finder.findAnnotatedMember(MyBean.class, "getMap"), _mapSerializer);

 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 testee.getAndFilter(myBeanWithFilteredMap, gen, provider, filter);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 AnyGetterWriter testee = new AnyGetterWriter(null, null, null);
 testee.resolve(createSerializerProvider());
 assertNotNull(testee._mapSerializer);
 }

}