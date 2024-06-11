package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.BeanPropertyMicrosorter;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class SettableAnyPropertyTest {

 private ObjectMapper mapper;
 private DeserializationConfig config;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 config = mapper.getConfig();
 JavaType javaType = mapper.getTypeFactory().constructType(TestPojo.class);
 BeanProperty property = BeanPropertyIntrospector.findProperty("anyProperty", javaType);
 AnnotatedMember setter = BeanProperty.findSetter(property);
 settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"name\":\"John\", \"age\":30, \"anyProperty\":{\"key1\":\"value1\"}}");
 Object pojo = mapper.readValue(parser, TestPojo.class);
 assertEquals("John", pojo.getClass().getDeclaredField("name").get(pojo));
 assertEquals(30, pojo.getClass().getDeclaredField("age").get(pojo));
 Map<Object,Object> map = (Map<Object,Object>) settableAnyProperty.getProperty().getBeanProperty().get(pojo);
 assertEquals("value1", map.get("key1"));

 settableAnyProperty.deserializeAndSet(parser, config, pojo, "anyProperty");
 assertEquals("value1", map.get("key2"));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullKey() throws Exception {
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"anyProperty\":null}");
 settableAnyProperty.deserializeAndSet(parser, config, null, "anyProperty");
 fail("Should throw JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithInvalidType() throws Exception {
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"anyProperty\":true}");
 settableAnyProperty.deserializeAndSet(parser, config, null, "anyProperty");
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"anyProperty\":{\"key1\":{\"value\":\"foo\"}}}");
 Object pojo = mapper.readValue(parser, TestPojo.class);
 assertEquals(null, pojo.getClass().getDeclaredField("anyProperty").get(pojo));
 settableAnyProperty = settableAnyProperty.withValueDeserializer(new FromStringDeserializer(String.class));
 pojo = mapper.readValue(parser, TestPojo.class);
 assertEquals("foo", pojo.getClass().getDeclaredField("anyProperty").get(pojo));
 }

 @Test
 public void testValueDeserializerWithTypeDeserializer() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(TestPojoWithType.class);
 JavaType type = mapper.getTypeFactory().constructType(TestEnum.class);
 settableAnyProperty = new SettableAnyProperty(settableAnyProperty.getProperty(), settableAnyProperty.getSetter(), javaType, null, null, mapper.getTypeDeserializer(type));
 Object pojo = mapper.readValue(parser, TestPojoWithType.class);
 assertEquals(TestEnum.VALUE_ONE, pojo.getClass().getDeclaredField("anyProperty").get(pojo));
 }

 @Test
 public void testKeyDeserializer() throws Exception {
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"anyProperty\":{\"key\":\"key1\"}}");
 Object pojo = mapper.readValue(parser, TestPojo.class);
 assertEquals("key1", pojo.getClass().getDeclaredField("anyPropertyKey").get(pojo));
 settableAnyProperty = new SettableAnyProperty(settableAnyProperty.getProperty(), settableAnyProperty.getSetter(), null, new StdConverter.KeyAsString(), null, null);
 pojo = mapper.readValue(parser, TestPojo.class);
 assertEquals("key", pojo.getClass().getDeclaredField("anyPropertyKey").get(pojo));
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerWithInvalidDeserializer() throws Exception {
 settableAnyProperty = new SettableAnyProperty(settableAnyProperty.getProperty(), settableAnyProperty.getSetter(), null, new Converter.NullConverter(), null, null);
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser parser = jsonFactory.createParser("{\"anyProperty\":{\"key\":\"key1\"}}");
 settableAnyProperty.deserializeAndSet(parser, config, null, "anyProperty");
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testSet() throws Exception {
 Object pojo = new TestPojo();
 Map<Object,Object> map = (Map<Object,Object>) settableAnyProperty.getProperty().getBeanProperty().get(pojo);
 settableAnyProperty.set(pojo, "key1", "value1");
 assertEquals("value1", map.get("key1"));
 }

 @Test
 public void testSetWithNullValue() throws Exception {
 Object pojo = new TestPojo();
 Map<Object,Object> map = (Map<Object,Object>) settableAnyProperty.getProperty().getBeanProperty().get(pojo);
 settableAnyProperty.set(pojo, "key1", null);
 assertNull(map.get("key1"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSetWithInvalidValueType() throws Exception {
 Object pojo = new TestPojo();
 Map<Object,Object> map = (Map<Object,Object>) settableAnyProperty.getProperty().getBeanProperty().get(pojo);
 settableAnyProperty.set(pojo, "key1", new Object());
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testToString() {
 assertEquals("[any property on class TestPojo]", settableAnyProperty.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithUnsupportedSetter() throws Exception {
 Object pojo = new TestPojoWithUnsupportedSetter();
 settableAnyProperty.set(pojo, "key1", "value1");
 fail("Should throw UnsupportedOperationException");
 }
}

 class TestPojo {
 @JsonProperty
 private String name;
 private int age;
 private Map<Object,Object> anyProperty;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 @JsonProperty(value = "anyProperty")
 @JsonDeserialize(contentAs = Map.class)
 public Map<Object,Object> getAnyProperty() {
 return anyProperty;
 }

 public void setAnyProperty(Map<Object,Object> anyProperty) {
 this.anyProperty = anyProperty;
 }

 public Object getAnyPropertyKey() {
 return anyPropertyKey;
 }

 public void setAnyPropertyKey(Object anyPropertyKey) {
 this.anyPropertyKey = anyPropertyKey;
 }
}

 class TestPojoWithType {
 @JsonProperty
 private String name;
 private int age;
 private TestEnum anyProperty;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 @JsonProperty(value = "anyProperty")
 @JsonDeserialize(using = TestEnumDeserializer.class, contentAs = TestEnum.class)
 public TestEnum getAnyProperty() {
 return anyProperty;
 }

 public void setAnyProperty(TestEnum anyProperty) {
 this.anyProperty = anyProperty;
 }
}

 enum TestEnum {
 VALUE_ONE, VALUE_TWO
}

 class TestPojoWithUnsupportedSetter {
 @JsonProperty
 private String name;
 private int age;
 private Map<Object,Object> anyProperty;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 @JsonProperty(value = "anyProperty")
 @JsonDeserialize(contentAs = Map.class)
 public Map<Object,Object> getAnyProperty() {
 return anyProperty;
 }

 public void setAnyProperty(Map<Object,Object> anyProperty) {
 // Unsupported
 throw new UnsupportedOperationException();
 }
}

 class TestEnumDeserializer extends JsonDeserializer<TestEnum> {
 @Override
 public TestEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return TestEnum.VALUE_ONE;
 }
}