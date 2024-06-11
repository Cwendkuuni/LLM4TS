package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

 private AnyGetterWriter _anyGetterWriter;
 private BeanProperty _property;
 private ObjectMapper _mapper;
 private AnnotatedMember _accessor;
 private MapSerializer _mapSerializer;
 private SerializerProvider _provider;
 private JsonGenerator _generator;

 @Before
 public void setUp() throws Exception {
 _mapper = new ObjectMapper();
 _mapper.findAndRegisterModules();
 _mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 _property = new BeanProperty.Std("anyMapProperty", Map.class, null, new PropertyName("anyMapProperty"));
 _accessor = new AnnotatedMember(new PropertyName("anyMapProperty"), null, null);
 _mapSerializer = new StdSerializer<Map<String, Object>>(Map.class) {
 @Override
 public void serializeFields(Map<String, Object> value, JsonGenerator gen, SerializerProvider provider)
 throws IOException, JsonProcessingException {
 for (Map.Entry<String, Object> entry : value.entrySet()) {
 gen.writeStartObject();
 gen.writeStringField("key", entry.getKey());
 gen.writeObjectField("value", entry.getValue());
 gen.writeEndObject();
 }
 }
 };
 _provider = _mapper.getSerializerProvider();
 _generator = _mapper.getFactory().createGenerator(System.out);
 _anyGetterWriter = new AnyGetterWriter(_property, _accessor, _mapSerializer);
 }

 @Test
 public void testGetAndSerializeMethod() throws Exception {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 Object bean = new Object() {{
 setAnyMapProperty(map);
 }};
 _anyGetterWriter.getAndSerialize(bean, _generator, _provider);
 _generator.flush();
 String actualOutput = _generator.getOutputStr();
 String expectedOutput = "[\n" +
         "  {\"key\":\"key1\",\"value\":\"value1\"},\n" +
         "  {\"key\":\"key2\",\"value\":\"value2\"}\n" +
         "]";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullValue() throws Exception {
 Object bean = new Object() {{
 setAnyMapProperty(null);
 }};
 _anyGetterWriter.getAndSerialize(bean, _generator, _provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = new Object() {{
 setAnyMapProperty("nonMapValue");
 }};
 _anyGetterWriter.getAndSerialize(bean, _generator, _provider);
 }

 @Test
 public void testGetAndFilterMethod() throws Exception {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 Object bean = new Object() {{
 setAnyMapProperty(map);
 }};
 _anyGetterWriter.getAndFilter(bean, _generator, _provider, new FilterProvider().emptyFilterProvider());
 _generator.flush();
 String actualOutput = _generator.getOutputStr();
 String expectedOutput = "[\n" +
         "  {\"key\":\"key1\",\"value\":\"value1\"},\n" +
         "  {\"key\":\"key2\",\"value\":\"value2\"}\n" +
         "]";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetAndFilterEmptyMap() throws Exception {
 Object bean = new Object() {{
 setAnyMapProperty(new HashMap<>());
 }};
 _anyGetterWriter.getAndFilter(bean, _generator, _provider, new FilterProvider().emptyFilterProvider());
 _generator.flush();
 String actualOutput = _generator.getOutputStr();
 String expectedOutput = "[]";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullValue() throws Exception {
 Object bean = new Object() {{
 setAnyMapProperty(null);
 }};
 _anyGetterWriter.getAndFilter(bean, _generator, _provider, new FilterProvider().emptyFilterProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapValue() throws Exception {
 Object bean = new Object() {{
 setAnyMapProperty("nonMapValue");
 }};
 _anyGetterWriter.getAndFilter(bean, _generator, _provider, new FilterProvider().emptyFilterProvider());
 }

 @Test
 public void testResolveMethod() throws Exception {
 _anyGetterWriter.resolve(_provider);
 assertTrue(_anyGetterWriter._mapSerializer != null);
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveNullMapSerializer() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property, _accessor, null);
 anyGetterWriter.resolve(_provider);
 }
}