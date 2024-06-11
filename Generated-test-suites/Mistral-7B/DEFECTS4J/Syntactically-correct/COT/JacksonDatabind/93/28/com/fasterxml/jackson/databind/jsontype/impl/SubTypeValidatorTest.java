package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

/**
 * JUnit test file for SubTypeValidator class.
 */
public class SubTypeValidatorTest {

 // Test method for validateSubType with a legal class
 @Test
 public void testValidateSubType_LegalClass() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType legalType = JavaType.construct(String.class);
 DeserializationContext context = null; // Not used in this test
 validator.validateSubType(context, legalType);
 }

 // Test method for validateSubType with a class in the list of prohibited classes
 @Test
 public void testValidateSubType_ProhibitedClass() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType prohibitedType = JavaType.construct(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 DeserializationContext context = null; // Not used in this test
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, prohibitedType));
 }

 // Test method for validateSubType with a Spring class above AbstractApplicationContext
 @Test
 public void testValidateSubType_SpringClassAboveAbstractApplicationContext() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClassType = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 DeserializationContext context = null; // Not used in this test
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, springClassType));
 }

 // Test method for validateSubType with a Spring class equal to AbstractApplicationContext
 @Test
 public void testValidateSubType_SpringClassEqualAbstractApplicationContext() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClassType = JavaType.construct(org.springframework.beans.factory.AbstractApplicationContext.class);
 DeserializationContext context = null; // Not used in this test
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, springClassType));
 }

 // Test method for validateSubType with a null JavaType
 @Test(expected = NullPointerException.class)
 public void testValidateSubType_NullJavaType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null; // Not used in this test
 validator.validateSubType(context, null);
 }

 // Test method for validateSubType with an empty JavaType
 @Test(expected = IllegalArgumentException.class)
 public void testValidateSubType_EmptyJavaType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null; // Not used in this test
 validator.validateSubType(context, JavaType.construct(Collections.emptyList().getClass()));
 }

 // Test method for validateSubType with a JavaType whose raw class name starts with a different prefix than "org.springframework."
 @Test
 public void testValidateSubType_JavaTypeWithDifferentPrefix() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType differentPrefixType = JavaType.construct(java.util.List.class);
 DeserializationContext context = null; // Not used in this test
 validator.validateSubType(context, differentPrefixType);
 }
}