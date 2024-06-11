package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link SubTypeValidator}
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubType_whenValidType_thenSuccess() throws Exception {
 final JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(createContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenInvalidType_thenException() throws Exception {
 final JavaType invalidType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(createContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenSpringInterface_thenException() throws Exception {
 final JavaType springInterfaceType = mapper.getTypeFactory().constructType(SubTypeValidator.AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(createContext(), springInterfaceType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenSpringApplicationContext_thenException() throws Exception {
 final JavaType springApplicationContextType = mapper.getTypeFactory().constructType(SubTypeValidator.AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(createContext(), springApplicationContextType);
 }

 @Test
 public void testValidateSubType_whenSpringClassWithSuperclass_thenSuccess() throws Exception {
 final JavaType springClassType = mapper.getTypeFactory().constructType(SubTypeValidator.AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(createContext(), springClassType);
 }

 @Test
 public void testValidateSubType_whenCustomIllegalClass_thenException() throws Exception {
 final String customIllegalClassName = "com.custom.package.NastyClass";
 Set<String> illegalClassNames = new HashSet<>();
 illegalClassNames.add(customIllegalClassName);
 SubTypeValidator validator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames() {
 return Collections.unmodifiableSet(illegalClassNames);
 }
 };
 final JavaType customIllegalType = mapper.getTypeFactory().constructType(Class.forName(customIllegalClassName));
 validator.validateSubType(createContext(), customIllegalType);
 }

 private DeserializationContext createContext() {
 return mapper.getDeserializationContext();
 }
}