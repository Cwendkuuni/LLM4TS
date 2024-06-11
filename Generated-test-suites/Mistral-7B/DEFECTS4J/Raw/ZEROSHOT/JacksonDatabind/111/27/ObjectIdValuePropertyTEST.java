package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanPropertyDeserializerProvider.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.EmptyBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private BeanDescription beanDescription;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 ObjectIdReader idReader = new ObjectIdReader("id", String.class, null);
 objectIdReader.idProperty = new SimpleBeanPropertyFilter("ignoreIdProperty", EmptyBeanPropertyFilter.FilteremptyFilter.instance());
 this.beanDescription = objectMapper.introspect(TestClass.class);
 this.objectIdReader = idReader;
 this.objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Integer.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 TestClass testClass = new TestClass();
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123\"");
 DeserializationContext context = objectMapper.getDeserializationContext();
 objectIdValueProperty.deserializeAndSet(jsonParser, context, testClass);
 assertEquals("123", testClass.getId());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 TestClass testClass = new TestClass();
 TestClass testClassResult = null;
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"123\"");
 DeserializationContext context = objectMapper.getDeserializationContext();
 testClassResult = (TestClass) objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, testClass);
 assertEquals("123", testClassResult.getId());
 }

 @Test
 public void testSet() throws IOException {
 TestClass testClass = new TestClass();
 testClass.setId("123");
 objectIdValueProperty.set(testClass, null);
 assertEquals("123", testClass.getId());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 TestClass testClass = new TestClass();
 TestClass testClassResult = null;
 testClass.setId("123");
 testClassResult = (TestClass) objectIdValueProperty.setAndReturn(testClass, null);
 assertEquals("123", testClassResult.getId());
 }

 @Test
 public void testWithName() throws Exception {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newObjectIdValueProperty.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(new StubJsonDeserializer());
 assertNotSame(objectIdValueProperty.getValueDeserializer(), newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(new TestNullValueProvider());
 assertNotSame(objectIdValueProperty.getNullProvider(), newObjectIdValueProperty.getNullProvider());
 }

 @Test
 public void testSerialization() throws IOException, JsonProcessingException {
 TestClass testClass = new TestClass();
 testClass.setId("123");
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 String json = objectWriter.writeValueAsString(testClass);
 TestClass testClassDeserialized = objectMapper.readValue(json, TestClass.class);
 assertEquals("123", testClassDeserialized.getId());
 }

 @Test
 public void testSerializationWithFilter() throws IOException, JsonProcessingException {
 TestClass testClass = new TestClass();
 testClass.setId("123");
 ObjectMapper objectMapper = objectMapper.copy();
 ObjectWriter objectWriter = objectMapper.writer(new SimpleBeanPropertyFilter("filter", new SimpleFilterProvider().findFilter("filter", TestClass.class)));
 String json = objectWriter.writeValueAsString(testClass);
 TestClass testClassDeserialized = objectMapper.readValue(json, TestClass.class);
 assertEquals("123", testClassDeserialized.getId());
 }

 @Test
 public void testWithName_whenNullName_thenThrowIllegalArgumentException() {
 try {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()){
 @Override
 public PropertyName getName() {
 return null;
 }
 };
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("name can't be null", e.getMessage());
 }
}

 @Test
 public void testWithValueDeserializer_whenNullValueDeserializer_thenThrowIllegalArgumentException() {
 try {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()){
 @Override
 public JsonDeserializer<?> getValueDeserializer() {
 return null;
 }
 };
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("value deserializer can't be null", e.getMessage());
 }
}

 @Test
 public void testWithNullProvider_whenNullNullProvider_thenThrowIllegalArgumentException() {
 try {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()){
 @Override
 public NullValueProvider getNullProvider() {
 return null;
 }
 };
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("null provider can't be null", e.getMessage());
 }
}
}

 class TestClass {
 private String id;

 public String getId() {
 return id;
 }

 public void setId(String id) {
 this.id = id;
 }
}

 class StubJsonDeserializer extends JsonDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "stub";
 }
}

 class TestNullValueProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return null;
 }
}

 class SimpleFilterProvider extends FilterProvider {
 @Override
 public Filter findFilter(String filterName, Class<?> filterClass) {
 return new SimpleBeanPropertyFilter("filter", EmptyBeanPropertyFilter.FilteremptyFilter.instance());
 }
}

 class SimpleFilter extends FilterBase {
 protected SimpleFilter(String filterName, FilterProvider filterProvider) {
 super(filterName, filterProvider);
 }

 @Override
 public Filter findFilter(String filterName, Class<?> filterClass) {
 return this;
 }
}