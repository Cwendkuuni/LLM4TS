package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testInstance() {
 SubTypeValidator validator = SubTypeValidator.instance();
 assertNotNull(validator);
 }

 @Test
 public void testValidateSubType_legalClass() throws JsonMappingException {
 JavaType legalType = MAPPER.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, legalType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_illegalClass() throws JsonMappingException {
 JavaType illegalType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, illegalType);
 }

 @Test
 public void testValidateSubType_springClass() throws JsonMappingException, JsonProcessingException {
 JavaType springType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
 ObjectMapper springMapper = new ObjectMapper();
 springMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 springMapper.setDefaultTyping(DefaultTyping.OBJECT_ARRAY);
 Object springObject = springMapper.readValue("[org.springframework.beans.factory.config.PropertyPathFactoryBean]", Object[].class);
 JavaType springPropertyPathFactoryBeanType = MAPPER.getTypeFactory().constructType(springObject.getClass().getComponentType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, springPropertyPathFactoryBeanType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springClass_invalid() throws JsonMappingException, JsonProcessingException {
 JavaType springType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
 ObjectMapper springMapper = new ObjectMapper();
 springMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 springMapper.setDefaultTyping(DefaultTyping.OBJECT_ARRAY);
 Object springObject = springMapper.readValue("[org.springframework.beans.factory.config.AbstractBeanFactoryPointcutAdvisor]", Object[].class);
 JavaType springAbstractBeanFactoryPointcutAdvisorType = MAPPER.getTypeFactory().constructType(springObject.getClass().getComponentType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, springAbstractBeanFactoryPointcutAdvisorType);
 }

 @Test
 public void testValidateSubType_emptySet() {
 SubTypeValidator emptyValidator = new SubTypeValidator();
 Set<String> emptySet = Collections.emptySet();
 emptyValidator._cfgIllegalClassNames = emptySet;
 try {
 emptyValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(String.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_nonEmptySet() {
 SubTypeValidator nonEmptyValidator = new SubTypeValidator();
 Set<String> nonEmptySet = new HashSet<>();
 nonEmptySet.add("org.apache.commons.collections.functors.InvokerTransformer");
 nonEmptyValidator._cfgIllegalClassNames = nonEmptySet;
 try {
 nonEmptyValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(String.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 nonEmptyValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(SubTypeValidator.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 nonEmptyValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(Object[].class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 nonEmptyValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(SubTypeValidator.class.getName()));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_customIllegalClass() throws JsonMappingException {
 SubTypeValidator customValidator = new SubTypeValidator();
 Set<String> customSet = new HashSet<>();
 customSet.add("com.custom.IllegalClass");
 customValidator._cfgIllegalClassNames = customSet;
 try {
 customValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(String.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 customValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(SubTypeValidator.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 customValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(Object[].class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 customValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(String.class.getName()));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 try {
 customValidator.validateSubType(null, MAPPER.getTypeFactory().constructType(com.custom.IllegalClass.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test
 public void testValidateSubType_prefixString() throws JsonMappingException {
 JavaType prefixStringType = MAPPER.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, prefixStringType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_prefixString_invalid() throws JsonMappingException {
 JavaType prefixStringType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, prefixStringType);
 }

 @Test
 public void testValidateSubType_prefixString_valid() {
 JavaType prefixStringType = MAPPER.getTypeFactory().constructType(Object[].class);
 ObjectMapper prefixStringMapper = new ObjectMapper();
 prefixStringMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 prefixStringMapper.setDefaultTyping(DefaultTyping.OBJECT_ARRAY);
 Object prefixStringObject = prefixStringMapper.readValue("[org.springframework.beans.factory.ObjectFactory]", Object[].class);
 JavaType prefixStringObjectType = MAPPER.getTypeFactory().constructType(prefixStringObject.getClass().getComponentType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, prefixStringObjectType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_prefixString_invalid_2() throws JsonMappingException {
 JavaType prefixStringType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, prefixStringType);
 }
}