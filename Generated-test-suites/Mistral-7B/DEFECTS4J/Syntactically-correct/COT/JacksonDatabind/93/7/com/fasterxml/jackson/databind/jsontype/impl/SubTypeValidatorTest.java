package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

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

 private SubTypeValidator validator = SubTypeValidator.instance();
 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDefaultNoDeserClassNames() {
 Set<String> classNames = validator.get_cfgIllegalClassNames();
 assertEquals("Size of default no deser class names should be 18", 18, classNames.size());
 }

 @Test
 public void testValidateSubType_whenTypeIsInDefaultNoDeserClassNames_thenThrowJsonMappingException() throws Exception {
 String typeName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType type = mapper.getTypeFactory().constructType(typeName);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, type));
 }

 @Test
 public void testValidateSubType_whenTypeIsSpringFrameworkSubClass_thenReturn() throws Exception {
 String typeName = "com.fasterxml.jackson.databind.JsonMappingException";
 JavaType type = mapper.getTypeFactory().constructType(typeName);
 validator.validateSubType(null, type);
 }

 @Test
 public void testValidateSubType_whenTypeStartsWithSpringPrefix_thenPerformDeepCheck() throws Exception {
 String typeName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType type = mapper.getTypeFactory().constructType(typeName);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, type));
 }

 @Test
 public void testValidateSubType_whenTypeIsNotInDefaultNoDeserClassNamesAndNotSpringSubclass_thenReturn() throws Exception {
 String typeName = "java.lang.String";
 JavaType type = mapper.getTypeFactory().constructType(typeName);
 validator.validateSubType(null, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeNameIsNull_thenThrowJsonMappingException() throws Exception {
 validator.validateSubType(null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsAnInterface_thenThrowJsonMappingException() throws Exception {
 String typeName = "java.io.Serializable";
 JavaType type = mapper.getTypeFactory().constructType(typeName);
 validator.validateSubType(null, type);
 }

 @Test(expected = JsonProcessingException.class)
 public void testValidateSubType_whenContextIsNull_thenThrowJsonProcessingException() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(String.class.getName());
 validator.validateSubType(null, type);
 }
}