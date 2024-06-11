package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;
 private static Set<String> defaultNoDeserClassName;

 @BeforeClass
 public static void init() {
 validator = SubTypeValidator.instance();
 defaultNoDeserClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 }

 @Test
 public void testValidateSubType_validClass() throws JsonMappingException {
 final JavaType type = null; // valid type, no validation required
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubType_illegalClass_nastyClass() throws JsonMappingException {
 final JavaType type = getClassType("org.apache.commons.collections4.functors.InvokerTransformer");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), type));
 }

 @Test
 public void testValidateSubType_illegalClass_springClassWithSubClass() throws JsonMappingException {
 final JavaType type = getClassType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_illegalClass_springClass() throws JsonMappingException {
 final JavaType type = getClassType("org.springframework.beans.factory.ObjectFactory");
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubType_illegalClass_prefixString() throws JsonMappingException {
 final JavaType type = getClassType("org.springframework.beans.factory.AbstractApplicationContext");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), type));
 }

 @Test
 public void testValidateSubType_illegalClass_prefixString_subclass() throws JsonMappingException {
 final JavaType type = getClassType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 validator.validateSubType(new DeserializationContext(null), type);
 }

 private JavaType getClassType(String className) {
 try {
 return new JavaType(getClass().getClassLoader().loadClass(className));
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }
}