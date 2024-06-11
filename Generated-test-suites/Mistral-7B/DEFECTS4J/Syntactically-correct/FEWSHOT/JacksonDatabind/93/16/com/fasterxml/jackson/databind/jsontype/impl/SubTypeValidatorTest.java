package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType validType = new TypeDeserializer().findContentType(new ClassIdType(Long.TYPE), new DeserializationContext(null));
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_IllegalType_NastyClassName() throws JsonMappingException {
 JavaType illegalType = new TypeDeserializer().findContentType(new ClassIdType(org.apache.commons.collections4.functors.InstantiateTransformer.class), new DeserializationContext(null));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testValidateSubType_IllegalType_SpringFrameworkSubclass() throws JsonMappingException {
 Class<?> springClass = AbstractApplicationContext.class;
 JavaType illegalType = new TypeDeserializer().findContentType(new ClassIdType(springClass), new DeserializationContext(null));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testValidateSubType_IllegalType_SpringFrameworkInterface() throws JsonMappingException {
 Class<?> springInterface = AbstractPointcutAdvisor.class;
 JavaType illegalType = new TypeDeserializer().findContentType(new ClassIdType(springInterface.getClassLoader().loadClass(springInterface.getName().replace("Interface", ""))), new DeserializationContext(null));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), illegalType));
 }
}