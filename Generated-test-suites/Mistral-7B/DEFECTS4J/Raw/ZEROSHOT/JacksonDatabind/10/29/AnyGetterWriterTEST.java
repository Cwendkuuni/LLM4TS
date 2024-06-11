package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link AnyGetterWriter}. This class tests all the methods in the AnyGetterWriter class.
 */
public class AnyGetterWriterTest {

 private static class TestClass {
 @JsonAnyGetter
 Map<String, String> getAnyMap() {
 return anyMap;
 }

 private Map<String, String> anyMap = new HashMap<>();
 }

 @Test
 public void testAnyGetterWriter() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
 BeanSerializerBase serializer = mapper.findValueSerializer(TestClass.class, null);
 BeanPropertyDefinitions propertyDefinitions = NestedIntrospector.findPropertyDefinitions(TestClass.class, null);
 BeanPropertyDefinition anyMapPropertyDefinition = propertyDefinitions.findPropertyDefinition("anyMap");
 AnnotatedClass testClassAnnotated = mapper.getSerializationConfig().getIntrospector().findClassAnnotations(TestClass.class);
 AnyGetterWriter testAnyGetterWriter = new AnyGetterWriter(anyMapPropertyDefinition.getBeanProperty(),
 anyMapPropertyDefinition.getMember(), new MapSerializer(null, null, mapper.getSerializationConfig()));
 BeanPropertyWriter mapWriter = mapper.findBeanPropertyWriter(Map.class, null);
 // When
 TestClass testClass = new TestClass();
 testAnyGetterWriter.resolve(serializer.getProvider());
 String json = mapper.writeValueAsString(testClass);
 // Then
 JsonParser parser = mapper.getFactory().createParser(json);
 Map<String, String> map = mapper.readValue(parser, Map.class);
 assertEquals("empty map", map.size(), 0);
 // When setting values and serializing
 testClass.getAnyMap().put("key1", "value1");
 testClass.getAnyMap().put("key2", "value2");
 anyGetterWriter.getAndSerialize(testClass, null, serializer.getProvider());
 json = mapper.writeValueAsString(testClass);
 parser = mapper.getFactory().createParser(json);
 map = mapper.readValue(parser, Map.class);
 assertEquals("map with 2 entries", map.size(), 2);
 assertEquals("value1", map.get("key1"));
 assertEquals("value2", map.get("key2"));
 // When filtering
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = provider.getFilterProvider().findFilterInstance(TestClass.class.getName());
 anyGetterWriter.getAndFilter(testClass, null, provider, filter);
 json = mapper.writeValueAsString(testClass);
 parser = mapper.getFactory().createParser(json);
 map = mapper.readValue(parser, Map.class);
 // check if all entries are present after filtering
 assertEquals("map with 2 entries after filtering", map.size(), 2);
 String filterName = filter.getName();
 assertEquals("CustomFilter", filterName);
 // Test filtering is working as expected
 map.put("key3", "value3");
 testClass = mapper.readValue(mapper.writeValueAsString(map), TestClass.class);
 assertEquals("value2", testClass.getAnyMap().get("key2"));
 assertNull("value3 should not be present after filtering", testClass.getAnyMap().get("key3"));
 }
}