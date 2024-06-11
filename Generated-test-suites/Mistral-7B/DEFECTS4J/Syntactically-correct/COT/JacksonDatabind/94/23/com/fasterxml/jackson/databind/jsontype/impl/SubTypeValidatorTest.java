package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test class for SubTypeValidator.
 */
public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_validType() throws JsonMappingException {
 JavaType validType = JavaType.construct(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_nastyClass() throws JsonMappingException {
 JavaType nastyType = JavaType.construct(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), nastyType));
 }

 @Test
 public void testValidateSubType_springApplicationContext() throws JsonMappingException {
 JavaType springApplicationContextType = JavaType.construct(org.springframework.context.ApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springApplicationContextType));
 }

 @Test
 public void testValidateSubType_springAbstractPointcutAdvisor() throws JsonMappingException {
 JavaType springAbstractPointcutAdvisorType = JavaType.construct(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springAbstractPointcutAdvisorType));
 }

 @Test
 public void testValidateSubType_springAbstractApplicationContext() throws JsonMappingException {
 JavaType springAbstractApplicationContextType = JavaType.construct(org.springframework.context.AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springAbstractApplicationContextType));
 }

 @Test
 public void testValidateSubType_emptyClass() throws JsonMappingException {
 JavaType emptyClassType = JavaType.construct(Object[].class);
 validator.validateSubType(new DeserializationContext(null), emptyClassType);
 }

 @Test
 public void testValidateSubType_interface() throws JsonMappingException {
 JavaType interfaceType = JavaType.construct(Comparable.class);
 validator.validateSubType(new DeserializationContext(null), interfaceType);
 }

 @Test
 public void testValidateSubType_prefixSpringClass() throws JsonMappingException {
 JavaType prefixSpringClassType = JavaType.construct(org.springframework.beans.factory.ObjectFactory.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), prefixSpringClassType));
 }

 @Test
 public void testValidateSubType_customNastyClass() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.custom.nasty.Class");
 SubTypeValidator customValidator = new SubTypeValidator() {
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customNastyType = JavaType.construct(CustomNastyClass.class);
 try {
 customValidator.validateSubType(new DeserializationContext(null), customNastyType);
 assertTrue(false);
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_customNastyInterface() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.custom.nasty.Interface");
 SubTypeValidator customValidator = new SubTypeValidator() {
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customNastyInterfaceType = JavaType.construct(CustomNastyInterface.class);
 try {
 customValidator.validateSubType(new DeserializationContext(null), customNastyInterfaceType);
 assertTrue(false);
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_nullType() {
 assertThrows(NullPointerException.class, () -> validator.validateSubType(null, JavaType.construct(Object.class)));
 }

 @Test
 public void testValidateSubType_nullDeserializationContext() {
 assertThrows(NullPointerException.class, () -> validator.validateSubType(null, JavaType.construct(Object.class)));
 }
}