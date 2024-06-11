package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testValidType() throws Exception {
 final JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test
 public void testSpringType() throws Exception {
 final JavaType springType = objectMapper.getTypeFactory().constructType(Object.class);
 springType.setRawClass(SubTypeValidator.class.getPackage().getName() + ".AbstractApplicationContext");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType));
 }

 @Test
 public void testSpringInterfaceType() throws Exception {
 final JavaType springInterfaceType = objectMapper.getTypeFactory().constructType(Object.class);
 springInterfaceType.setRawClass(SubTypeValidator.class.getPackage().getName() + ".AbstractPointcutAdvisor");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), springInterfaceType);
 }

 @Test
 public void testNastyClassType() throws Exception {
 final JavaType nastyClassType = objectMapper.getTypeFactory().constructType(Object.class);
 nastyClassType.setRawClass(SubTypeValidator.class.getPackage().getName() + ".InvokerTransformer");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyClassType));
 }

 @Test
 public void testNastyClassPrefixType() throws Exception {
 final JavaType nastyClassPrefixType = objectMapper.getTypeFactory()
 .constructType(Object.class);
 nastyClassPrefixType.setRawClass("org.apache.commons.collections4.functors.InstantiateTransformer");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyClassPrefixType));
 }
}