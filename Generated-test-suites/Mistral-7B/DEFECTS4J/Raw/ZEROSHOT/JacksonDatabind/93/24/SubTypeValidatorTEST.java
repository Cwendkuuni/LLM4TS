package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidClasses() throws JsonMappingException {
 JavaType type = null; // valid class
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClasses() throws JsonMappingException {
 Set<String> defaultIllegalClasses = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;

 // Test with a known invalid class
 JavaType invalidClassType = new JavaType(getClass().getClassLoader(), "org.apache.commons.collections4.functors.InstantiateTransformer");
 validator.validateSubType(new DeserializationContext(null), invalidClassType);

 // Test with a class that starts with "org.springframework."
 JavaType springClassType = new JavaType(getClass().getClassLoader(), "org.springframework.beans.factory.ObjectFactory");
 validator.validateSubType(new DeserializationContext(null), springClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSubClassOfInvalidClass() throws JsonMappingException {
 Set<String> defaultIllegalClasses = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;

 JavaType parentClassType = new JavaType(getClass().getClassLoader(), "java.lang.Object");
 JavaType invalidSubClassType = new JavaType(getClass().getClassLoader(), "com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator$1"); // anonymous inner class of SubTypeValidator

 // The inner class is a subclass of "java.util.logging.FileHandler", which is in the list of invalid classes
 validator.validateSubType(new DeserializationContext(null), parentClassType.getType());
 validator.validateSubType(new DeserializationContext(null), invalidSubClassType);
 }

 @Test
 public void testEmptySet() throws JsonMappingException {
 Set<String> emptySet = Collections.emptySet();
 SubTypeValidator emptyValidator = new SubTypeValidator();
 emptyValidator._cfgIllegalClassNames = emptySet;

 JavaType emptyType = null; // empty validator should behave like the default one
 emptyValidator.validateSubType(new DeserializationContext(null), emptyType);
 }

 @Test
 public void testCustomIllegalClasses() throws JsonMappingException {
 Set<String> customIllegalClasses = new HashSet<>();
 customIllegalClasses.add("com.example.MyInvalidClass");
 SubTypeValidator customValidator = new SubTypeValidator();
 customValidator._cfgIllegalClassNames = customIllegalClasses;

 JavaType customInvalidType = new JavaType(getClass().getClassLoader(), "com.example.MyInvalidClass");
 customValidator.validateSubType(new DeserializationContext(null), customInvalidType);
 }
}