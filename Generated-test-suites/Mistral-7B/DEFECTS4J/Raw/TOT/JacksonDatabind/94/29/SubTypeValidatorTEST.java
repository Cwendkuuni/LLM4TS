package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidType() throws JsonMappingException {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testNastyClassNames() throws JsonMappingException {
 JavaType nastyType1 = mapper.getTypeFactory().constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType1));

 JavaType nastyType2 = mapper.getTypeFactory().constructType(org.springframework.beans.factory.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType2));
 }

 @Test
 public void testSpringClasses() throws JsonMappingException {
 Class<?> springType1 = org.springframework.beans.factory.config.PropertyPathFactoryBean.class;
 JavaType springJavaType1 = mapper.getTypeFactory().constructType(springType1.getName());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springJavaType1));

 Class<?> springType2 = org.springframework.beans.factory.ObjectFactory.class;
 JavaType springJavaType2 = mapper.getTypeFactory().constructType(springType2.getName());
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springJavaType2));
 }
}