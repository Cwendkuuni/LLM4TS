package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_validType() throws Exception {
 JavaType validType = new JavaType(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_invalidType_inList() throws Exception {
 String invalidClassName = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType invalidType = new JavaType(Class.forName(invalidClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testValidateSubType_invalidType_startsWithPrefix() throws Exception {
 String invalidClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType invalidType = new JavaType(Class.forName(invalidClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_throwException() throws Exception {
 String illegalClassName = "java.util.logging.FileHandler";
 JavaType illegalType = new JavaType(Class.forName(illegalClassName));
 validator.validateSubType(new DeserializationContext(null), illegalType);
 }

}