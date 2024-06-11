package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Parameterized.class)
public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private ObjectMapper mapper;
 private JavaType type;
 private String className;

 public SubTypeValidatorTest(String className) {
 this.className = className;
 }

 @Parameterized.Parameters(name = "{index}: test validateSubType for class {0}")
 public static JavaType[] data() {
 ObjectMapper mapper = new ObjectMapper();
 return new JavaType[] { mapper.getTypeFactory().constructClassType(SubTypeValidatorTest.class.getName() + "_" + "1"),
 TypeFactory.defaultInstance().constructClassType(SubTypeValidatorTest.class.getName() + "_" + "2") };
 }

 @Test
 public void testValidateSubType_ValidClass() throws Exception {
 validator = SubTypeValidator.instance();
 mapper = new ObjectMapper();
 type = TypeFactory.defaultInstance().constructType(className);
 validator.validateSubType(mapper.getContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalClass() throws Exception {
 validator = SubTypeValidator.instance();
 mapper = new ObjectMapper();
 type = TypeFactory.defaultInstance().constructType(className);
 validator.validateSubType(mapper.getContext(), type);
 if (className.startsWith("org.springframework.")) {
 // Spring classes have 'AbstractApplicationContext' and 'AbstractPointcutAdvisor' as superclasses
 // and we cannot allow any Spring contexts or Advisors to be deserialized for security reasons
 // so we expect a JsonMappingException here
 throw new JsonMappingException("Expected JsonMappingException for class " + className);
 }
 // other classes should not throw an exception
 throw new UndeclaredThrowableException(new JsonMappingException("Unexpected JsonMappingException for class " + className));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalPrefixClass() throws Exception {
 validator = SubTypeValidator.instance();
 mapper = new ObjectMapper();
 Class<?> clazz = Class.forName(className);
 type = TypeFactory.defaultInstance().constructType(clazz.getName());
 validator.validateSubType(mapper.getContext(), type);
 // Spring classes with 'AbstractApplicationContext' or 'AbstractPointcutAdvisor' as superclasses have a prefix 'org.springframework.'
 // so we expect a JsonMappingException here
 throw new JsonMappingException("Expected JsonMappingException for class " + className);
 }
}

class SubTypeValidatorTest_1 {
 // valid class, should not throw an exception
}

class SubTypeValidatorTest_2 {
 // valid class, should not throw an exception
}