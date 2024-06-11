package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private final static String PREFIX_STRING = "org.springframework.";

 @Test
 public void testValidateSubTypeWithValidTypes() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null; // Not used in this test
 JavaType type1 = new JavaType(String.class);
 JavaType type2 = new JavaType(Integer.class);
 JavaType type3 = new JavaType(Collections.class);
 JavaType type4 = new JavaType(HashSet.class);

 validator.validateSubType(context, type1);
 validator.validateSubType(context, type2);
 validator.validateSubType(context, type3);
 validator.validateSubType(context, type4);
 }

 @Test
 public void testValidateSubTypeWithNastyClassNames() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null;
 JavaType nastyType1 = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);
 JavaType nastyType2 = new JavaType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
 JavaType nastyType3 = new JavaType(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
 JavaType nastyType4 = new JavaType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, nastyType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, nastyType2));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, nastyType3));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, nastyType4));
 }

 @Test
 public void testValidateSubTypeWithSpringApplicationContexts() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null;
 JavaType applicationContextType1 = new JavaType(com.fasterxml.jackson.databind.jsontype.impl.AbstractApplicationContext.class);
 JavaType applicationContextType2 = new JavaType(AbstractPointcutAdvisor.class);

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, applicationContextType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, applicationContextType2));
 }

 @Test
 public void testValidateSubTypeWithValidClassesNotStartingWithPrefix() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null;
 JavaType validType = new JavaType(StringBuilder.class);

 validator.validateSubType(context, validType);
 }

 @Test
 public void testValidateSubTypeWithNullType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = null;
 JavaType nullType = null;

 assertNull(validator.validateSubType(context, nullType));
 }
}