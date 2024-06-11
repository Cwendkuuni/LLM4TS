package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = JavaTypeFactory.instance.constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testNastyClassWithPrefix() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = JavaTypeFactory.instance.constructType(AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType));
 }

 @Test
 public void testNastyClassWithSimpleName() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = JavaTypeFactory.instance.constructType(AbstractPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType));
 }

 @Test
 public void testNastyClassWithPrefixAndSuperclass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = JavaTypeFactory.instance.constructType(AbstractBeanFactoryPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType));
 }

 @Test
 public void testValidClassWithPrefix() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = JavaTypeFactory.instance.constructType(PrefixClass.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testNastyClassWithLongName() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = JavaTypeFactory.instance.constructType(LongNastyClass.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType));
 }

 private DeserializationContext createContext() {
 return MAPPER.getSerializers().findValueSerializer(String.class).getConfig().getContext();
 }
}

class PrefixClass {
 // This class should be valid as its name starts with the prefix, but does not match any of the forbidden classes
}

class AbstractApplicationContext extends Object {
 // This class is forbidden, it is a Spring ApplicationContext
}

class AbstractPointcutAdvisor extends Object {
 // This class is forbidden, it is a Spring PointcutAdvisor
}

class AbstractBeanFactoryPointcutAdvisor extends Object {
 // This class is forbidden, it is a Spring BeanFactoryPointcutAdvisor
}

class LongNastyClass {
 // This class should be forbidden due to its long name
 public static final String LONG_NAME = "org.long.nasty.class.name";
}