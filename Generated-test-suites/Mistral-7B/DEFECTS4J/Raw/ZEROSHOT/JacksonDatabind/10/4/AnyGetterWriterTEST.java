package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 MapSerializer mapSerializer = mapper.getSerializerProvider().findValueSerializer(Map.class);
 AnnotatedMember accessor = mapper.introspect().findAnnotatedMember("anyMap", TestBean.class);
 BeanProperty property = mapper.introspect().findProperty("anyMap", TestBean.class);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 }});

 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndSerialize(bean, gen, getProvider());
 String actualJson = gen.getOutputContent().toString();
 String expectedJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
 assertEquals(expectedJson, actualJson);
 } finally {
 gen.close();
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullValue() throws Exception {
 Object bean = new TestBean();
 anyGetterWriter.getAndSerialize(bean, getProvider().getGenerator(), getProvider());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = new TestBean();
 bean.setAnyMap(42);
 anyGetterWriter.getAndSerialize(bean, getProvider().getGenerator(), getProvider());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullValue() throws Exception {
 Object bean = new TestBean();
 anyGetterWriter.getAndFilter(bean, getProvider().getGenerator(), getProvider(), null);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapValue() throws Exception {
 Object bean = new TestBean();
 bean.setAnyMap(42);
 anyGetterWriter.getAndFilter(bean, getProvider().getGenerator(), getProvider(), null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testGetAndFilterIncludeExclusion() throws Exception {
 Object bean = new TestBean();
 bean.setAnyMap(new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }});

 ObjectNode rootNode = mapper.createObjectNode();
 PropertyFilter filter = new FilterProvider().filter("testFilter", new SimpleFilterProvider() {{
 setFailOnEmpty(false);
 setInclude(value -> Include.NON_NULL);
 setFilterId("testFilter");
 }});
 JsonGenerator gen = createJsonGenerator();
 try {
 anyGetterWriter.getAndFilter(bean, gen, getProvider(), filter);
 String actualJson = gen.getOutputContent().toString();
 String expectedJson = "{\"key1\":\"value1\"}";
 assertEquals(expectedJson, actualJson);
 } finally {
 gen.close();
 }
}

 @Test
 public void testResolve() throws Exception {
 SerializerProvider provider = getProvider();
 assertNull(anyGetterWriter.getMapSerializer());
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter.getMapSerializer());
 }
}

 class TestBean {
 @JsonAnyGetter
 public Map<String, Object> getAnyMap() {
 return anyMap;
 }
 private Map<String, Object> anyMap;
 public void setAnyMap(Map<String, Object> anyMap) {
 this.anyMap = anyMap;
 }
 }