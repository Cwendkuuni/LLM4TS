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
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

 private Object testObject;
 private BeanPropertyDefinition propertyDefinition;
 private AnyGetterWriter anyGetterWriter;
 private Object mapValue;
 private MapSerializer mapSerializer;
 private JsonGenerator jsonGenerator;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 testObject = new TestClass();
 mapValue = new HashMap<>();
 mapValue.put("key1", "value1");
 mapValue.put("key2", "value2");
 propertyDefinition = BeanProperty.Standardiker.construct(TestClass.class, "anyMap");
 anyGetterWriter = new AnyGetterWriter(propertyDefinition, propertyDefinition.getMember(), mapSerializer = new MapSerializer(ObjectMapper.getDefaultInstance().getFactory()));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 JsonGenerator gen = createJsonGenerator();
 anyGetterWriter.getAndSerialize(testObject, gen, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider prov) throws JsonMappingException {
 return mapSerializer;
 }
 });
 gen.flush();
 String json = jsonGenerator.getOutputFactory().writeValueAsString(gen);
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = new Filter();
 JsonGenerator gen = createJsonGenerator();
 anyGetterWriter.getAndFilter(testObject, gen, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider prov) throws JsonMappingException {
 return mapSerializer;
 }
 }, filter);
 gen.flush();
 String json = jsonGenerator.getOutputFactory().writeValueAsString(gen);
 assertEquals("{\"key1\":\"value1\"}", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 JsonGenerator gen = createJsonGenerator();
 anyGetterWriter.getAndSerialize(testObject, gen, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider prov) throws JsonMappingException {
 return mapSerializer;
 }
 });
 assertNull(mapValue); // Verify that getValue() did not call on null object
 gen.flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 TestClass testObjectWithNonMapValue = new TestClassWithNonMapValue();
 JsonGenerator gen = createJsonGenerator();
 anyGetterWriter.getAndSerialize(testObjectWithNonMapValue, gen, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider prov) throws JsonMappingException {
 return mapSerializer;
 }
 });
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 TestClass testObjectWithNonMapValue = new TestClassWithNonMapValue();
 PropertyFilter filter = new Filter();
 JsonGenerator gen = createJsonGenerator();
 anyGetterWriter.getAndFilter(testObjectWithNonMapValue, gen, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider prov) throws JsonMappingException {
 return mapSerializer;
 }
 }, filter);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 anyGetterWriter.resolve(objectMapper.getSerializerProvider());
 assertNotNull(mapSerializer);
 }

 private JsonGenerator createJsonGenerator() throws IOException {
 ObjectWriter writer = ObjectMapper.getDefaultInstance().writerWithDefaultPrettyPrinter();
 StringWriter stringWriter = new StringWriter();
 jsonGenerator = writer.writeValueAsJsonWriter(stringWriter);
 return jsonGenerator;
 }

 private static class TestClass {
 @JsonAnyGetter
 public Map<String, String> getAnyMap() {
 return (Map<String, String>) this.mapValue;
 }
 }

 private static class TestClassWithNonMapValue {
 @JsonAnyGetter
 public Object getAnyMap() {
 return new Object();
 }
 }

 private static class Filter extends JsonFilter {
 @Override
 public void serializeFields(Map<String, Object> map, JsonGenerator gen, SerializerProvider serializers)
 throws IOException, JsonProcessingException {
 for (Map.Entry<String, Object> entry : map.entrySet()) {
 String key = entry.getKey();
 Object value = entry.getValue();
 if (key.equals("key1")) {
 gen.writeString(key);
 gen.writeString(":");
 gen.writeString(value.toString());
 gen.writeComma();
 }
 }
 gen.writeEndObject();
 }
 }
}