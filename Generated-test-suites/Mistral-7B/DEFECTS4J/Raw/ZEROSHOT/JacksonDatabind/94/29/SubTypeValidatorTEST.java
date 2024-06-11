package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private DeserializationContext context;
 private JavaTypeFactory typeFactory;
 private SubTypeValidator validator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 context = new DeserializationContext(null, null, null);
 typeFactory = context.getTypeFactory();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubType_knownGoodClass() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(String.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_knownBadClass() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(InvokerTransformer.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springApplicationContext() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(AbstractApplicationContext.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springPointcutAdvisor() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(AbstractPointcutAdvisor.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springRefForwardingDataSource() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(JndiRefForwardingDataSource.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springWrapperConnectionPoolDataSource() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(WrapperConnectionPoolDataSource.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springComboPooledDataSource() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(ComboPooledDataSource.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_javaLoggingFileHandler() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(FileHandler.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_sunOrgApacheBcelClassLoader() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(ClassLoader.class);
 validator.validateSubType(context, javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_apacheTomcatDbcpDataSource() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(BasicDataSource.class);
 validator.validateSubType(context, javaType);
 }

 @Test
 public void testValidateSubType_prefixSpringButNotBlockedClass() throws JsonMappingException {
 JavaType javaType = typeFactory.constructType(PrefixSpringButNotBlockedClass.class);
 validator.validateSubType(context, javaType);
 }

 private static class PrefixSpringButNotBlockedClass {
 }
}