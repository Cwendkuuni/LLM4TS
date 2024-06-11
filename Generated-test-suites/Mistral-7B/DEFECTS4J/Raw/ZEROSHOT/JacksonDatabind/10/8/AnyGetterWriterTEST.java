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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 BeanProperty property = new BeanProperty("anyMap", Map.class, null);
 AnnotatedMember accessor = new AnnotatedMember("anyGetter", Map.class, null);
 MapSerializer mapSerializer = new MapSerializer(objectMapper.getFactory(), Map.class, true, 0);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws JsonProcessingException, IOException {
 Object bean = new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);

 String expectedJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
 String actualJson = gen.getOutputContentAsString();
 assertEquals(expectedJson, actualJson);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonProcessingException, IOException {
 Object bean = new HashMap<String, Object>();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws JsonProcessingException, IOException {
 Object bean = new Object(){};
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test
 public void testGetAndFilter() throws JsonProcessingException, IOException {
 Object bean = new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }};
 PropertyFilter filter = new FilterProvider().withFilter("anyMap", new JsonFilter() {
 @Override
 public void serializeAsString(SerializerProvider p, JsonGenerator g, String name) throws IOException, JsonProcessingException {
 if (name.equals("key3")) {
 g.writeString("filteredValue3");
 } else {
 super.serializeAsString(p, g, name);
 }
 }
 });
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 String expectedJson = "{\"key1\":\"value1\",\"key1\":\"filteredValue3\"}";
 String actualJson = gen.getOutputContentAsString();
 assertEquals(expectedJson, actualJson);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws JsonProcessingException, IOException {
 Object bean = new HashMap<String, Object>();
 PropertyFilter filter = new FilterProvider().emptyFilterProvider();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws JsonProcessingException, IOException {
 Object bean = new Object(){};
 PropertyFilter filter = new FilterProvider().emptyFilterProvider();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 fail("Expected JsonMappingException to be thrown");
 }
}