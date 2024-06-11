package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AnyGetterWriterTest extends TestBase {

 private AnyGetterWriter anyGetterWriter;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 AnyPropertyBean bean = new AnyPropertyBean();
 bean.setMap(new HashMap<String, String>());
 BeanProperty property = objectMapper.findProperty("map", AnyPropertyBean.class, bean);
 AnnotatedMember accessor = property.getMember();
 AnnotatedMethod method = property.getGetter();
 AnyGetterWriter.MapSerializer mapSerializer = new AnyGetterWriter.MapSerializer(true, false);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = objectMapper.getSerializerProvider();
 Object bean = new AnyPropertyBean();
 bean.setMap(new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 }});
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 String json = readTreeFromString(gen.getOutputStream().toString());
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullMap() throws Exception {
 Object bean = new AnyPropertyBean();
 anyGetterWriter.getAndSerialize(bean, null, objectMapper.getSerializerProvider());
 fail("Expected JsonMappingException but got null");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapType() throws Exception {
 Object bean = new AnyPropertyBean();
 bean.setMap(new Integer(42));
 anyGetterWriter.getAndSerialize(bean, null, objectMapper.getSerializerProvider());
 fail("Expected JsonMappingException but got null");
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = new FilterTest();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = objectMapper.getSerializerProvider();
 Object bean = new AnyPropertyBean();
 bean.setMap(new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }});
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 String json = readTreeFromString(gen.getOutputStream().toString());
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json.toString());
 }

 @Test
 public void testGetAndFilterNullMap() throws Exception {
 Object bean = new AnyPropertyBean();
 anyGetterWriter.getAndFilter(bean, null, objectMapper.getSerializerProvider(), new FilterTest());
 fail("Expected JsonMappingException but got null");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapType() throws Exception {
 Object bean = new AnyPropertyBean();
 bean.setMap(new Integer(42));
 anyGetterWriter.getAndFilter(bean, null, objectMapper.getSerializerProvider(), new FilterTest());
 fail("Expected JsonMappingException but got null");
 }

 @Test
 public void testResolve() throws Exception {
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }

 @Test
 public void testGetAnyGetterWriterWithNullSerializer() throws Exception {
 try {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
 fail("Expected IllegalArgumentException but got null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetAnyGetterWriterWithNullProperty() throws Exception {
 try {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
 fail("Expected IllegalArgumentException but got null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @JsonInclude(JsonInclude.Include.NON_NULL)
 public static class AnyPropertyBean {
 private Map<String, String> map;

 public Map<String, String> getMap() {
 return map;
 }

 @JsonAnyGetter
 public Map<String, String> getAllProperties() {
 return map;
 }
 }

 static class FilterTest implements PropertyFilter {
 @Override
 public void serializeAsField(Map.Entry<String, ?> entry, JsonGenerator gen, SerializerProvider provider)
 throws IOException, JsonProcessingException {
 fail("Unexpected call to serializeAsField()");
 }

 @Override
 public void serializeAsProperty(Map.Entry<String, ?> entry, JsonGenerator gen, SerializerProvider provider, String propertyName, int serializedSize, boolean isEmpty)
 throws IOException, JsonProcessingException {
 fail("Unexpected call to serializeAsProperty()");
 }
 }
}