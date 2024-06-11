package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() throws Exception {
 DeserializationContext ctxt = null; // not used in this test
 JavaType validType = JavaTypeFactory.instance.constructType(String.class);
 validator.validateSubType(ctxt, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyType() throws Exception {
 DeserializationContext ctxt = null;
 JavaType nastyType = JavaTypeFactory.instance.constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 validator.validateSubType(ctxt, nastyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringBeanType() throws Exception {
 DeserializationContext ctxt = null;
 JavaType springType = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 validator.validateSubType(ctxt, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testJdkProvidedType() throws Exception {
 DeserializationContext ctxt = null;
 JavaType jdkType = JavaTypeFactory.instance.constructType(java.util.logging.FileHandler.class);
 validator.validateSubType(ctxt, jdkType);
 }

 @Test(expected = JsonMappingException.class)
 public void testThirdPartyType() throws Exception {
 DeserializationContext ctxt = null;
 JavaType thirdPartyType = JavaTypeFactory.instance.constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
 validator.validateSubType(ctxt, thirdPartyType);
 }
}