package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testInstance() {
 SubTypeValidator validator = SubTypeValidator.instance();
 assertNotNull(validator);
 }

 @Test
 public void testValidateSubType_ValidType() throws Exception {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType() throws Exception {
 JavaType invalidType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), invalidType);
 }

 @Test
 public void testValidateSubType_SpringClass() throws Exception {
 JavaType springType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClass() throws Exception {
 JavaType nastyType = mapper.getTypeFactory().constructType("org.apache.commons.collections4.functors.InstantiateTransformer");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), nastyType);
 }

 @Test
 public void testValidateSubType_Interface() throws Exception {
 JavaType interfaceType = mapper.getTypeFactory().constructType(Comparable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(mapper.getConfig()), interfaceType);
 }

 @Test
 public void testDefaultNoDeserClassNameSet() {
 Set<String> defaultNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 Set<String> expected = new HashSet<>(Arrays.asList(
 // Courtesy of [https://github.com/kantega/notsoserial]
 "org.apache.commons.collections.functors.InvokerTransformer",
 "org.apache.commons.collections4.functors.InvokerTransformer",
 "org.apache.commons.collections4.functors.InstantiateTransformer",
 "org.codehaus.groovy.runtime.ConvertedClosure",
 "org.codehaus.groovy.runtime.MethodClosure",
 "org.springframework.beans.factory.ObjectFactory",
 "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
 "org.apache.xalan.xsltc.trax.TemplatesImpl",
 // [databind#1680]
 "com.sun.rowset.JdbcRowSetImpl",
 // [databind#1737]
 "java.util.logging.FileHandler",
 "java.rmi.server.UnicastRemoteObject",
 // [databind#1737] 3rd party
 "org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor",
 "org.springframework.beans.factory.config.PropertyPathFactoryBean",
 // [databind#1855] more 3rd party
 "org.apache.tomcat.dbcp.dbcp2.BasicDataSource",
 "com.sun.org.apache.bcel.internal.util.ClassLoader"
 ));
 assertEquals(expected, defaultNames);
 }

}