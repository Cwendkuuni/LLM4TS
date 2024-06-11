package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.throwException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidClassName() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mockDeserializationContext(), validType);
 }

 @Test
 public void testValidateSubType_IllegalClassName_EmptySet() throws Exception {
 SubTypeValidator validator = new SubTypeValidator();
 Set<String> emptySet = Collections.emptySet();
 validator._cfgIllegalClassNames = emptySet;

 JavaType illegalType = mapper.getTypeFactory().constructType(Object.class);
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_SpringClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType illegalType1 = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType1)
 );

 JavaType illegalType2 = mapper.getTypeFactory().constructType("org.springframework.beans.factory.AbstractApplicationContext");
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType2)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_OtherClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType illegalType = mapper.getTypeFactory().constructType("org.apache.commons.collections4.functors.InvokerTransformer");
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_Array() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType illegalType = mapper.getTypeFactory().constructArrayType(Long.TYPE, new Long[] { 1L, 2L });
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_HashSet() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType illegalType = mapper.getTypeFactory().constructType(HashSet.class);
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_CustomSet() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType customSetType = mapper.getTypeFactory().constructType(CustomSet.class);
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), customSetType)
 );
 }

 @Test
 public void testValidateSubType_IllegalClassName_CustomClassWithPrefixSpring() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 JavaType illegalType = mapper.getTypeFactory().constructType("com.mycompany.mypackage.MyCustomClassWithSpringPrefix");
 throwException(
 JsonMappingException.class,
 () -> validator.validateSubType(mockDeserializationContext(), illegalType)
 );
 }

 private static DeserializationContext mockDeserializationContext() {
 return mock(DeserializationContext.class);
 }

}