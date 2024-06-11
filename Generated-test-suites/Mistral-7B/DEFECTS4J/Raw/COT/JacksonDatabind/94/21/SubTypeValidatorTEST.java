package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

import org.junit.Test;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_whenTypeIsValid_thenPass() {
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsInvalid_thenThrowException() {
 JavaType invalidType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
 validator.validateSubType(createContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsSpringBeanFactory_thenThrowException() {
 JavaType springType = objectMapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 validator.validateSubType(createContext(), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsCommonsCollectionsFunctor_thenThrowException() {
 JavaType commonsType = objectMapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
 validator.validateSubType(createContext(), commonsType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsJndiRefForwardingDataSource_thenThrowException() {
 JavaType jndiType = objectMapper.getTypeFactory().constructType(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
 validator.validateSubType(createContext(), jndiType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsWrapperConnectionPoolDataSource_thenThrowException() {
 JavaType wrapperType = objectMapper.getTypeFactory().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
 validator.validateSubType(createContext(), wrapperType);
 }

 @Test
 public void testValidateSubType_whenTypeIsSpringAbstractPointcutAdvisor_thenPass() {
 JavaType advisorType = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 validator.validateSubType(createContext(), advisorType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsJavaUtilLoggingFileHandler_thenThrowException() {
 JavaType loggingType = objectMapper.getTypeFactory().constructType(java.util.logging.FileHandler.class);
 validator.validateSubType(createContext(), loggingType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsJavaRmiUnicastRemoteObject_thenThrowException() {
 JavaType rmiType = objectMapper.getTypeFactory().constructType(java.rmi.server.UnicastRemoteObject.class);
 validator.validateSubType(createContext(), rmiType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsThirdPartyDataSource_thenThrowException() {
 JavaType thirdPartyType = objectMapper.getTypeFactory().constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
 validator.validateSubType(createContext(), thirdPartyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_whenTypeIsCustomClass_thenThrowException() {
 JavaType customType = objectMapper.getTypeFactory().constructType(CustomClass.class);
 validator.validateSubType(createContext(), customType);
 }

 @Test
 public void testValidateSubType_whenTypeIsInterface_thenPass() {
 JavaType interfaceType = objectMapper.getTypeFactory().constructType(MyInterface.class);
 validator.validateSubType(createContext(), interfaceType);
 }

 private DeserializationContext createContext() {
 return objectMapper.getDeserializationContext();
 }

}