package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new JsonMapper();
 objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testValidType() throws Exception {
 String json = "{\"type\": \"com.acme.ValidType\"}";
 JavaType validType = objectMapper.getTypeFactory().constructType(new ClassIdType(getClass().getName(), "ValidType"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 ObjectReader reader = objectMapper.readerForType(validType);
 Assert.assertEquals(new ValidType(), reader.readValue(json));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws Exception {
 String json = "{\"type\": \"com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator\"}";
 JavaType invalidType = objectMapper.getTypeFactory().constructType(new ClassIdType(getClass().getName(), "SubTypeValidator"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyType() throws Exception {
 String json = "{\"type\": \"org.apache.commons.collections4.functors.InstantiateTransformer\"}";
 JavaType nastyType = objectMapper.getTypeFactory().constructType(new ClassIdType(getClass().getName(), "SubTypeValidator"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), nastyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringType() throws Exception {
 String json = "{\"type\": \"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType springType = objectMapper.getTypeFactory().constructType(new ClassIdType(getClass().getName(), "SubTypeValidator"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), springType);
 }

}