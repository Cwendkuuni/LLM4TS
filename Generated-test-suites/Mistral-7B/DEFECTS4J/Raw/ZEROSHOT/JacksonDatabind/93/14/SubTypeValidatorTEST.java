package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator SUB_TYPE_VALIDATOR = SubTypeValidator.instance();

 @Test
 public void testDefaultNoDeserClassNames() {
 Set<String> defaultNoDeserClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 Set<String> expectedDefaultNoDeserClassNames = new HashSet<>();
 expectedDefaultNoDeserClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
 expectedDefaultNoDeserClassNames.add("org.apache.commons.collections.functors.InstantiateTransformer");
 expectedDefaultNoDeserClassNames.add("org.apache.commons.collections4.functors.InvokerTransformer");
 expectedDefaultNoDeserClassNames.add("org.apache.commons.collections4.functors.InstantiateTransformer");
 expectedDefaultNoDeserClassNames.add("org.codehaus.groovy.runtime.ConvertedClosure");
 expectedDefaultNoDeserClassNames.add("org.codehaus.groovy.runtime.MethodClosure");
 expectedDefaultNoDeserClassNames.add("org.springframework.beans.factory.ObjectFactory");
 expectedDefaultNoDeserClassNames.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
 expectedDefaultNoDeserClassNames.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
 expectedDefaultNoDeserClassNames.add("com.sun.rowset.JdbcRowSetImpl");
 expectedDefaultNoDeserClassNames.add("java.util.logging.FileHandler");
 expectedDefaultNoDeserClassNames.add("java.rmi.server.UnicastRemoteObject");
 expectedDefaultNoDeserClassNames.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 expectedDefaultNoDeserClassNames.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
 expectedDefaultNoDeserClassNames.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
 expectedDefaultNoDeserClassNames.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
 expectedDefaultNoDeserClassNames.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

 assertEquals(expectedDefaultNoDeserClassNames, defaultNoDeserClassNames);
 }

 @Test
 public void testValidateSubTypeWithValidClass() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 SUB_TYPE_VALIDATOR.validateSubType(new DeserializationContext(JavaTypeFactory.instance(), Collections.emptyMap()), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithInvalidClass() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
 SUB_TYPE_VALIDATOR.validateSubType(new DeserializationContext(JavaTypeFactory.instance(), Collections.emptyMap()), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
 SUB_TYPE_VALIDATOR.validateSubType(new DeserializationContext(JavaTypeFactory.instance(), Collections.emptyMap()), javaType);
 }
}