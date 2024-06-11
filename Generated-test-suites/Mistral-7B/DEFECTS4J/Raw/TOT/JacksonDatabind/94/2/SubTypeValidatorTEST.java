package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private SubTypeValidator subTypeValidator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 subTypeValidator = SubTypeValidator.instance();
 }

 @Test
 public void testValidType() throws Exception {
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), javaType);
 }

 @Test
 public void testInvalidType() throws Exception {
 JavaType javaType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), javaType));
 }

 @Test
 public void testSpringClass() throws Exception {
 JavaType javaType = objectMapper.getTypeFactory().constructType(Object[].class);
 subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), javaType.getComponentType());

 Class<?> clazz = Object.class.getDeclaredClass();
 clazz.getDeclaredMethod("setClassLoader", ClassLoader.class).setAccessible(true);
 clazz.getDeclaredMethod("getSuperclass").setAccessible(true);
 Class<?> springClass = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");

 JavaType springJavaType = objectMapper.getTypeFactory().constructType(new TypeReference<Class<?>>() {});
 springJavaType = objectMapper.getTypeFactory().constructType(springJavaType.getType().getComponentType());

 clazz.getDeclaredMethod("setBeanClass", Class.class).setAccessible(true);
 clazz.getDeclaredMethod("setPropertyContextClassLoader", ClassLoader.class).setAccessible(true);
 clazz.getDeclaredMethod("getObject").setAccessible(true);

 Object instance = clazz.newInstance();
 clazz.getDeclaredMethod("setBeanClass", Class.class).invoke(instance, springClass);
 clazz.getDeclaredMethod("setPropertyContextClassLoader", ClassLoader.class).invoke(instance, objectMapper.getDeserializationContext().getClassLoader());
 Object result = clazz.getDeclaredMethod("getObject").invoke(instance);

 JavaType resultType = (JavaType) result.getClass().getMethod("getType").invoke(result);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), resultType));
 }

 @Test
 public void testSpringInterface() throws Exception {
 JavaType javaType = objectMapper.getTypeFactory().constructType(Comparable.class);
 subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), javaType);

 Class<?> clazz = Comparable.class.getDeclaredClass();
 clazz.getDeclaredMethod("getInterfaces").setAccessible(true);
 Class<?> springInterface = (Class<?>) clazz.getDeclaredMethod("getInterfaces").invoke(clazz);

 JavaType springJavaType = objectMapper.getTypeFactory().constructType(new TypeReference<Class<?>>() {});
 springJavaType = objectMapper.getTypeFactory().constructType(springJavaType.getType().getComponentType());

 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), springJavaType));
 }
}