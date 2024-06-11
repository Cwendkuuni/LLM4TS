package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class SubTypeValidatorTest {

 private static JsonMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new JsonMapper();
 }

 /**
 * Test method for {@link SubTypeValidator#instance()}
 */
 @Test
 public void testInstance() {
 SubTypeValidator validator = SubTypeValidator.instance();
 assertEquals(SubTypeValidator.class, validator.getClass());
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with valid type
 */
 @Test
 public void testValidateSubTypeWithValidType() throws JsonMappingException, JsonProcessingException {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), validType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with illegal type
 */
 @Test
 public void testValidateSubTypeWithIllegalType() throws JsonMappingException {
 JavaType illegalType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), illegalType));
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with Spring class
 */
 @Test
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException, JsonProcessingException {
 JavaType springType = mapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), springType));
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with interface
 */
 @Test
 public void testValidateSubTypeWithInterface() throws JsonMappingException, JsonProcessingException {
 JavaType interfaceType = mapper.getTypeFactory().constructType(Comparable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), interfaceType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with nested interface
 */
 @Test
 public void testValidateSubTypeWithNestedInterface() throws JsonMappingException, JsonProcessingException {
 JavaType nestedInterfaceType = mapper.getTypeFactory().constructType(java.util.List.class.getClass().getInterfaces()[0]);
 JavaType javaUtilListType = mapper.getTypeFactory().constructType(java.util.List.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaUtilListType);
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), nestedInterfaceType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with empty set of illegal class names
 */
 @Test
 public void testValidateSubTypeWithEmptyIllegalClassNameSet() throws JsonMappingException, JsonProcessingException {
 Set<String> emptySet = new HashSet<>();
 SubTypeValidator validator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = emptySet;
 };
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), validType);
 }
}