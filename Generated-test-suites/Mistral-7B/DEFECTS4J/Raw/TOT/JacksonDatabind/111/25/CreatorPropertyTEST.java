package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest {

 private static class TestBean {
 @JsonDeserialize(using = CreatorProperty.class)
 private CreatorProperty creatorProperty;

 public TestBean() {
 // Default constructor
 }

 public TestBean(String value) {
 this();
 creatorProperty = new CreatorProperty(PropertyName.construct("creatorProperty"),
 String.class, null, null, null, null, 0, "valueId", null);
 creatorProperty.setFallbackSetter(new CreatorProperty(creatorProperty, PropertyName.construct("setCreatorProperty")));
 }

 public void setCreatorProperty(String value) {
 creatorProperty.setValue(value);
 }

 public String getCreatorProperty() {
 return creatorProperty.getValue();
 }
 }

 @SuppressWarnings("unchecked")
 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testCreatorPropertyDeserialization() throws IOException {
 ObjectReader reader = MAPPER.readerForClass(TestBean.class);
 String json = "{\"creatorProperty\":\"test\"}";
 TestBean bean = reader.readValue(json);
 assertEquals("test", bean.getCreatorProperty());
 }

 @Test(expected = IOException.class)
 public void testCreatorPropertyMissingSetter() throws IOException {
 ObjectReader reader = MAPPER.readerForClass(TestBean.class);
 String json = "{\"creatorProperty\":null}";
 TestBean bean = reader.readValue(json);
 fail("Expected IOException to be thrown due to missing setter.");
 }

 @Test
 public void testCreatorPropertyInjection() throws IOException {
 ObjectReader reader = MAPPER.readerForClass(TestBean.class);
 String json = "{\"creatorProperty\":{\"@valueId\":\"valueId\"}}";
 TestBean bean = reader.readValue(json);
 assertTrue(bean.getClass().getDeclaredField("creatorProperty")
 .get(bean) instanceof CreatorProperty);
 Object injectedValue = new Object();
 ObjectWriter writer = MAPPER.writerForValueType(new TypeReference<Object>() {});
 String jsonToWrite = writer.writeValueAsString(injectedValue);
 json = "{\"creatorProperty\":" + jsonToWrite + "}";
 TestBean updatedBean = reader.readValue(json);
 assertEquals(injectedValue, updatedBean.getCreatorProperty());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreatorPropertyInvalidInjectValueId() throws IOException {
 ObjectReader reader = MAPPER.readerForClass(TestBean.class);
 String json = "{\"creatorProperty\":{\"@valueId\":null}}";
 TestBean bean = reader.readValue(json);
 fail("Expected JsonMappingException to be thrown due to null inject value id.");
 }

 @Test
 public void testCreatorPropertySerializaton() throws IOException {
 TestBean bean = new TestBean("test");
 ObjectWriter writer = MAPPER.writerForClass(TestBean.class);
 String json = writer.writeValueAsString(bean);
 TestBean deserializedBean = MAPPER.readValue(json, TestBean.class);
 assertEquals("test", deserializedBean.getCreatorProperty());
 }

 @Test
 public void testCreatorPropertyWithNullValue() throws IOException {
 TestBean bean = new TestBean(null);
 ObjectWriter writer = MAPPER.writerForClass(TestBean.class);
 String json = writer.writeValueAsString(bean);
 TestBean deserializedBean = MAPPER.readValue(json, TestBean.class);
 assertNull(deserializedBean.getCreatorProperty());
 }

 @Test
 public void testCreatorPropertyWithCustomConverter() throws IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final StdConverter<Integer, String> intToStrConverter = new StdConverter<Integer, String>() {
 @Override public String convert(Integer value, StdSerializer<String> serializer)
 throws IOException {
 serializer.serializeString(String.valueOf(value));
 return null;
 }
 };
 mapper.setConverter(new TypeDeserializer.Finder() {
 @Override public Converter<Object, Object> findValueConverter(DeserializationConfig config,
 TypeDeserializer typeDeserializer, TypeDescriptor typeDeserializer, TypeDescriptor valueType) {
 if (typeDeserializer.getType() == Integer.class) {
 return intToStrConverter;
 }
 return null;
 }
 });
 ObjectReader reader = mapper.readerForClass(TestBean.class);
 TestBean bean = reader.readValue("[1]");
 assertEquals("1", bean.getCreatorProperty());
 }
}