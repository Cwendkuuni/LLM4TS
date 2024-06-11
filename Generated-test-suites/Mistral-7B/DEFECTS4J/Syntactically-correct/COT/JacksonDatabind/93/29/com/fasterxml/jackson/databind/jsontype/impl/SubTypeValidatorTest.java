package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private DeserializationContext context;
 private JavaTypeFactory typeFactory;
 private TypeFactory typeFactoryBean;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 context = new DeserializationContext(new TestableBaseContext(), new ObjectMapper());
 typeFactory = context.getTypeFactory();
 typeFactoryBean = context.getTypeFactory().asTypeFactory();
 }

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 final String validTypeName = "com.example.ValidType";
 JavaType validType = typeFactory.constructType(validTypeName);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType() throws JsonMappingException {
 final String invalidTypeName = "org.apache.commons.collections4.functors.InvokerTransformer";
 JavaType invalidType = typeFactoryBean.constructType(invalidTypeName);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringTypeWithAbstractPointcutAdvisor() throws JsonMappingException {
 final String springTypeName = "org.springframework.beans.factory.config.AbstractPointcutAdvisor";
 JavaType springType = typeFactoryBean.constructType(springTypeName);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringTypeWithAbstractApplicationContext() throws JsonMappingException {
 final String springTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 JavaType springType = typeFactoryBean.constructType(springTypeName);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClass() throws JsonMappingException {
 final String nastyClass = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType nastyType = typeFactoryBean.constructType(nastyClass);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, nastyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClassWithCustomPrefix() throws JsonMappingException {
 final String nastyClassWithPrefix = "com.custom.prefix.org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType nastyType = typeFactoryBean.constructType(nastyClassWithPrefix);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(context, nastyType);
 }
}