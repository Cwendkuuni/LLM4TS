package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.CreatorProperty.StandardCreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreatorPropertyTest {

 // Test case for CreatorProperty constructor
 @SuppressWarnings("unchecked")
 @Test
 public void testCreatorPropertyConstructor() throws Exception {
 Class<CreatorProperty> clazz = CreatorProperty.class;
 PropertyName name = PropertyName.construct("propertyName");
 JavaType type = new TypeReference<Integer>() {}.getType();
 PropertyName wrapperName = PropertyName.construct("wrapperName");
 TypeDeserializer typeDeser = null;
 Annotations contextAnnotations = null;
 AnnotatedParameter param = null;
 int index = 0;
 Object injectableValueId = null;
 CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
 assertEquals("CreatorProperty", creatorProperty.getClass().getSimpleName());
 }

 // Test case for CreatorProperty copy constructor
 @Test
 public void testCreatorPropertyCopyConstructor() throws Exception {
 CreatorProperty src = new CreatorProperty(new PropertyName("srcName"), new JavaType(int.class), null, null, null, null, 0, null, null);
 CreatorProperty dest = new CreatorProperty(src, new PropertyName("destName"));
 assertEquals(src.getName(), dest.getName());
 assertEquals(src.getType(), dest.getType());
 assertEquals(src.getWrapperName(), dest.getWrapperName());
 assertEquals(src.getTypeDeserializer(), dest.getTypeDeserializer());
 assertEquals(src.getContextAnnotations(), dest.getContextAnnotations());
 assertEquals(src.getAnnotated(), dest.getAnnotated());
 assertEquals(src.getCreatorIndex(), dest.getCreatorIndex());
 assertEquals(src._injectableValueId, dest._injectableValueId);
 assertEquals(src._fallbackSetter, dest._fallbackSetter);
 }

 // Test case for CreatorProperty withName method
 @Test
 public void testCreatorPropertyWithName() throws Exception {
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("originalName"), new JavaType(int.class), null, null, null, null, 0, null, null);
 CreatorProperty newCreatorProperty = creatorProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newCreatorProperty.getName().asString());
 }

 // Test case for CreatorProperty withValueDeserializer method
 @Test
 public void testCreatorPropertyWithValueDeserializer() throws Exception {
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("originalName"), new JavaType(int.class), null, null, null, null, 0, null, null);
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {};
 CreatorProperty newCreatorProperty = creatorProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer.getClass(), newCreatorProperty._valueDeserializer);
 }

 // Test case for CreatorProperty withNullProvider method
 @Test
 public void testCreatorPropertyWithNullProvider() throws Exception {
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("originalName"), new JavaType(int.class), null, null, null, null, 0, null, null);
 NullValueProvider nva = new NullValueProvider() {};
 CreatorProperty newCreatorProperty = creatorProperty.withNullProvider(nva);
 assertEquals(nva.getClass(), newCreatorProperty._nullProvider);
 }

 // Test case for CreatorProperty markAsIgnorable method
 @Test
 public void testCreatorPropertyMarkAsIgnorable() throws Exception {
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("originalName"), new JavaType(int.class), null, null, null, null, 0, null, null);
 creatorProperty.markAsIgnorable();
 assertTrue(creatorProperty.isIgnorable());
 }

 // Test case for CreatorProperty findInjectableValue method
 @Test
 public void testCreatorPropertyFindInjectableValue() throws Exception {
 DeserializationContext context = new TestDeserializationContext();
 ObjectMapper mapper = new ObjectMapper();
 Object beanInstance = mapper.readValue(new ObjectMapper().readTree("{\"bean\":{}}"), Bean.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("injectableProperty"), new JavaType(String.class), null, null, null, null, 0, null, null);
 Object value = creatorProperty.findInjectableValue(context, beanInstance);
 assertNotNull(value);
 assertEquals("injectValue", value);
 }

 // Test case for CreatorProperty inject method
 @Test
 public void testCreatorPropertyInject() throws Exception {
 DeserializationContext context = new TestDeserializationContext();
 ObjectMapper mapper = new ObjectMapper();
 Object beanInstance = mapper.readValue(new ObjectMapper().readTree("{\"bean\":{}}"), Bean.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("injectableProperty"), new JavaType(String.class), null, null, null, null, 0, null, null);
 creatorProperty.inject(context, beanInstance);
 assertEquals("injectValue", ((Bean)beanInstance).getInjectableProperty());
 }

 // Test case for CreatorProperty deserializeAndSet method
 @Test
 public void testCreatorPropertyDeserializeAndSet() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JsonParser p = mapper.getFactory().createParser("{\"value\":42}");
 Object beanInstance = mapper.readValue(p, Bean.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("settableProperty"), new JavaType(Integer.class), null, null, null, null, 0, null, null);
 creatorProperty.deserializeAndSet(p, config, beanInstance);
 assertEquals(42, ((Bean)beanInstance).getSettableProperty());
 }

 // Test case for CreatorProperty setAndReturn method
 @Test
 public void testCreatorPropertySetAndReturn() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 Object beanInstance = mapper.readValue(new ObjectMapper().readTree("{\"value\":42}"), Bean.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("settableProperty"), new JavaType(Integer.class), null, null, null, null, 0, null, null);
 Object newValue = Object.newInstance(int.class, 43);
 assertEquals(42, ((Bean)beanInstance).getSettableProperty());
 Object newValueReturn = creatorProperty.setAndReturn(beanInstance, newValue);
 assertEquals(43, ((Bean)beanInstance).getSettableProperty());
 assertEquals(newValue, newValueReturn);
 }

 // Test case for CreatorProperty with missing setter
 @Test(expected = JsonMappingException.class)
 public void testCreatorPropertyMissingSetter() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JsonParser p = mapper.getFactory().createParser("{\"value\":42}");
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("missingSetterProperty"), new JavaType(Integer.class), null, null, null, null, 0, null, null);
 creatorProperty.deserializeAndSet(p, config, null);
 }
}

class Bean {
 @JsonCreator
 public Bean(String injectValue, Integer settableProperty, String missingSetterProperty) {
 this.injectValue = injectValue;
 this.settableProperty = settableProperty;
 }

 @JsonValue
 public String getInjectValue() {
 return injectValue;
 }

 @JsonValue
 public Integer getSettableProperty() {
 return settableProperty;
 }

 private String injectValue;
 private Integer settableProperty;
}

class TestDeserializationContext extends MinimalDeserializationContext {
 @Override
 public Object findBeanForDeserialization(DeserializationContext ctxt, BeanProperty property, Object bean) throws IOException {
 return bean;
 }

 @Override
 public Object instanceWithValue(DeserializationConfig config, BeanDescription beanDesc, Class<?> beanClass, Object value, Annotation[] anns) throws InstantiationException, IllegalAccessException {
 return value;
 }
}