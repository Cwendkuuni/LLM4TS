package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private AnyGetterWriter _anyGetterWriter;
 private BeanProperty _property;
 private AnnotatedMember _accessor;
 private Object _bean;
 private MapSerializer _mapSerializer;

 @Before
 public void setUp() throws Exception {
 _property = mockProperty("testKey", String.class, Map.class);
 _accessor = mockAnnotatedMember(_property);
 _bean = new HashMap<>();
 _bean.put("testKey", "testValue");
 _mapSerializer = createMapSerializer(ObjectMapper.getDefaultInstance(), _property, _accessor);
 _anyGetterWriter = new AnyGetterWriter(_property, _accessor, _mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws IOException {
 Object value = _bean.get("testKey");
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(_bean, gen, provider);
 String result = gen.toString();
 assertEquals("{\"testKey\":[\"" + value + "\"]}", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws IOException {
 Object value = null;
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(_bean, gen, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws IOException {
 Object value = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(_bean, gen, provider);
 }

 @Test
 public void testGetAndFilter() throws IOException {
 Object value = _bean.get("testKey");
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = createPropertyFilter();
 _anyGetterWriter.getAndFilter(_bean, gen, provider, filter);
 String result = gen.toString();
 assertEquals("{\"testKey\":[\"" + value + "\"]}", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws IOException {
 Object value = null;
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = createPropertyFilter();
 _anyGetterWriter.getAndFilter(_bean, gen, provider, filter);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws IOException {
 Object value = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = createPropertyFilter();
 _anyGetterWriter.getAndFilter(_bean, gen, provider, filter);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.resolve(provider);
 assertNotNull(_anyGetterWriter.getMapSerializer());
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveWithNullSerializerProvider() {
 SerializerProvider provider = null;
 _anyGetterWriter.resolve(provider);
 }

}