package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class AnyGetterWriterTest {

 private AnyGetterWriter _anyGetterWriter;
 private ObjectNode _testMap;
 private JsonSerializerProvider _serializerProvider;
 private JsonGenerator _jsonGenerator;
 private AnnotatedMember _testAccessor;
 private BeanProperty _testProperty;
 private MapSerializer _mapSerializer;

 @Before
 public void setup() {
 _testProperty = new BeanProperty(new StandardSerializer(Object.class), null, null);
 _testAccessor = new AnnotatedMember(new StandardName("testAccessor"), null, null);
 _mapSerializer = new MapSerializer(new StandardSerializer(Map.class), null, null);
 _serializerProvider = new StandardSerializerProvider();
 _jsonGenerator = _serializerProvider.getGenerator();
 _anyGetterWriter = new AnyGetterWriter(_testProperty, _testAccessor, _mapSerializer);
 _testMap = ObjectMapper.getInstance().createObjectNode();
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object testObject = new TestClass();
 _testMap.put("testKey", "testValue");
 when(testObject.getTestMap()).thenReturn(_testMap);
 _anyGetterWriter.getAndSerialize(testObject, _jsonGenerator, _serializerProvider);
 String output = _jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\"testKey\":[\"testValue\"]}", output);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object testObject = new TestClass();
 _anyGetterWriter.getAndSerialize(testObject, _jsonGenerator, _serializerProvider);
 verifyNoMoreInteractions(_testMap);
 fail("Expected JsonMappingException when value is null");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object testObject = new TestClass();
 when(testObject.getTestMap()).thenReturn(123);
 _anyGetterWriter.getAndSerialize(testObject, _jsonGenerator, _serializerProvider);
 verifyNoMoreInteractions(_testMap);
 fail("Expected JsonMappingException when value is not a map");
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object testObject = new TestClass();
 _testMap.put("testKey1", 1);
 _testMap.put("testKey2", 2);
 _testMap.put("testKey3", null);
 when(testObject.getTestMap()).thenReturn(_testMap);
 PropertyFilter filter = new Filter();
 _anyGetterWriter.getAndFilter(testObject, _jsonGenerator, _serializerProvider, filter);
 String output = _jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\"testKey1\":1,\"testKey2\":2}", output);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 Object testObject = new TestClass();
 _testMap.put("testKey1", 1);
 _testMap.put("testKey2", 2);
 _testMap.put("testKey3", null);
 when(testObject.getTestMap()).thenReturn(_testMap);
 PropertyFilter filter = new Filter();
 _anyGetterWriter.getAndFilter(testObject, _jsonGenerator, _serializerProvider, filter);
 String output = _jsonGenerator.getOutputBuffer().toString();
 verifyNoMoreInteractions(_testMap);
 fail("Expected JsonMappingException when value is null");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object testObject = new TestClass();
 _testMap.put("testKey1", 1);
 _testMap.put("testKey2", 2);
 when(testObject.getTestMap()).thenReturn(123);
 PropertyFilter filter = new Filter();
 _anyGetterWriter.getAndFilter(testObject, _jsonGenerator, _serializerProvider, filter);
 verifyNoMoreInteractions(_testMap);
 fail("Expected JsonMappingException when value is not a map");
 }

 @Test
 public void testResolve() {
 _anyGetterWriter.resolve(_serializerProvider);
 assertNotNull(_anyGetterWriter.getMapSerializer());
 }

}

 class TestClass {
 private Map<String, Object> testMap;

 public Map<String, Object> getTestMap() {
 return testMap;
 }

 public void setTestMap(Map<String, Object> testMap) {
 this.testMap = testMap;
 }
 }

 class Filter {}